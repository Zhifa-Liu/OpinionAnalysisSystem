package cn.edu.neu.cons;

import cn.edu.neu.tool.ClickHouseUtil;

/**
 * @author 32098
 * desc ClickHouse Constant
 */
public class ClickHouseConstant {
    public static final String USERNAME = "default";
    public static final String PASSWORD = "root";
    public static final String URL = "jdbc:clickhouse://master:8123/poas";
    public static final String DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";

    public static final String CREATE_DATABASE_POAS = "CREATE DATABASE IF NOT EXISTS poas";

    /**
     * 系统用户表
     */
    public static final String CREATE_TABLE_USER = "" +
            "CREATE TABLE IF NOT EXISTS poas.user(\n" +
            "    username String,\n" +
            "    password String,\n" +
            "    user_type String\n" +
            ") ENGINE = MergeTree\n" +
            "ORDER BY username";

    /**
     * 管理员配置表
     */
    public static final String CREATE_TABLE_TOPN = "" +
            "CREATE TABLE IF NOT EXISTS poas.topn(\n" +
            "    article_rank UInt8,\n" +
            "    article_hot_rank UInt8,\n" +
            "    event_comment_rank UInt8,\n" +
            "    modify_time DateTime\n" +
            ") ENGINE = MergeTree\n" +
            "ORDER BY modify_time";
    public static final String CREATE_TABLE_THRESHOLD = "" +
            "CREATE TABLE IF NOT EXISTS poas.threshold(\n" +
            "    neg_comment_proportion_threshold Float64,\n" +
            "    modify_time DateTime\n" +
            ") ENGINE = MergeTree\n" +
            "ORDER BY modify_time";
    public static final String CREATE_TABLE_REPTILE_PARAM = "" +
            "CREATE TABLE IF NOT EXISTS poas.reptile_param(\n" +
            "    reptile_gap Int16,\n" +
            "    gap_time_unit String,\n" +
            "    modify_time DateTime\n" +
            ") ENGINE = MergeTree\n" +
            "ORDER BY modify_time";

