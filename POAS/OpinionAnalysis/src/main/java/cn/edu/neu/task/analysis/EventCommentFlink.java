package cn.edu.neu.task.analysis;

import cn.edu.neu.bean.EventComment;
import cn.edu.neu.bean.EventCommentStat;
import cn.edu.neu.cons.ClickHouseConstant;
import cn.edu.neu.cons.KafkaConstant;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author 32098
 */
public class EventCommentFlink {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventCommentStatic{
        private String eventName;
        private Long manCommentCount;
        private Long womanCommentCount;
        private Long posCommentCount;
        private Long negCommentCount;
        private Long allCount;
        private String newestTime;
    }

    public static void main(String[] args) throws Exception {
        Properties pros = new Properties();
        pros.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        pros.setProperty("group.id", "groupB");
        pros.setProperty("auto.offset.reset", "latest");
        pros.setProperty("enable.auto.commit", "true");
        pros.setProperty("auto.commit.interval.ms", "2000");

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(
                KafkaConstant.EVENT_COMMENT,
                new SimpleStringSchema(),
                pros
        );
        kafkaSource.setStartFromEarliest();
        // 重新设置从 kafka 记录的 group.id 的位置开始消费，如果没有记录则从 auto.offset.reset 配置开始消费
        // kafkaSource.setStartFromGroupOffsets();

        int batchSize = 500;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 1. env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 设置checkpoint的时间间隔为1000ms做一次Checkpoint
        env.enableCheckpointing(60000);
        // 设置checkpoint的执行模式为EXACTLY_ONCE(默认)
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 设置checkpoint的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 设置两个checkpoint之间最少等待时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000);
        // 设置同一时间有多少个checkpoint可以同时执行
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 设置是否清理检查点
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 2. source
        DataStreamSource<String> kafkaDataStream = env.addSource(kafkaSource);

        // 3. transformation
        // to java object
        SingleOutputStreamOperator<EventComment> eventCommentStream = kafkaDataStream.map(new MapFunction<String, EventComment>() {
            @Override
            public EventComment map(String s) throws Exception {
                return JSON.parseObject(s, EventComment.class);
            }
        });

        /*
         * Code
         */

