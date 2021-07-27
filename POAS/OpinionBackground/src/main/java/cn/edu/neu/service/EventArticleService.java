package cn.edu.neu.service;

import cn.edu.neu.mapper.EventArticleMapper;
import cn.edu.neu.mapper.TopNMapper;
import cn.edu.neu.po.ArticleTime;
import cn.edu.neu.po.EventArticle;
import cn.edu.neu.po.EventRegion;
import cn.edu.neu.po.TopN;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 32098
 */
@Service
public class EventArticleService {
    @Autowired
    private EventArticleMapper eventArticleMapper;
    @Autowired
    private TopNMapper topnMapper;

    /**
     * 获取事件文章各种排名
     * @param eventName 事件名
     * @return 事件文章的各种排名
     */
    public String getEventArticleRank(String eventName){
        int topN = 5;
        TopN top = topnMapper.getTopN();
        int topN1 = top.getArticleRank();
        int topN2 = top.getArticleHotRank();
        System.out.println(topN1+" "+topN2);
        Map<String, Object> articleLikeRank = getEventArticleLikeRank(eventName, topN1);
        Map<String, Object> articleCommentRank = getEventArticleCommentRank(eventName, topN1);
        Map<String, Object> articleReplyRank = getEventArticleReportRank(eventName, topN1);
        List<EventArticle> articleHotRank = getEventArticleHotRank(eventName,topN2);
        System.out.println(JSONObject.toJSONString(Arrays.asList(articleLikeRank, articleCommentRank, articleReplyRank,articleHotRank)));

        return JSONObject.toJSONString(Arrays.asList(articleLikeRank, articleCommentRank, articleReplyRank, articleHotRank));
    }

    /**
     * 获取事件文章的点赞排名
     * @param eventName eventName
     * @param topN topN
     * @return 事件文章的点赞排名
     */
    private Map<String, Object> getEventArticleLikeRank(String eventName, int topN){
        List<Map<String, Integer>> articleLikeRankList = eventArticleMapper.getEventArticleLikeRank(eventName, topN);
        List<String> articleIdList = new ArrayList<>();
        List<Integer> articleLikeCountList = new ArrayList<>();
        for (Map<String, Integer> stringIntegerMap : articleLikeRankList) {
            articleIdList.add(String.valueOf(stringIntegerMap.get("article_author")));
            articleLikeCountList.add(Integer.valueOf(String.valueOf(stringIntegerMap.get("attitudes_count"))));
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("articleAuthor", articleIdList);
        map.put("articleLikeCount", articleLikeCountList);
        return map;
    }

    /**
     * 获取事件文章的评论排名
     * @param eventName eventName
     * @param topN topN
     * @return 事件文章的评论排名
     */
    private Map<String, Object> getEventArticleCommentRank(String eventName, int topN){
        List<Map<String, Integer>> articleCommentRankList = eventArticleMapper.getEventArticleCommentRank(eventName, topN);
        List<String> articleIdList = new ArrayList<>();
        List<Integer> articleCommentCountList = new ArrayList<>();
        for (Map<String, Integer> stringIntegerMap : articleCommentRankList) {
            articleIdList.add(String.valueOf(stringIntegerMap.get("article_author")));
            articleCommentCountList.add(Integer.valueOf(String.valueOf(stringIntegerMap.get("comments_count"))));
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("articleAuthor", articleIdList);
        map.put("articleCommentCount", articleCommentCountList);
        return map;
    }

    /**
     * 获取事件文章的转发排名
     * @param eventName eventName
     * @param topN topN
     * @return 事件文章的转发排名
     */
    private Map<String, Object> getEventArticleReportRank(String eventName, int topN){
        List<Map<String, Integer>> articleReportRankList = eventArticleMapper.getEventArticleReportRank(eventName, topN);
        List<String> articleIdList = new ArrayList<>();
        List<Integer> articleReportCountList = new ArrayList<>();
        for (Map<String, Integer> stringIntegerMap : articleReportRankList) {
            articleIdList.add(String.valueOf(stringIntegerMap.get("article_author")));
            articleReportCountList.add(Integer.valueOf(String.valueOf(stringIntegerMap.get("reports_count"))));
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("articleAuthor", articleIdList);
        map.put("articleReportCount", articleReportCountList);
        return map;
    }

    /**
     * 获取事件文章的热度排名
     * @param eventName eventName
     * @return 事件文章的转发排名
     */
    private List<EventArticle> getEventArticleHotRank(String eventName,int topN){
        List<EventArticle> articleHotRankList = eventArticleMapper.getEventArticleHotRank(eventName,topN);
//        List<String> articleIdList = new ArrayList<>();
//        List<Double> articleHotList = new ArrayList<>();
//        for (Map<String, Double> stringIntegerMap : articleHotRankList) {
//            articleIdList.add(String.valueOf(stringIntegerMap.get("article_id")));
//            articleHotList.add(Double.parseDouble(String.valueOf(stringIntegerMap.get("article_hot"))));
//        }
//        Map<String, Object> map = new HashMap<>(3);
//        map.put("articleId", articleIdList);
//        map.put("articleHot", articleHotList);
        return articleHotRankList;
    }

    /**
     * 获取事件文章的地域分布
     * @param eventName eventName
     * @return 事件文章的地域分布信息
     */
    public Map<String, Integer> getEventArticleRegionDistribute(String eventName){
        List<EventRegion> eventArticleRegions = eventArticleMapper.getEventArticleRegion(eventName);
        Map<String,Integer> map = new HashMap<String,Integer>();
        for (int i = 0; i < eventArticleRegions.size(); i++) {
            EventRegion objs = eventArticleRegions.get(i);
            map.put(objs.getArticleRegion(), objs.getValue());
        }
        return map;
    }

    /**
     * 获取事件某一文章的传播链路：未完成整棵树的构建
     *
     * @param articleId 文章ID
     */
    public String getEventArticleSpreadLink(String articleId){
        JSONObject jsonObject = new JSONObject();
        String articleAuthor = eventArticleMapper.getArticleAuthor(articleId);
        // Map<转发文章IDArticleID, 文章作者>
        Map<String, String> articleSpreadInfo = eventArticleMapper.getArticleSpreadInfo(articleId);
        List<String> reportLevel1 = new ArrayList<>(articleSpreadInfo.values());
        jsonObject.put("name", articleAuthor);
        jsonObject.put("children", reportLevel1);
        return jsonObject.toJSONString();
    }

    public String getEventArticleTimeline(String eventName){
        List<ArticleTime> list = eventArticleMapper.getArticleTime(eventName);
        return JSONObject.toJSONString(list);

    }
}




