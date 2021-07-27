package cn.edu.neu.mapper;

import cn.edu.neu.po.ArticleTime;
import cn.edu.neu.po.EventArticle;
import cn.edu.neu.po.EventRegion;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 32098
 *
 * 事件文章分析 Mapper
 */
@Repository
public interface EventArticleMapper {
    /**
     * 获取事件文章的点赞排名
     *
     * Error SQL:
     * SELECT article_id, attitudes_count FROM event_article WHERE event_name=#{eventName} ORDER BY attitudes_count desc limit #{topN}
     *
     * @param eventName eventName 事件名
     * @param topN topN
     * @return Map<ArticleId, AttitudesCount>
     *     max_time(spider_time) as latest_spider_time
     */
    @Select("SELECT * FROM (" +
            "   SELECT article_author, max(attitudes_count) as attitudes_count FROM event_article WHERE event_name=#{eventName} GROUP BY article_id,article_author" +
            ") ORDER BY attitudes_count desc limit #{topN}")
    List<Map<String, Integer>> getEventArticleLikeRank(String eventName, int topN);

    /**
     * 获取事件文章的评论排名
     *
     * @param eventName eventName
     * @param topN topN
     * @return Map<ArticleId, CommentsCount>
     */
    @Select("SELECT * FROM (" +
            "   SELECT article_author, max(comments_count) as comments_count FROM event_article WHERE event_name=#{eventName} GROUP BY article_id,article_author" +
            ") ORDER BY comments_count desc limit #{topN}")
    List<Map<String, Integer>> getEventArticleCommentRank(String eventName, int topN);

    /**
     * 获取事件文章的转发量排名
     *
     * @param eventName eventName
     * @param topN topN
     * @return Map<ArticleId, ReportsCount>
     */
    @Select("SELECT * FROM (" +
            "   SELECT article_author, max(reports_count) as reports_count FROM event_article WHERE event_name=#{eventName} GROUP BY article_id,article_author" +
            ") ORDER BY reports_count desc limit #{topN}")
    List<Map<String, Integer>> getEventArticleReportRank(String eventName, int topN);

    /**
     * 获取某一事件（@param eventName）所有文章的热度排名
     *
     * Error SQL:
     * SELECT article_text, article_hot FROM event_article WHERE event_name=#{eventName} ORDER BY article_hot desc limit #{topN}
     *
     * @param eventName eventName
     * @return Map<ArticleText, Hot>
     */
    @Select("SELECT article_author as name,\n" +
            "       article_hot as value,\n" +
            "       attitudes_count,\n" +
            "       comments_count,\n" +
            "       reports_count,\n" +
            "       article_text\n" +
            "FROM\n" +
            "            (SELECT article_author,\n" +
            "                    article_text,\n" +
            "                    max(attitudes_count) as attitudes_count,\n" +
            "                    max(comments_count) as comments_count,\n" +
            "                    max(reports_count) as reports_count,\n" +
            "                    round(max(article_hot),2) as article_hot\n" +
            "            FROM event_article WHERE event_name=#{eventName}\n" +
            "            GROUP BY article_author,article_id,article_text)\n" +
            "            ORDER BY article_hot desc\n" +
            "limit #{topN}")
    List<EventArticle> getEventArticleHotRank(String eventName,int topN);

    /**
     * 获取关于某一事件（@param eventName）的所有文章的地域分布：
     *
     * 下面的 SQL 语句存在问题，因为 event_article 表通过 spider_time 字段标识了同一篇文章的数据变化：
     * SELECT article_region, count() FROM event_article WHERE event_name=#{eventName} GROUP BY article_region
     *
     * @param eventName eventName
     * @return Map<Region, EventArticleCount>
     */
    @Select("select article_region,count(DISTINCT article_id) as value\n" +
            "from event_article\n" +
            "where event_name=#{eventName}\n" +
            "group by article_region")
    List<EventRegion> getEventArticleRegion(String eventName);

    /**
     * 获取文章的作者
     *
     * @param articleId 文章ID
     * @return ArticleAuthor
     */
    @Select("SELECT article_author from event_article WHERE article_id=#{articleId}")
    String getArticleAuthor(String articleId);

    /**
     * 获取转发该文章的转发文章ID与转发人
     *
     * @param articleId 文章ID
     * @return Map<ArticleID, ArticleAuthor>
     */
    @Select("SELECT article_id, article_author from event_article WHERE source_article_id=#{articleId}")
    Map<String, String> getArticleSpreadInfo(String articleId);

    @Select("select article_author,article_text,article_time\n" +
            "from event_article\n" +
            "where event_name=#{eventName}\n" +
            "group by article_author,article_text,article_time\n" +
            "order by article_time")
    List<ArticleTime> getArticleTime(String eventName);
}




