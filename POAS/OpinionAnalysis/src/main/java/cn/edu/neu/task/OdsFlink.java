package cn.edu.neu.task;

import cn.edu.neu.bean.*;
import cn.edu.neu.cons.KafkaConstant;
import cn.edu.neu.filter.JsonFilterFunction;
import cn.edu.neu.map.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author superwasabi
 *
 * 从数据源处理数据到不同Topic
 **/
public class OdsFlink {
    public static void main(String[] args){
        // 1. env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置checkpoint的时间间隔为1000ms做一次Checkpoint
        env.enableCheckpointing(60000);
        // 设置checkpoint的执行模式为EXACTLY_ONCE(默认)
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 设置checkpoint的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(120000);
        // 设置两个checkpoint之间最少等待时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(60000);
        // 设置同一时间有多少个checkpoint可以同时执行
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 设置是否清理检查点
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        // 2. source
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        // 消费者属性之组id
        prop.setProperty("group.id", "default");
        // 消费者属性之offset重置规则
        prop.setProperty("auto.offset.reset", "latest");     // latest   earliest
        // 自动提交 offset
        prop.setProperty("enable.auto.commit", "true");
        // 自动提交间隔
        prop.setProperty("auto.commit.interval.ms", "2000");

        FlinkKafkaConsumer<String> odsConsumerA = new FlinkKafkaConsumer<String>(KafkaConstant.WEIBO_ODS, new SimpleStringSchema(), prop);
        odsConsumerA.setStartFromEarliest();
        // 重新设置从 kafka 记录的 group.id 的位置开始消费，如果没有记录则从 auto.offset.reset 配置开始消费
        // odsConsumerA.setStartFromGroupOffsets();

        FlinkKafkaConsumer<String> odsConsumerB = new FlinkKafkaConsumer<String>(KafkaConstant.TOUTIAO_ODS, new SimpleStringSchema(), prop);
        odsConsumerA.setStartFromEarliest();
        // 重新设置从 kafka 记录的 group.id 的位置开始消费，如果没有记录则从 auto.offset.reset 配置开始消费
        // odsConsumerB.setStartFromGroupOffsets();

        SingleOutputStreamOperator<String> ods1 = env.addSource(odsConsumerA).filter(new JsonFilterFunction());
        SingleOutputStreamOperator<String> ods2 = env.addSource(odsConsumerB).filter(new JsonFilterFunction());

        DataStream<String> ods = ods1.union(ods2);

        // ods.print();

        // 3. transform\4. sink
        SingleOutputStreamOperator<Article> odsArticleStream = ods.map(new ArticleMapFunction()).filter(Objects::nonNull);
        // odsArticleStream.print("article");
        /*
         * 文章(微博博文)原始数据————事件文章数据
         *
         */
        SingleOutputStreamOperator<String> eventArticleStream = odsArticleStream
                .flatMap(new EventArticleFlatMapFunction())
                .map(new MapFunction<EventArticle, String>() {
                    @Override
                    public String map(EventArticle eventArticle) throws Exception {
                        return JSONObject.toJSONString(eventArticle);
                    }
                });
        Properties propsWeiboDetail = new Properties();
        propsWeiboDetail.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        FlinkKafkaProducer<String> sinkA = new FlinkKafkaProducer<>(KafkaConstant.EVENT_ARTICLE,  new SimpleStringSchema(),  propsWeiboDetail);
        eventArticleStream.addSink(sinkA); // ok
        // eventArticleStream.print("event_article");

        /*
         * 文章(微博博文)原始数据————事件评论数据
         */
        SingleOutputStreamOperator<String> eventCommentStream = odsArticleStream.flatMap(new EventCommentFlatMapFunction()).map(
                new MapFunction<EventComment, String>() {
                    @Override
                    public String map(EventComment eventComment) throws Exception {
                        return JSONObject.toJSONString(eventComment);
                    }
                }
        );
        Properties propsWeiboComment = new Properties();
        propsWeiboComment.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        FlinkKafkaProducer<String> sinkB = new FlinkKafkaProducer<>(KafkaConstant.EVENT_COMMENT,  new SimpleStringSchema(),  propsWeiboComment);
        eventCommentStream.addSink(sinkB); // ok
        // eventCommentStream.print("event_comment");

        /*
         * 文章(微博博文)原始数据————事件关键词数据
         *
         */
        SingleOutputStreamOperator<String> eventKeywordStream = odsArticleStream.flatMap(new EventKeywordFlatMapFunction()).map(
                new MapFunction<EventKeyword, String>() {
                    @Override
                    public String map(EventKeyword eventKeyword) throws Exception {
                        return JSONObject.toJSONString(eventKeyword);
                    }
                }
        );
        Properties propsWeiboKeywords = new Properties();
        propsWeiboKeywords.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        FlinkKafkaProducer<String> sinkC = new FlinkKafkaProducer<>(KafkaConstant.EVENT_KEYWORDS,  new SimpleStringSchema(),  propsWeiboKeywords);
        eventKeywordStream.addSink(sinkC); // ok
        // eventKeywordStream.print("event_keyword");

        /*
         * 文章(微博博文)原始数据————事件评论关键词数据
         *
         */
        SingleOutputStreamOperator<String> commentKeywordStream = odsArticleStream.flatMap(new EventCommentKeyWordFlatMapFunction()).map(
                new MapFunction<EventCommentKeyword, String>() {
                    @Override
                    public String map(EventCommentKeyword eventCommentKeyword) throws Exception {
                        return JSONObject.toJSONString(eventCommentKeyword);
                    }
                }
        );
        Properties propsCommentKeywords = new Properties();
        propsCommentKeywords.setProperty("bootstrap.servers", KafkaConstant.BROKER_LIST);
        FlinkKafkaProducer<String> sinkD = new FlinkKafkaProducer<>(KafkaConstant.EVENT_COMMENT_KEYWORDS, new SimpleStringSchema(),  propsCommentKeywords);
        commentKeywordStream.addSink(sinkD); // ok
        // commentKeywordStream.print("comment_keyword");

        // 5. execute
        try {
            env.execute("streaming" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//        Map<KafkaTopicPartition, Long> map = new HashMap<>();
//        map.put(new KafkaTopicPartition(KafkaConstant.WEIBO_ODS, 0), 0L);
//        odsConsumerA.setStartFromSpecificOffsets(map);