    /**
     * 用于舆情分析的表
     */
    public static final String CREATE_TABLE_EVENT_ARTICLE = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_article(\n" +
            "    event_name String,\n" +
            "    article_id String,\n" +
            "    article_author String,\n" +
            "    author_id String,\n" +
            "    author_gender String,\n" +
            "    author_type String,\n" +
            "    article_time DateTime,\n" +
            "    article_text String,\n" +
            "    attitudes_count Int32,\n" +
            "    comments_count Int32,\n" +
            "    reports_count Int32,\n" +
            "    article_hot Float64,\n" +
            "    article_region String,\n" +
            "    spider_time DateTime\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name\n" +
            "PARTITION BY toDate(spider_time)";
    public static final String CREATE_TABLE_EVENT_HOUR = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_hour(\n" +
            "    event_name String,\n" +
            "    event_type String,\n" +
            "    event_hot Float64,\n" +
            "    start_time String,\n" +
            "    end_time String\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name";
    public static final String CREATE_TABLE_EVENT_DAY = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_day(\n" +
            "    event_name String,\n" +
            "    event_type String,\n" +
            "    event_hot Float64,\n" +
            "    start_time String,\n" +
            "    end_time String\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name";
    public static final String CREATE_TABLE_EVENT_TREND = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_trend(\n" +
            "    event_name String,\n" +
            "    event_info_count Int64,\n" +
            "    event_attention_count Int64,\n" +
            "    time DateTime,\n" +
            "    statTimeStamp Int64" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name";
//    public static final String CREATE_TABLE_EVENT_TREND_HOUR = "" +
//            "CREATE TABLE IF NOT EXISTS poas.event_trend_hour(\n" +
//            "    event_name String,\n" +
//            "    event_info_count Int64,\n" +
//            "    event_attention_count Int64,\n" +
//            "    start_time String,\n" +
//            "    end_time String\n" +
//            ") ENGINE=MergeTree()\n" +
//            "ORDER BY event_name";
//    public static final String CREATE_TABLE_EVENT_TREND_DAY = "" +
//            "CREATE TABLE IF NOT EXISTS poas.event_trend_day(\n" +
//            "    event_name String,\n" +
//            "    event_info_count Int64,\n" +
//            "    event_attention_count Int64,\n" +
//            "    start_time String,\n" +
//            "    end_time String\n" +
//            ") ENGINE=MergeTree()\n" +
//            "ORDER BY event_name";
    public static final String CREATE_TABLE_EVENT_SPREAD = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_spread(\n" +
            "    event_name String,\n" +
            "    spread_people String,\n" +
            "    spread_people_type String,\n" +
            "    spread_count Int64\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name";
    public static final String CREATE_TABLE_EVENT_KEYWORD = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_keyword(\n" +
            "    event_name String,\n" +
            "    event_keyword Array(String),\n" +
            "    keyword_count Array(Int32),\n" +
            "    time DateTime\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name\n" +
            "PARTITION BY toDate(time)";

    public static final String CREATE_TABLE_EVENT_COMMENT_DETAIL = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_comment_detail(\n" +
            "    event_name String,\n" +
            "    comment_id String,\n" +
            "    comment_text String,\n" +
            "    comment_reply Int32,\n" +
            "    comment_like Int32,\n" +
            "    comment_emotion Int8,\n" +
            "    time DateTime\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name\n" +
            "PARTITION BY toDate(time)";
    public static final String CREATE_TABLE_EVENT_COMMENT_STAT = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_comment_stat(\n" +
            "    event_name String,\n" +
            "    man_comment_proportion Float64,\n" +
            "    woman_comment_proportion Float64,\n" +
            "    pos_comment_proportion Float64,\n" +
            "    neg_comment_proportion Float64,\n" +
            "    time DateTime\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name\n" +
            "PARTITION BY toDate(time)";
    public static final String CREATE_TABLE_EVENT_COMMENT_KEYWORD = "" +
            "CREATE TABLE IF NOT EXISTS poas.event_comment_keyword(\n" +
            "    event_name String,\n" +
            "    comment_keyword Array(String),\n" +
            "    keyword_count Array(Int32),\n" +
            "    time DateTime\n" +
            ") ENGINE=MergeTree()\n" +
            "ORDER BY event_name\n" +
            "PARTITION BY toDate(time)";
    public static final String CREATE_TABLE_LOGS = "" +
            "CREATE TABLE IF NOT EXISTS poas.logs(\n" +
            "    type String,\n" +
            "    operator String,\n" +
            "    times String\n"+
            ") ENGINE=MergeTree()\n" +
            "ORDER BY operator\n" +
            "PARTITION BY toDateTime(times)";

    public static void main(String[] args) {
//        ClickHouseUtil.executeSql(CREATE_DATABASE_POAS);
//
//        ClickHouseUtil.executeSql(CREATE_TABLE_USER);
//        ClickHouseUtil.executeSql(CREATE_TABLE_TOPN);
//        ClickHouseUtil.executeSql(CREATE_TABLE_THRESHOLD);
//        ClickHouseUtil.executeSql(CREATE_TABLE_REPTILE_PARAM);

        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_ARTICLE);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_HOUR);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_DAY);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_TREND);
//        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_TREND_HOUR);
//        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_TREND_DAY);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_SPREAD);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_KEYWORD);

        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_COMMENT_DETAIL);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_COMMENT_STAT);
        ClickHouseUtil.executeSql(CREATE_TABLE_EVENT_COMMENT_KEYWORD);
        ClickHouseUtil.executeSql(CREATE_TABLE_LOGS);

//        /*
//         * 插入系统用户
//         */
//        ClickHouseUtil.executeSql("INSERT INTO poas.user VALUES ('root', 'root', 'admin')");
//        ClickHouseUtil.executeSql("INSERT INTO poas.user VALUES ('lzf', 'lzf', 'analysis')");
//        ClickHouseUtil.executeSql("INSERT INTO poas.user VALUES ('lcx', 'lcx', 'analysis')");

    }
}


