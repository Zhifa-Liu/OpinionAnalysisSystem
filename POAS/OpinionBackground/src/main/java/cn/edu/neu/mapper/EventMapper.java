package cn.edu.neu.mapper;

import cn.edu.neu.po.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 32098
 */
@Repository
public interface EventMapper {
    /**
     * 获取所有事件
     *
     * @return Events
     *
     *
     */
    @Select("select\n" +
            "       event_name,\n" +
            "       event_type,\n" +
            "       groupArray(event_hot)[-1] as eventHot\n" +
            "from (select *\n" +
            "from event_day\n" +
            "where dateDiff('day',toDateTime(start_time),now())<30)\n" +
            "group by event_name,event_type\n" +
            "order by eventHot DESC\n" +
            "limit 20")
    List<Event> getAllEvent();

    /**
     * 获取事件热度增长率
     * 环比增长率 =（本期数 - 上期数) /上期数
     *
     * @return List<EventHotIncrease>
     */
    @Select("select\n" +
            "       event_name as name,\n" +
            "       groupArray(event_hot)[-1] as eventHot,\n" +
            "       if(arrayElement(groupArray(event_hot),\n" +
            "           length(groupArray(event_hot))-1) = 0,\n" +
            "           0,\n" +
            "           round((arrayElement(groupArray(event_hot), length(groupArray(event_hot))) - arrayElement(groupArray(event_hot), length(groupArray(event_hot))-1)) / arrayElement(groupArray(event_hot), length(groupArray(event_hot))-1), 2)) AS value\n" +
            "from (select *\n" +
            "                 from event_day\n" +
            "                 where dateDiff('day',toDateTime(start_time),now())<7)\n" +
            "group by event_name\n" +
            "order by value DESC\n")
    List<EventHotIncrease> getEventHotIncrease();

    /**
     * 获得趋势事件增长曲线
     * @param eventName 事件名
     * @return List<EventTrendIncrease>
     */
    @Select(
            "SELECT event_name_ as event_name,\n" +
                    "       event_info_count as event_info_count,\n" +
                    "       event_attention_count as event_attention_count,\n" +
                    "        if(neighbor(event_info_count, 1)=0,0,round((event_info_count - neighbor(event_info_count, 1))/neighbor(event_info_count, 1), 2))           as event_info_increase,\n" +
                    "        if(neighbor(event_attention_count, 1)=0,0,round((event_attention_count - neighbor(event_attention_count, 1))/neighbor(event_attention_count, 1), 2)) as event_attention_increase,\n" +
                    "        time_ as time\n" +
                    "FROM (\n" +
                    "      SELECT groupArray(1)(event_name)[1]              as event_name_,\n" +
                    "             groupArray(event_info_count)[-1]          as event_info_count,\n" +
                    "             groupArray(event_attention_count)[-1]     as event_attention_count,\n" +
                    "             formatDateTime(time, '%Y-%m-%d %H:00:00') as time_\n" +
                    "      FROM event_trend\n" +
                    "      WHERE event_name = #{eventName}\n" +
                    "        and statTimeStamp = (\n" +
                    "          SELECT statTimeStamp FROM event_trend WHERE event_name = #{eventName} ORDER BY statTimeStamp DESC LIMIT 1\n" +
                    "      )\n" +
                    "      GROUP BY formatDateTime(time, '%Y-%m-%d %H:00:00')\n" +
                    "      ORDER BY formatDateTime(time, '%Y-%m-%d %H:00:00') DESC\n" +
                    "      LIMIT 9\n" +
                    " )\n" +
                    "ORDER BY time"
    )
    List<EventTrendIncrease> getEventTrendIncrease(String eventName);

    /**
     * 获取某一类型的所有事件
     *
     * @param eventType 事件类型名
     * @return Events
     */
    @Select("select\n" +
            "       event_name,\n" +
            "       event_type,\n" +
            "       groupArray(event_hot)[-1] as eventHot\n" +
            "from (select *\n" +
            "from event_day\n" +
            "where dateDiff('day',toDateTime(start_time),now())<30 and event_type=#{eventType})\n" +
            "group by event_name,event_type\n" +
            "order by eventHot DESC\n" +
            "limit 20")
    List<Event> getEventByType(String eventType);

    /**
     * 获取不同领域热度(领域下事件的热度和): Maybe
     *
     * @return EventTypeHots
     */
    @Select("select * from (SELECT event_type, round(sum(event_hot),2) as hot, toDate(toDateTime(start_time)) AS time\n" +
            "            FROM event_hour\n" +
            "            WHERE start_time<>'1970-01-01 00:00:00'" +
            "            GROUP BY event_type, time\n" +
            "            ORDER BY time desc\n" +
            "            LIMIT 28)\n" +
            "order by time")
    List<EventTypeHot> getEventTypeHot();

