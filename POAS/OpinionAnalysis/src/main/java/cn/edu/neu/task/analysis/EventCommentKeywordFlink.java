package cn.edu.neu.task.analysis;

import cn.edu.neu.bean.EventCommentKeyword;
import cn.edu.neu.cons.ClickHouseConstant;
import cn.edu.neu.cons.KafkaConstant;
import cn.edu.neu.tool.ClickHouseUtil;
import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 32098
 */
public class EventCommentKeywordFlink {
    public static void main(String[] args) throws Exception {
        Properties pros = new Properties();
        pros.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        pros.setProperty("group.id", "groupC");
        pros.setProperty("auto.offset.reset", "latest");
        pros.setProperty("enable.auto.commit", "true");
        pros.setProperty("auto.commit.interval.ms", "2000");

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<String>(
                KafkaConstant.EVENT_COMMENT_KEYWORDS,
                new SimpleStringSchema(),
                pros
        );
        kafkaSource.setStartFromEarliest();
        // 重新设置从 kafka 记录的 group.id 的位置开始消费，如果没有记录则从 auto.offset.reset 配置开始消费
        // kafkaSource.setStartFromGroupOffsets();

        int batchSize = 500;

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
        SingleOutputStreamOperator<EventCommentKeyword> commentKeywordStream = kafkaDataStream.map(new MapFunction<String, EventCommentKeyword>() {
            @Override
            public EventCommentKeyword map(String s) throws Exception {
                return JSON.parseObject(s, EventCommentKeyword.class);
            }
        });

        /*
         * Code
         */

        // write to event_comment_keyword
        SingleOutputStreamOperator<EventCommentKeyword> newCommentKeywordStream = commentKeywordStream.keyBy(EventCommentKeyword::getEventName).reduce(
                new ReduceFunction<EventCommentKeyword>() {
                    @Override
                    public EventCommentKeyword reduce(EventCommentKeyword commentKeywordA, EventCommentKeyword commentKeywordB) throws Exception {
                        // System.out.println("A: " + commentKeywordA.getCommentIds().size()+ " B: " + commentKeywordB.getCommentIds().size());
                        if(commentKeywordA.getCommentIds().contains(commentKeywordB.getCommentIds().get(0))){
                            return null;
                        }else{
                            EventCommentKeyword eventCommentKeyword = new EventCommentKeyword();
                            List<String> keywordA = commentKeywordA.getKeyword();
                            List<Integer> wordCountA = commentKeywordA.getWordCount();
                            List<String> keywordB = commentKeywordB.getKeyword();
                            List<Integer> wordCountB = commentKeywordB.getWordCount();
                            List<String> keyword = new ArrayList<>();
                            List<Integer> wordCount = new ArrayList<>();

                            for (String s : keywordB) {
                                if (keywordA.contains(s)) {
                                    keyword.add(s);
                                    wordCount.add(wordCountA.get(keywordA.indexOf(s)) + wordCountB.get(keywordB.indexOf(s)));
                                } else {
                                    keyword.add(s);
                                    wordCount.add(wordCountB.get(keywordB.indexOf(s)));
                                }
                            }
                            for (String s : keywordA) {
                                if (!keyword.contains(s)) {
                                    keyword.add(s);
                                    wordCount.add(wordCountA.get(keywordA.indexOf(s)));
                                }
                            }

                            eventCommentKeyword.setEventName(commentKeywordA.getEventName());
                            List<String> commentIds = new ArrayList<>();
                            commentIds.addAll(commentKeywordA.getCommentIds());
                            commentIds.addAll(commentKeywordB.getCommentIds());
                            eventCommentKeyword.setCommentIds(commentIds);
                            eventCommentKeyword.setKeyword(keyword);
                            eventCommentKeyword.setWordCount(wordCount);
                            eventCommentKeyword.setTime(commentKeywordA.getTime().compareTo(commentKeywordB.getTime()) > 0 ? commentKeywordA.getTime() : commentKeywordB.getTime());
                            return eventCommentKeyword;
                        }
                    }
                }
        ).filter(
                new FilterFunction<EventCommentKeyword>() {
                    @Override
                    public boolean filter(EventCommentKeyword eventCommentKeyword) throws Exception {
                        return eventCommentKeyword != null;
                    }
                }
        );

        String insertIntoEventKeyword = "INSERT INTO event_comment_keyword VALUES (?,?,?,?)";
        newCommentKeywordStream.addSink(
                JdbcSink.sink(
                        insertIntoEventKeyword,
                        new JdbcStatementBuilder<EventCommentKeyword>(){
                            @Override
                            public void accept(PreparedStatement preparedStatement, EventCommentKeyword commentKeyword) throws SQLException {
                                preparedStatement.setString(1, commentKeyword.getEventName());
                                Array keywordArr = ClickHouseUtil.getConnection().createArrayOf("String", commentKeyword.getKeyword().toArray(new String[0]));
                                preparedStatement.setArray(2, keywordArr);
                                Array wordCountArr = ClickHouseUtil.getConnection().createArrayOf("Int32", commentKeyword.getWordCount().toArray(new Integer[0]));
                                preparedStatement.setArray(3, wordCountArr);
                                preparedStatement.setString(4, commentKeyword.getTime());
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
        // newCommentKeywordStream.print("event comment keyword");

        env.execute("event comment keyword");
    }
}