        // write to event_comment_detail
        // eventCommentStream.print("event comment");
        eventCommentStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_comment_detail VALUES (?,?,?,?,?,?,?)",
                        new JdbcStatementBuilder<EventComment>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, EventComment eventComment) throws SQLException {
                                // System.out.println(eventComment);
                                preparedStatement.setString(1, eventComment.getEventName());
                                preparedStatement.setString(2, eventComment.getCommentId());
                                preparedStatement.setString(3, eventComment.getCommentText());
                                preparedStatement.setInt(4, eventComment.getCommentReply());
                                preparedStatement.setInt(5, eventComment.getCommentLike());
                                preparedStatement.setString(6, eventComment.getCommentEmotion());
                                preparedStatement.setString(7, eventComment.getSpiderTime());
                            }
                        },
                        new JdbcExecutionOptions.Builder().withBatchSize(batchSize).build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName(ClickHouseConstant.DRIVER)
                                .withUrl(ClickHouseConstant.URL)
                                .withUsername(ClickHouseConstant.USERNAME)
                                .withPassword(ClickHouseConstant.PASSWORD)
                                .build()
                )
        );

        // write to event_comment_stat
        SingleOutputStreamOperator<EventComment> eventCommentTempStream = eventCommentStream
                .keyBy(EventComment::getEventName)
                .map(
                        new RichMapFunction<EventComment, EventComment>() {
                            // -a.定义状态用来存储已处理过的评论ID
                            private ListState<String> commentIdsState = null;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                // -b.定义状态描述符: 描述状态的名称和里面的数据类型
                                ListStateDescriptor<String> descriptor = new ListStateDescriptor<String>("commentIdsState", String.class);
                                // -c.根据状态描述符初始化状态
                                commentIdsState = getRuntimeContext().getListState(descriptor);
                            }

                            @Override
                            public EventComment map(EventComment eventCommentLongTuple) throws Exception {
                                // 通过有状态计算防止同一个评论多次计算
                                for (String s : commentIdsState.get()) {
                                    if (s.equals(eventCommentLongTuple.getCommentId())) {
                                        return null;
                                    }
                                }
                                commentIdsState.add(eventCommentLongTuple.getCommentId());
                                return eventCommentLongTuple;
                            }
                        }
                ).filter(new FilterFunction<EventComment>() {
//                    private int count = 0;
//                    private Set<String> set = new HashSet<>();
                    @Override
                    public boolean filter(EventComment eventComment) throws Exception {
//                        if(eventComment!=null){
//                            // System.out.println(eventComment.getEventName());
//                            if(!set.contains(eventComment.getEventName())){
//                                count++;
//                                System.out.println(count+"%"+eventComment.getEventName()+"-"+eventComment.getSpiderTime());
//                                set.add(eventComment.getEventName());
//                            }
//                        }
                        return eventComment != null;
                    }
                });

        SingleOutputStreamOperator<EventCommentStat> eventCommentStatStream = eventCommentTempStream.keyBy(EventComment::getEventName)
                .window(TumblingProcessingTimeWindows.of(Time.of(Long.MAX_VALUE, TimeUnit.DAYS)))
                .process(
                        new ProcessWindowFunction<EventComment, EventCommentStat, String, TimeWindow>() {
                            MapState<String, EventCommentStatic> mapState = null;

                            @Override
                            public void open(Configuration parameters) {
                                MapStateDescriptor<String, EventCommentStatic> descriptor = new MapStateDescriptor<String, EventCommentStatic>("mapState", String.class, EventCommentStatic.class);
                                mapState = getRuntimeContext().getMapState(descriptor);
                            }

                            @Override
                            public void process(String eventName, Context context, Iterable<EventComment> iterable, Collector<EventCommentStat> collector) throws Exception {
                                EventCommentStatic eventCommentStatic = mapState.get(eventName);

                                // System.out.println(eventCommentStatic);

                                for (EventComment eventComment : iterable) {
                                    if(eventCommentStatic!=null){
                                        if("m".equals(eventComment.getCommenterGender())){
                                            eventCommentStatic.setManCommentCount(eventCommentStatic.getManCommentCount()+1);
                                        }else{
                                            eventCommentStatic.setWomanCommentCount(eventCommentStatic.getWomanCommentCount()+1);
                                        }
                                        if("1".equals(eventComment.getCommentEmotion())){
                                            eventCommentStatic.setPosCommentCount(eventCommentStatic.getPosCommentCount()+1);
                                        }else if("0".equals(eventComment.getCommentEmotion())){
                                            eventCommentStatic.setNegCommentCount(eventCommentStatic.getNegCommentCount()+1);
                                        }
                                        eventCommentStatic.setAllCount(eventCommentStatic.getAllCount()+1);
                                        eventCommentStatic.setNewestTime(
                                                eventComment.getSpiderTime().compareTo(eventCommentStatic.getNewestTime())>0?eventComment.getSpiderTime():eventCommentStatic.getNewestTime()
                                        );
                                    }else{
                                        eventCommentStatic = new EventCommentStatic();
                                        eventCommentStatic.setEventName(eventComment.getEventName());
                                        if("m".equals(eventComment.getCommenterGender())){
                                            eventCommentStatic.setManCommentCount(1L);
                                            eventCommentStatic.setWomanCommentCount(0L);
                                        }else{
                                            eventCommentStatic.setManCommentCount(0L);
                                            eventCommentStatic.setWomanCommentCount(1L);
                                        }
                                        if("1".equals(eventComment.getCommentEmotion())){
                                            eventCommentStatic.setPosCommentCount(1L);
                                            eventCommentStatic.setNegCommentCount(0L);
                                        }else {
                                            eventCommentStatic.setPosCommentCount(0L);
                                            eventCommentStatic.setNegCommentCount(1L);
                                        }
                                        eventCommentStatic.setAllCount(1L);
                                        eventCommentStatic.setNewestTime(eventComment.getSpiderTime());
                                    }
                                }

                                mapState.put(eventName, eventCommentStatic);

                                EventCommentStat stat = new EventCommentStat();
                                stat.setEventName(eventName);

                                stat.setManCommentProportion(
                                        BigDecimal.valueOf((double) eventCommentStatic.getManCommentCount() / eventCommentStatic.getAllCount()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()
                                );
                                stat.setWomanCommentProportion(
                                        BigDecimal.valueOf((double) eventCommentStatic.getWomanCommentCount() / eventCommentStatic.getAllCount()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()
                                );
                                stat.setPosCommentProportion(
                                        BigDecimal.valueOf((double) eventCommentStatic.getPosCommentCount() / eventCommentStatic.getAllCount()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()
                                );
                                stat.setNegCommentProportion(
                                        BigDecimal.valueOf((double) eventCommentStatic.getNegCommentCount() / eventCommentStatic.getAllCount()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()
                                );
                                stat.setTime(eventCommentStatic.getNewestTime());

                                collector.collect(stat);
                            }
                        }
                );

        eventCommentStatStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_comment_stat VALUES (?,?,?,?,?,?)",
                        new JdbcStatementBuilder<EventCommentStat>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, EventCommentStat eventCommentStat) throws SQLException {
                                preparedStatement.setString(1, eventCommentStat.getEventName());
                                preparedStatement.setDouble(2, eventCommentStat.getManCommentProportion());
                                preparedStatement.setDouble(3, eventCommentStat.getWomanCommentProportion());
                                preparedStatement.setDouble(4, eventCommentStat.getPosCommentProportion());
                                preparedStatement.setDouble(5, eventCommentStat.getNegCommentProportion());
                                preparedStatement.setString(6, eventCommentStat.getTime());
                            }
                        },
                        new JdbcExecutionOptions.Builder().withBatchSize(batchSize).build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName(ClickHouseConstant.DRIVER)
                                .withUrl(ClickHouseConstant.URL)
                                .withUsername(ClickHouseConstant.USERNAME)
                                .withPassword(ClickHouseConstant.PASSWORD)
                                .build()
                )
        );
        // eventCommentStatStream.print("event comment stat");

        env.execute("event comment");
    }
}

