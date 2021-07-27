package cn.edu.neu.mapper;

import cn.edu.neu.po.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 32098
 *
 * 事件评论分析 Mapper
 */
@Repository
public interface EventCommentMapper {
    /**
     * 获取事件评论的点赞排名
     *
     * @param eventName eventName 事件名
     * @param topN topN
     * @return Map<ArticleId, AttitudesCount>
     *
     */
    @Select("SELECT comment_text as name, comment_like as value FROM (" +
            "   SELECT comment_text, max(comment_like) as comment_like FROM event_comment_detail WHERE event_name=#{eventName} GROUP BY comment_text" +
            ") ORDER BY comment_like desc limit #{topN}")
    List<Map<String, Integer>> getEventCommentLikeRank(String eventName, int topN);

    /**
     * 获取事件评论的回复排名
     *
     * @param eventName eventName 事件名
     * @param topN topN
     * @return Map<ArticleId, AttitudesCount>
     *     max_time(spider_time) as latest_spider_time
     */
    @Select("SELECT comment_text as name, comment_reply as value FROM (" +
            "   SELECT comment_text, max(comment_reply) as comment_reply FROM event_comment_detail WHERE event_name=#{eventName} GROUP BY comment_text" +
            ") ORDER BY comment_reply desc limit #{topN}")
    List<Map<String, Integer>> getEventCommentReplyRank(String eventName, int topN);

    /**
     * 获取事件评论的关键词
     * "SELECT * FROM event_comment_keyword WHERE event_name=#{eventName} ORDER BY time, keyword_count  limit 1"
     * @param eventName 事件名
     * @return 事件评论的关键词及对应的词频
     */
    @Select("SELECT * FROM event_comment_keyword\n" +
            "WHERE event_name=#{eventName} AND length(comment_keyword)=(\n" +
            "    SELECT max(length(comment_keyword)) FROM event_comment_keyword WHERE event_name=#{eventName}\n" +
            ")\n" +
            "LIMIT 1")
    EventCommentKeyword getEventKeyword(String eventName);

    /**
     * 获取事件评论的情感走向
     *
     * @param eventName eventName
     * @return EventCommentEmotionTrends
     */
    @Select("SELECT\n" +
            "   event_name,\n" +
            "   formatDateTime(time, '%Y-%m-%d %H:%M') as times,\n" +
            "   groupArray(1)(pos_comment_proportion)[1] as pos_comment_proportion,\n" +
            "   groupArray(1)(neg_comment_proportion)[1] as neg_comment_proportion FROM (\n" +
            "        SELECT *\n" +
            "        FROM event_comment_stat\n" +
            "        WHERE event_name=#{eventName}\n" +
            "        ORDER BY time DESC\n" +
            "   )\n" +
            "GROUP BY event_name,formatDateTime(time, '%Y-%m-%d %H:%M')\n" +
            "ORDER BY times DESC limit 7")
    List<EventCommentEmotionTrend> getEventCommentEmotionTrend(String eventName);

    /**
     * 获得评论详情
     * @param eventName
     * @param commentEmotion
     * @return
     */
    @Select(
            "select event_name,comment_text,comment_emotion,formatDateTime(min(time), '%Y-%m-%d %H:%M') as timetmp\n" +
                    "            from event_comment_detail\n" +
                    "            where event_name=#{eventName} and comment_emotion=#{commentEmotion}\n" +
                    "            group by event_name,comment_text,comment_emotion")
    List<EventComment> getEventComment(String eventName,String commentEmotion);

    /**
     * 获取事件评论的性别占比
     *
     * @param eventName eventName
     * @return EventCommentGender
     */
    @Select("SELECT event_name, man_comment_proportion, woman_comment_proportion, time FROM event_comment_stat WHERE event_name=#{eventName} ORDER BY time DESC limit 1")
    EventCommentGender getEventCommentGender(String eventName);

    /**
     *
     * @param threshold threshold
     * @return List<Map<String, String>>
     */
    @Select("select event_name as eventName,neg_comment_proportion as negCommentProportion,rate\n" +
            "from\n" +
            "(select event_name,\n" +
            "       q[-1] as neg_comment_proportion,\n" +
            "       if(length(q)=1\n" +
            "              or q[length(q)-1]=0,0\n" +
            "           ,round((q[-1]\n" +
            "               -q[length(q)-1])\n" +
            "               /q[length(q)-1],3)) as rate\n" +
            "from (select event_name,groupArray(neg_comment_proportion) as q\n" +
            "    from event_comment_stat\n" +
            "      where dateDiff('day',now(),time)<5\n" +
            "      GROUP BY event_name))\n" +
            "where neg_comment_proportion<>1 and neg_comment_proportion>#{threshold}\n" +
            "order by neg_comment_proportion desc")
    List<Map<String, String>> getWarningEvent(Double threshold);

    /**
     *
     * @param threshold threshold
     * @return List<Map<String, String>>
     */
    @Select("select event_name as eventName,neg_comment_proportion as negCommentProportion,rate\n" +
            "from\n" +
            "(select event_name,\n" +
            "       q[-1] as neg_comment_proportion,\n" +
            "       if(length(q)=1\n" +
            "              or q[length(q)-1]=0,0\n" +
            "           ,round((q[-1]\n" +
            "               -q[length(q)-1])\n" +
            "               /q[length(q)-1],3)) as rate\n" +
            "from (select event_name,groupArray(neg_comment_proportion) as q\n" +
            "    from event_comment_stat\n" +
            "      where dateDiff('day',time,now())<7\n" +
            "      GROUP BY event_name))\n" +
            "where neg_comment_proportion>#{threshold} and neg_comment_proportion<>1\n" +
            "order by ${para} desc\n" +
            "limit 10")
    List<Map<String, String>> getTopNWarningEvent(Double threshold,String para);
}


