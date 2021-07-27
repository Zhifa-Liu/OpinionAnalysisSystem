package cn.edu.neu.task.analysis;

import cn.edu.neu.bean.Event;
import cn.edu.neu.bean.EventArticle;
import cn.edu.neu.bean.EventSpread;
import cn.edu.neu.bean.EventTrend;
import cn.edu.neu.cons.ClickHouseConstant;
import cn.edu.neu.cons.ElasticSearchConstant;
import cn.edu.neu.cons.KafkaConstant;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
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
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkBase;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch.util.RetryRejectedExecutionFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 32098
 *
 *
 */
public class EventArticleFlink {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventSpreadPeople{
        private String eventName;
        private String spreadPeople;
        private String spreadPeopleType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventArticleCollection {
        private String eventName;
        private List<String> eventArticleIds;
        private List<String> eventArticleTimes;
        private List<Integer> eventArticleAttentions;
    }

    public static void main(String[] args) throws Exception {
        Properties pros = new Properties();
        pros.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        pros.setProperty("group.id", "groupA");
        pros.setProperty("auto.offset.reset", "latest");
        pros.setProperty("enable.auto.commit", "true");
        pros.setProperty("auto.commit.interval.ms", "2000");

        int batchSize = 500;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(
                KafkaConstant.EVENT_ARTICLE,
                new SimpleStringSchema(),
                pros
        );
        kafkaSource.setStartFromEarliest();
//        Map<KafkaTopicPartition, Long> map = new HashMap<>();
//        map.put(new KafkaTopicPartition(KafkaConstant.EVENT_ARTICLE, 0), 0L);
//        kafkaSource.setStartFromSpecificOffsets(map);
        // 重新设置从 kafka 记录的 group.id 的位置开始消费，如果没有记录则从 auto.offset.reset 配置开始消费
        // kafkaSource.setStartFromGroupOffsets();

        // 1. env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

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
        SingleOutputStreamOperator<EventArticle> eventArticleStream = kafkaDataStream.map(new MapFunction<String, EventArticle>() {
            @Override
            public EventArticle map(String s) throws Exception {
                return JSON.parseObject(s, EventArticle.class);
            }
        });

        /*
         * Code
         */
        // write to event_article
        eventArticleStream.addSink(
            JdbcSink.sink(
                    "INSERT INTO event_article VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new JdbcStatementBuilder<EventArticle>() {
                        @Override
                        public void accept(PreparedStatement preparedStatement, EventArticle eventArticle) throws SQLException {
                            preparedStatement.setString(1, eventArticle.getEventName());
                            preparedStatement.setString(2, eventArticle.getArticleId());
                            preparedStatement.setString(3, eventArticle.getArticleAuthor());
                            preparedStatement.setString(4, eventArticle.getAuthorId());
                            preparedStatement.setString(5, eventArticle.getAuthorGender());
                            preparedStatement.setString(6, eventArticle.getAuthorType());
                            preparedStatement.setString(7, eventArticle.getArticleTime());
                            preparedStatement.setString(8, eventArticle.getArticleText());
                            preparedStatement.setInt(9, eventArticle.getAttitudesCount());
                            preparedStatement.setInt(10, eventArticle.getCommentsCount());
                            preparedStatement.setInt(11, eventArticle.getReportsCount());
                            preparedStatement.setDouble(12, eventArticle.getArticleHot());
                            preparedStatement.setString(13, eventArticle.getArticleRegion());
                            preparedStatement.setString(14, eventArticle.getSpiderTime());
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
        // eventArticleStream.print("event article");

        // write to event_spread
        SingleOutputStreamOperator<EventSpread> eventSpreadStream = eventArticleStream
                .map(
                        new MapFunction<EventArticle, Tuple2<EventSpreadPeople, Long>>() {
                            @Override
                            public Tuple2<EventSpreadPeople, Long> map(EventArticle eventArticle) throws Exception {
                                /*
                                 * (eventName, ArticleAuthor, AuthorType, reportsCount)
                                 */
                                return Tuple2.of(new EventSpreadPeople(eventArticle.getEventName(), eventArticle.getArticleAuthor(), eventArticle.getAuthorType()), Long.valueOf(eventArticle.getReportsCount()));
                            }
                        }
                )
                .keyBy(e -> e.f0)
                .sum(1)
                .map(
                        new MapFunction<Tuple2<EventSpreadPeople, Long>, EventSpread>() {
                            @Override
                            public EventSpread map(Tuple2<EventSpreadPeople, Long> in) throws Exception {
                                return new EventSpread(in.f0.eventName, in.f0.spreadPeople, in.f0.spreadPeopleType, in.f1);
                            }
                        }
                );
        eventSpreadStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_spread VALUES (?, ?, ?, ?)",
                        new JdbcStatementBuilder<EventSpread>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, EventSpread eventSpread) throws SQLException {
                                preparedStatement.setString(1, eventSpread.getEventName());
                                preparedStatement.setString(2, eventSpread.getSpreadPeople());
                                preparedStatement.setString(3, eventSpread.getSpreadPeopleType());
                                preparedStatement.setLong(4, eventSpread.getSpreadCount());
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
        // eventSpreadStream.print("event spread");

        /*
         * write to event_trend
         */
        SingleOutputStreamOperator<EventTrend> eventTrendStream = eventArticleStream
                .keyBy(EventArticle::getEventName)
                .window(TumblingProcessingTimeWindows.of(Time.of(Long.MAX_VALUE, TimeUnit.DAYS)))
                .process(
                        new ProcessWindowFunction<EventArticle, EventTrend, String, TimeWindow>() {
                            MapState<String, EventArticleCollection> mapState = null;

                            @Override
                            public void open(Configuration parameters) {
                                MapStateDescriptor<String, EventArticleCollection> descriptor = new MapStateDescriptor<String, EventArticleCollection>("mapState", String.class, EventArticleCollection.class);
                                mapState = getRuntimeContext().getMapState(descriptor);
                            }

                            @Override
                            public void process(String s, Context context, Iterable<EventArticle> iterable, Collector<EventTrend> collector) throws Exception {
                                EventArticleCollection eventArticleCollection = mapState.get(s);
                                for (EventArticle eventArticle : iterable) {
                                    if(eventArticleCollection !=null){
                                        if(!eventArticleCollection.getEventArticleIds().contains(eventArticle.getArticleId())){
//                                            List<String> eventArticleIds = eventArticleCollection.getEventArticleIds();
//                                            eventArticleIds.add(eventArticle.getArticleId());
//                                            eventArticleCollection.setEventArticleIds(eventArticleIds);
                                            eventArticleCollection.getEventArticleIds().add(eventArticle.getArticleId());

//                                            List<String> eventArticleTimes = eventArticleCollection.getEventArticleTimes();
//                                            eventArticleTimes.add(eventArticle.getArticleTime());
//                                            eventArticleCollection.setEventArticleTimes(eventArticleTimes);
                                            eventArticleCollection.getEventArticleTimes().add(eventArticle.getArticleTime());

//                                            List<Integer> eventArticleAttentions= eventArticleCollection.getEventArticleAttentions();
//                                            eventArticleAttentions.add(eventArticle.getAttitudesCount());
//                                            eventArticleCollection.setEventArticleAttentions(eventArticleAttentions);
                                            eventArticleCollection.getEventArticleAttentions().add(eventArticle.getAttitudesCount());
                                        }else{
                                            int index = eventArticleCollection.getEventArticleIds().indexOf(eventArticle.getArticleId());
                                            if(eventArticleCollection.getEventArticleAttentions().get(index)<eventArticle.getAttitudesCount()){
//                                                List<Integer> eventArticleAttentions= eventArticleCollection.getEventArticleAttentions();
//                                                eventArticleAttentions.set(index, eventArticle.getAttitudesCount());
//                                                eventArticleCollection.setEventArticleAttentions(eventArticleAttentions);
                                                eventArticleCollection.getEventArticleAttentions().add(eventArticle.getAttitudesCount());
                                            }
                                        }
                                    }else{
                                        eventArticleCollection = new EventArticleCollection();
                                        eventArticleCollection.setEventName(s);
                                        List<String> eventArticleIds = new ArrayList<>();
                                        eventArticleIds.add(eventArticle.getArticleId());
                                        eventArticleCollection.setEventArticleIds(eventArticleIds);

                                        List<String> eventArticleTimes = new ArrayList<>();
                                        eventArticleTimes.add(eventArticle.getArticleTime());
                                        eventArticleCollection.setEventArticleTimes(eventArticleTimes);

                                        List<Integer> eventArticleAttentions= new ArrayList<>();
                                        eventArticleAttentions.add(eventArticle.getAttitudesCount());
                                        eventArticleCollection.setEventArticleAttentions(eventArticleAttentions);
                                    }
                                }
                                mapState.put(s, eventArticleCollection);

                                List<String> eventArticleTimes = eventArticleCollection.getEventArticleTimes();
                                List<String> eventArticleTimesCopy1 = new ArrayList<>(eventArticleTimes);
                                Collections.sort(eventArticleTimesCopy1);
                                for(int i=0; i<eventArticleTimesCopy1.size(); i++){
                                    List<String> eventArticleTimesCopy2 = new ArrayList<>(eventArticleTimes);
                                    int index = eventArticleTimesCopy2.indexOf(eventArticleTimesCopy1.get(i));
                                    eventArticleTimesCopy2.set(index, "");
                                    EventTrend eventTrend = new EventTrend();
                                    eventTrend.setEventName(s);
                                    eventTrend.setTime(eventArticleTimesCopy1.get(i));
                                    eventTrend.setEventInfoCount((long) (i+1));
                                    eventTrend.setEventAttentionCount(Long.valueOf(eventArticleCollection.getEventArticleAttentions().get(index)));
                                    for(int j=0; j<i; j++){
                                        int ind = eventArticleTimesCopy2.indexOf(eventArticleTimesCopy1.get(j));
                                        if(ind==-1){
                                            System.exit(-1);
                                        }
                                        eventArticleTimesCopy2.set(ind, "");
                                        int attentionCount = eventArticleCollection.getEventArticleAttentions().get(ind);
                                        eventTrend.setEventAttentionCount(eventTrend.getEventAttentionCount()+attentionCount);
                                    }
                                    eventTrend.setStatTimeStamp(System.currentTimeMillis());
                                    collector.collect(eventTrend);
                                }
                            }
                        }
                );
        // eventTrendStream.print("event trend");
        eventTrendStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_trend VALUES (?, ?, ?, ?, ?)",
                        new JdbcStatementBuilder<EventTrend>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, EventTrend eventTrend) throws SQLException {
                                preparedStatement.setString(1, eventTrend.getEventName());
                                preparedStatement.setLong(2, eventTrend.getEventInfoCount());
                                preparedStatement.setLong(3, eventTrend.getEventAttentionCount());
                                preparedStatement.setString(4, eventTrend.getTime());
                                preparedStatement.setLong(5, eventTrend.getStatTimeStamp());
                            }
                        },
                        new JdbcExecutionOptions.Builder().withBatchSize(batchSize).withBatchIntervalMs(5000).build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName(ClickHouseConstant.DRIVER)
                                .withUrl(ClickHouseConstant.URL)
                                .withUsername(ClickHouseConstant.USERNAME)
                                .withPassword(ClickHouseConstant.PASSWORD)
                                .build()
                )
        );