    /**
     * 获取所有事件的所有文章的地域分布
     *
     * @return Map<Region, ArticleCount>
     */
    @Select("select article_region,count(DISTINCT article_id) as value\n" +
            "from event_article\n" +
            "where article_region not like '%其%' and article_region not like'%百胜（中国）投资有限公司%'\n" +
            "group by article_region")
    List<EventRegion> getAllEventArticleRegion();

    /**
     * 获取事件的关键词
     *
     * @param eventName 事件名
     * @return EventKeyword
     */
    @Select("SELECT * FROM event_keyword\n" +
            "WHERE event_name=#{eventName} AND length(event_keyword)=(\n" +
            "    SELECT max(length(event_keyword)) FROM event_keyword WHERE event_name=#{eventName}\n" +
            ")\n" +
            "LIMIT 1")
    EventKeyword getEventKeyword(String eventName);

    /**
     * 获取事件的走势
     *
     * @param eventName 事件名
     * @return EventTrends
     */
    @Select(
            "SELECT event_name_ as event_name, event_info_count_ as event_info_count, event_attention_count_ as event_attention_count, time_ as time\n" +
                    "FROM (\n" +
                    "     SELECT max(event_name)            as event_name_,\n" +
                    "            max(event_info_count)      as event_info_count_,\n" +
                    "            max(event_attention_count) as event_attention_count_,\n" +
                    "            formatDateTime(time, '%Y-%m-%d %H:00:00') as time_\n" +
                    "     FROM event_trend\n" +
                    "     WHERE event_name = #{eventName}\n" +
                    "       and statTimeStamp = (\n" +
                    "         SELECT statTimeStamp FROM event_trend WHERE event_name = #{eventName} ORDER BY statTimeStamp DESC LIMIT 1\n" +
                    "     )\n" +
                    "     GROUP BY formatDateTime(time, '%Y-%m-%d %H:00:00')\n" +
                    "     ORDER BY formatDateTime(time, '%Y-%m-%d %H:00:00') DESC\n" +
                    "     LIMIT 9\n" +
                    ")\n" +
                    "ORDER BY time"
    )
    List<EventTrend> getEventTrendHour(String eventName);

    /**
     * 获取事件的走势
     *
     * @param eventName 事件名
     * @return List<EventTrend>
     */
    @Select(
            "SELECT event_name_ as event_name, event_info_count_ as event_info_count, event_attention_count_ as event_attention_count, time_ as time\n" +
                    "FROM (\n" +
                    "     SELECT max(event_name)            as event_name_,\n" +
                    "            max(event_info_count)      as event_info_count_,\n" +
                    "            max(event_attention_count) as event_attention_count_,\n" +
                    "            toDate(time)               as time_\n" +
                    "     FROM event_trend\n" +
                    "     WHERE event_name = #{eventName}\n" +
                    "       and statTimeStamp = (\n" +
                    "         SELECT statTimeStamp FROM event_trend WHERE event_name = #{eventName} ORDER BY statTimeStamp DESC LIMIT 1\n" +
                    "     )\n" +
                    "     GROUP BY toDate(time)\n" +
                    "     ORDER BY toDate(time) DESC\n" +
                    "     LIMIT 9\n" +
                    ")\n" +
                    "ORDER BY time"
    )
    List<EventTrend> getEventTrendDay(String eventName);

    /**
     * 获取事件的传播人
     *
     * @param eventName 事件名
     * @param type type
     * @param topN topN
     * @return List<EventSpread>
     */
    @Select("SELECT event_name,spread_people,spread_people_type,max(spread_count) as spread_count\n" +
            "FROM event_spread\n" +
            "WHERE event_name=#{eventName} and spread_people_type=#{type}\n" +
            "GROUP BY event_name,spread_people,spread_people_type\n" +
            "ORDER BY spread_count DESC\n" +
            "Limit #{topN}")
    List<EventSpread> getEventSpread(String eventName, String type, int topN);
}


/*
"select event_name,sum(event_info_count) as event_info_count,sum(event_attention_count) as event_attention_count,toDate(toDateTime(start_time)) AS time\n" +
"from event_trend_day\n" +
"where event_name=#{event_name} AND start_time<>'1970-01-01 00:00:00'\n" +
"GROUP BY time,event_name\n" +
"ORDER BY time desc\n"
 */

/*
"select\n" +
"       if(neighbor(event_hot,-1)=0,0,round((event_hot-neighbor(event_hot,-1))/neighbor(event_hot,-1),2)) as rate,\n" +
"       start_time\n" +
"from (select event_hot,start_time from event_day\n" +
"where event_name=#{eventName}\n" +
"group by event_hot,start_time\n" +
"order by start_time)"
 */