/*
// Error Case 1
SingleOutputStreamOperator<EventCommentStat> eventCommentStatStream = eventCommentTempStream
        .keyBy(EventComment::getEventName)
        .window(TumblingProcessingTimeWindows.of(Time.of(Long.MAX_VALUE, TimeUnit.DAYS)))
        .aggregate(
                new AggregateFunction<EventComment, Tuple7<String, Long, Long, Long, Long, Long, String>, Tuple6<String, Double, Double, Double, Double, String>>() {
                    @Override
                    public Tuple7<String, Long, Long, Long, Long, Long, String> createAccumulator() {
                        return Tuple7.of("", 0L, 0L, 0L, 0L, 0L, "");
                    }

                    @SneakyThrows
                    @Override
                    public Tuple7<String, Long, Long, Long, Long, Long, String> add(EventComment eventComment, Tuple7<String, Long, Long, Long, Long, Long, String> acc) {
                        String eventName = eventComment.getEventName();
                        long manCount = 0L;
                        long womanCount = 0L;
                        if ("m".equals(eventComment.getCommenterGender())) {
                            manCount = 1L;
                        } else {
                            womanCount = 1L;
                        }
                        long posCount = 0L;
                        long negCount = 0L;
                        if ("1".equals(eventComment.getCommentEmotion())) {
                            posCount = 1L;
                        } else {
                            negCount = 1L;
                        }
                        // System.out.println(Tuple7.of(eventName, acc.f1 + manCount, acc.f2 + womanCount, acc.f3 + posCount, acc.f4 + negCount, acc.f5 + 1, eventComment.getSpiderTime().compareTo(acc.f6) > 0 ? eventComment.getSpiderTime() : acc.f6));
                        return Tuple7.of(eventName, acc.f1 + manCount, acc.f2 + womanCount, acc.f3 + posCount, acc.f4 + negCount, acc.f5 + 1, eventComment.getSpiderTime().compareTo(acc.f6) > 0 ? eventComment.getSpiderTime() : acc.f6);
                    }

                    @Override
                    public Tuple6<String, Double, Double, Double, Double, String> getResult(Tuple7<String, Long, Long, Long, Long, Long, String> accFinal) {
                        String eventName = accFinal.f0;
                        double manProportion = new BigDecimal((double) accFinal.f1 / accFinal.f5).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double womanProportion = new BigDecimal((double) accFinal.f2 / accFinal.f5).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double posProportion = new BigDecimal((double) accFinal.f3 / accFinal.f5).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        double negProportion = new BigDecimal((double) accFinal.f4 / accFinal.f5).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        System.out.println(Tuple6.of(eventName, manProportion, womanProportion, posProportion, negProportion, accFinal.f6));
                        return Tuple6.of(eventName, manProportion, womanProportion, posProportion, negProportion, accFinal.f6);
                    }

                    @Override
                    public Tuple7<String, Long, Long, Long, Long, Long, String> merge(Tuple7<String, Long, Long, Long, Long, Long, String> accA, Tuple7<String, Long, Long, Long, Long, Long, String> accB) {
                        return Tuple7.of(accA.f0, accA.f1 + accB.f1, accA.f2 + accB.f2, accA.f3 + accB.f3, accA.f4 + accB.f4, accA.f5 + accB.f5, accA.f6.compareTo(accB.f6) > 0 ? accA.f6 : accB.f6);
                    }
                },
                new WindowFunction<Tuple6<String, Double, Double, Double, Double, String>, EventCommentStat, String, TimeWindow>() {
                    @Override
                    public void apply(String eventName, TimeWindow timeWindow, Iterable<Tuple6<String, Double, Double, Double, Double, String>> iterable, Collector<EventCommentStat> collector) throws Exception {
                        Tuple6<String, Double, Double, Double, Double, String> result = iterable.iterator().next();
                        collector.collect(new EventCommentStat(eventName, result.f1, result.f2, result.f3, result.f4, result.f5));
                    }
                });
 */