        /*
         * write to event
         */
        SingleOutputStreamOperator<EventArticle> timedEventArticleStreamB = eventArticleStream.map(
                        new MapFunction<EventArticle, Tuple2<EventArticle, Long>>() {
                            @Override
                            public Tuple2<EventArticle, Long> map(EventArticle eventArticle) throws Exception {
                                return Tuple2.of(eventArticle, format.parse(eventArticle.getSpiderTime()).getTime());
                            }
                        })
                        .assignTimestampsAndWatermarks(
                                WatermarkStrategy.<Tuple2<EventArticle, Long>>forBoundedOutOfOrderness(Duration.ofMinutes(10)).withTimestampAssigner(
                                        (element, ts) -> element.f1
                                ))
                        .map(ele->ele.f0);
        SingleOutputStreamOperator<Event> eventHourStream = timedEventArticleStreamB
                .keyBy(EventArticle::getField)
                .keyBy(EventArticle::getEventName)
                .window(TumblingEventTimeWindows.of(Time.hours(1)))
                .aggregate(
                        new AggregateFunction<EventArticle, Tuple4<String, Double, Long, String>, Event>() {
                            @Override
                            public Tuple4<String, Double, Long, String> createAccumulator() {
                                return Tuple4.of("", 0D, 0L, "");
                            }

                            @SneakyThrows
                            @Override
                            public Tuple4<String, Double, Long, String> add(EventArticle eventArticle, Tuple4<String, Double, Long, String> acc) {
                                String eventName = eventArticle.getEventName();
                                long articleTime = format.parse(eventArticle.getArticleTime()).getTime();
                                long spiderTime = format.parse(eventArticle.getSpiderTime()).getTime();
                                int hours = (int) ((spiderTime - articleTime) / (1000 * 60 * 60));
                                double weightSum = eventArticle.getAttitudesCount() * 0.5 + eventArticle.getCommentsCount() * 0.3 + eventArticle.getReportsCount() * 0.2;
                                double singleHot = (weightSum + 1) / Math.pow(hours + 2, 2);
                                String eventType = eventArticle.getField();
                                return Tuple4.of(eventName, acc.f1 + singleHot, acc.f2 + 1, eventType);
                            }

                            @Override
                            public Event getResult(Tuple4<String, Double, Long, String> accFinal) {
                                Event event = new Event();
                                event.setEventName(accFinal.f0);
                                event.setEventHot(new BigDecimal(accFinal.f1 / accFinal.f2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                                event.setEventType(accFinal.f3);
                                return event;
                            }

                            @Override
                            public Tuple4<String, Double, Long, String> merge(Tuple4<String, Double, Long, String> acc1, Tuple4<String, Double, Long, String> acc2) {
                                return Tuple4.of(acc1.f0, acc1.f1+acc2.f1, acc1.f2+acc2.f2, acc1.f3);
                            }
                        },
                        new WindowFunction<Event, Event, String, TimeWindow>() {
                            @Override
                            public void apply(String s, TimeWindow timeWindow, Iterable<Event> iterable, Collector<Event> collector) throws Exception {
                                Event event = iterable.iterator().next();
                                event.setStartTime(format.format(timeWindow.getStart()));
                                event.setEndTime(format.format(timeWindow.getEnd()));
                                collector.collect(event);
                            }
                        }
                );
        SingleOutputStreamOperator<Event> eventDayStream = timedEventArticleStreamB
                .keyBy(EventArticle::getField)
                .keyBy(EventArticle::getEventName)
                .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
                .aggregate(
                        new AggregateFunction<EventArticle, Tuple4<String, Double, Long, String>, Event>() {
                            @Override
                            public Tuple4<String, Double, Long, String> createAccumulator() {
                                /*
                                 * 累加器：（事件名，事件文章总热度，事件文章计数，事件类型
                                 */
                                return Tuple4.of("", 0D, 0L, "");
                            }

                            @SneakyThrows
                            @Override
                            public Tuple4<String, Double, Long, String> add(EventArticle eventArticle, Tuple4<String, Double, Long, String> acc) {
                                String eventName = eventArticle.getEventName();
                                long articleTime = format.parse(eventArticle.getArticleTime()).getTime();
                                long spiderTime = format.parse(eventArticle.getSpiderTime()).getTime();
                                int hours = (int) ((spiderTime - articleTime) / (1000 * 60 * 60));
                                double weightSum = eventArticle.getAttitudesCount() * 0.5 + eventArticle.getAttitudesCount() * 0.3 + eventArticle.getReportsCount() * 0.2;
                                double articleHot = (weightSum + 1) / Math.pow(hours + 2, 2);
                                String eventType = eventArticle.getField();
                                return Tuple4.of(eventName, acc.f1 + articleHot, acc.f2 + 1, eventType);
                            }

                            @Override
                            public Event getResult(Tuple4<String, Double, Long, String> accFinal) {
                                Event event = new Event();
                                event.setEventName(accFinal.f0);
                                event.setEventHot(new BigDecimal(accFinal.f1 / accFinal.f2).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());
                                event.setEventType(accFinal.f3);
                                return event;
                            }

                            @Override
                            public Tuple4<String, Double, Long, String> merge(Tuple4<String, Double, Long, String> acc1, Tuple4<String, Double, Long, String> acc2) {
                                return Tuple4.of(acc1.f0, acc1.f1+acc2.f1, acc1.f2+acc2.f2, acc1.f3);
                            }
                        },
                        new WindowFunction<Event, Event, String, TimeWindow>() {
                            @Override
                            public void apply(String s, TimeWindow timeWindow, Iterable<Event> iterable, Collector<Event> collector) throws Exception {
                                Event event = iterable.iterator().next();
                                event.setStartTime(format.format(timeWindow.getStart()));
                                event.setEndTime(format.format(timeWindow.getEnd()));
                                collector.collect(event);
                            }
                        }
                );
        // eventHourStream.print("event(hour)");
        eventHourStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_hour VALUES (?, ?, ?, ?, ?)",
                        new JdbcStatementBuilder<Event>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, Event event) throws SQLException {
                                preparedStatement.setString(1, event.getEventName());
                                preparedStatement.setString(2, event.getEventType());
                                preparedStatement.setDouble(3, event.getEventHot());
                                preparedStatement.setString(4, event.getStartTime());
                                preparedStatement.setString(5, event.getEndTime());
                            }
                        },
                        new JdbcExecutionOptions.Builder().withBatchSize(batchSize).withBatchIntervalMs(5000).build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName(ClickHouseConstant.DRIVER)
                                .withUrl(ClickHouseConstant.URL)
                                .withUsername(ClickHouseConstant.USERNAME)
                                .withPassword(ClickHouseConstant.PASSWORD)
                                .build()
                )
        );
        // eventDayStream.print("event(day)");
        eventDayStream.addSink(
                JdbcSink.sink(
                        "INSERT INTO event_day VALUES (?, ?, ?, ?, ?)",
                        new JdbcStatementBuilder<Event>() {
                            @Override
                            public void accept(PreparedStatement preparedStatement, Event event) throws SQLException {
                                preparedStatement.setString(1, event.getEventName());
                                preparedStatement.setString(2, event.getEventType());
                                preparedStatement.setDouble(3, event.getEventHot());
                                preparedStatement.setString(4, event.getStartTime());
                                preparedStatement.setString(5, event.getEndTime());
                            }
                        },
                        new JdbcExecutionOptions.Builder().withBatchSize(batchSize).withBatchIntervalMs(5000).build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withDriverName(ClickHouseConstant.DRIVER)
                                .withUrl(ClickHouseConstant.URL)
                                .withUsername(ClickHouseConstant.USERNAME)
                                .withPassword(ClickHouseConstant.PASSWORD)
                                .build()
                )
        );

        /*
         * write event to es
         */
        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost(ElasticSearchConstant.ES_HOST, ElasticSearchConstant.ES_PORT));
        ElasticsearchSink.Builder<Event> esSinkBuilder = new ElasticsearchSink.Builder<Event>(
                httpHosts,
                new ElasticsearchSinkFunction<Event>() {
                    @Override
                    public void process(Event event, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("eventName", event.getEventName());
                        map.put("eventHot", String.valueOf(event.getEventHot()));
                        map.put("eventType", event.getEventType());
                        map.put("startTime", event.getStartTime());
                        map.put("endTime", event.getEndTime());

                        IndexRequest request = Requests.indexRequest()
                                .index("event")
                                .source(map);

                        requestIndexer.add(request);
                    }
                }
        );
        esSinkBuilder.setBulkFlushMaxActions(100);
        esSinkBuilder.setBulkFlushInterval(5000);
        // 设置失败重试的次数
        esSinkBuilder.setBulkFlushBackoffRetries(3);
        // 设置重试的时间间隔
        esSinkBuilder.setBulkFlushBackoffDelay(2);
        // 设置重试策略
        esSinkBuilder.setBulkFlushBackoffType(ElasticsearchSinkBase.FlushBackoffType.EXPONENTIAL);
        // 设置失败处理
        esSinkBuilder.setFailureHandler(new RetryRejectedExecutionFailureHandler());

        eventHourStream.addSink(esSinkBuilder.build());

        env.execute("event article");
    }
}