/*
// Error Case 2
// https://www.cnblogs.com/dduo/articles/14874199.html
SingleOutputStreamOperator<EventCommentStat> eventCommentStatStream = eventCommentTimedStream.keyBy(EventComment::getEventName)
                .process(new KeyedProcessFunction<String, EventComment, EventCommentStat>() {
                    MapState<String, EventCommentStatic> mapState = null;
                    Collector<EventCommentStat> out = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        MapStateDescriptor<String, EventCommentStatic> descriptor = new MapStateDescriptor<String, EventCommentStatic>("mapState", String.class, EventCommentStatic.class);
                        mapState = getRuntimeContext().getMapState(descriptor);
                    }

                    @Override
                    public void processElement(EventComment eventComment, Context context, Collector<EventCommentStat> collector) throws Exception {
                        if(out==null){
                            out = collector;
                        }
                        EventCommentStatic eventCommentStatic = mapState.get(eventComment.getEventName());
                        if(eventCommentStatic!=null){
                            if("m".equals(eventComment.getCommenterGender())){
                                eventCommentStatic.setManCommentCount(eventCommentStatic.getManCommentCount()+1);
                            }else{
                                eventCommentStatic.setWomanCommentCount(eventCommentStatic.getWomanCommentCount()+1);
                            }
                            if("1".equals(eventComment.getCommentEmotion())){
                                eventCommentStatic.setPosCommentCount(eventCommentStatic.getPosCommentCount()+1);
                            }else {
                                eventCommentStatic.setNegCommentCount(eventCommentStatic.getNegCommentCount()+1);
                            }
                            eventCommentStatic.setAllCount(eventCommentStatic.getAllCount()+1);
                            eventCommentStatic.setNewestTime(
                                    eventComment.getSpiderTime().compareTo(eventCommentStatic.getNewestTime())>0?eventComment.getSpiderTime():eventCommentStatic.getNewestTime()
                            );
                        }else{
                            eventCommentStatic = new EventCommentStatic();
                            eventCommentStatic.setEventName(eventComment.getEventName());
                            if("m".equals(eventComment.getCommenterGender())){
                                eventCommentStatic.setManCommentCount(1L);
                                eventCommentStatic.setWomanCommentCount(0L);
                            }else{
                                eventCommentStatic.setManCommentCount(0L);
                                eventCommentStatic.setWomanCommentCount(1L);
                            }
                            if("1".equals(eventComment.getCommentEmotion())){
                                eventCommentStatic.setPosCommentCount(1L);
                                eventCommentStatic.setNegCommentCount(0L);
                            }else {
                                eventCommentStatic.setPosCommentCount(0L);
                                eventCommentStatic.setNegCommentCount(1L);
                            }
                            eventCommentStatic.setAllCount(1L);
                            eventCommentStatic.setNewestTime(eventComment.getSpiderTime());
                        }
                        System.out.println(eventCommentStatic);
                        mapState.put(eventComment.getEventName(), eventCommentStatic);
                    }

                    @Override
                    public void close() throws Exception {
                        for (String eventName : mapState.keys()) {
                            EventCommentStatic staticOfEvent = mapState.get(eventName);
                            EventCommentStat stat = new EventCommentStat();
                            stat.setEventName(staticOfEvent.getEventName());
                            stat.setManCommentProportion((double) staticOfEvent.getManCommentCount() / staticOfEvent.getAllCount());
                            stat.setWomanCommentProportion((double) staticOfEvent.getWomanCommentCount() / staticOfEvent.getAllCount());
                            stat.setPosCommentProportion((double) staticOfEvent.getPosCommentCount() / staticOfEvent.getAllCount());
                            stat.setNegCommentProportion((double) staticOfEvent.getNegCommentCount() / staticOfEvent.getAllCount());
                            System.out.println(stat);
                            out.collect(stat);
                        }
                    }
                });

 */

