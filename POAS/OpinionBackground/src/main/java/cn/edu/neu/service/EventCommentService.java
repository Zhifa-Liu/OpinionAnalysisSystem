package cn.edu.neu.service;

import cn.edu.neu.mapper.EventCommentMapper;
import cn.edu.neu.mapper.ThresholdMapper;
import cn.edu.neu.mapper.TopNMapper;
import cn.edu.neu.po.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 32098
 */
@Service
public class EventCommentService {
    @Autowired
    private EventCommentMapper eventCommentMapper;

    @Autowired
    private TopNMapper topnMapper;
    @Autowired
    private ThresholdMapper thresholdMapper;

//    /**
//     * 获取事件评论的排名
//     *
//     * @param eventName 事件名
//     * @return 获取事件评论的点赞与回复排名
//     */
//    public String getEventCommentRank(String eventName){
//        int topN = 5;
//        int topn = topnMapper.getTopN().getEventCommentRank();
//        System.out.println(topn);
//        Map<String, Object> commentLikeRank = getEventCommentLikeRank(eventName, topN);
//        Map<String, Object> commentReplyRank = getEventCommentReplyRank(eventName, topN);
//        System.out.println(JSONObject.toJSONString(Arrays.asList(commentLikeRank, commentReplyRank)));
//        return JSONObject.toJSONString(Arrays.asList(commentLikeRank, commentReplyRank));
//    }

    /**
     * 获取事件评论的点赞排名
     *
     * @param eventName 事件名
     * @return Map<Comment, CommentLike>
     */
    public String getEventCommentLikeRank(String eventName){
        int topN = 5;
        int topn = topnMapper.getTopN().getEventCommentRank();
        System.out.println(topn);
        List<Map<String, Integer>> commentLikeRankList = eventCommentMapper.getEventCommentLikeRank(eventName, topn);
        return JSONObject.toJSONString(commentLikeRankList);
    }

    /**
     * 获取事件评论的回复排名
     *
     * @param eventName 事件名
     * @return Map<Comment, CommentReply>
     */
    public String getEventCommentReplyRank(String eventName){
        int topN = 5;
        int topn = topnMapper.getTopN().getEventCommentRank();
        System.out.println(topn);
        List<Map<String, Integer>> commentReplyRankList = eventCommentMapper.getEventCommentReplyRank(eventName, topn);
        return JSONObject.toJSONString(commentReplyRankList);
    }

    /**
     * 获取事件评论的关键词
     *
     * @param eventName 事件名
     * @return JSONString
     */
    public String getEventCommentKeyword(String eventName){
        EventCommentKeyword rowCommentKeyword = eventCommentMapper.getEventKeyword(eventName);
        String keywordStr = rowCommentKeyword.getCommentKeyword();
        String countStr = rowCommentKeyword.getKeywordCount();
        String[] keywordArray = keywordStr.substring(1, keywordStr.length()-1).split(",");
        String[] countArray = countStr.substring(1, countStr.length()-1).split(",");
        assert keywordArray.length == countArray.length;
        List<Map<String, String>> commentKeyword = new ArrayList<>();
        for(int i=0; i<keywordArray.length; i++){
            Map<String, String> map = new HashMap<>(5);
            map.put("name", keywordArray[i].replace("'", ""));
            map.put("value", countArray[i]);
            commentKeyword.add(map);
        }
        System.out.println(JSONObject.toJSONString(commentKeyword));
        return JSONObject.toJSONString(commentKeyword);
    }

    /**
     * 获取事件评论的情感趋势
     *
     * @param eventName 事件名
     * @return JSONString
     */
    public String getEventCommentEmotionTrend(String eventName){
        List<EventCommentEmotionTrend> eventCommentEmotionTrendList = eventCommentMapper.getEventCommentEmotionTrend(eventName);
        List<String> times = new ArrayList<>();
        List<Double> poss = new ArrayList<>();
        List<Double> negs = new ArrayList<>();
        List<List<EventComment>> lists = getEventComment(eventName);
        for(int i=eventCommentEmotionTrendList.size()-1; i>=0; i--){
            EventCommentEmotionTrend eventCommentEmotionTrend = eventCommentEmotionTrendList.get(i);
            times.add(eventCommentEmotionTrend.getTimes());
            poss.add(eventCommentEmotionTrend.getPosCommentProportion());
            negs.add(eventCommentEmotionTrend.getNegCommentProportion());
        }
        Map<String, Object> map = new HashMap<>(10);
        map.put("times", times);
        map.put("poss", poss);
        map.put("negs", negs);
        map.put("comments",lists);
        System.out.println(JSONObject.toJSONString(map));
        return JSONObject.toJSONString(map);
    }

    public List<List<EventComment>> getEventComment(String eventName){
        String pos="1";
        String neg="0";
        String mid="2";
        List<EventComment> posList = eventCommentMapper.getEventComment(eventName,pos);
        System.out.println(posList);
        List<EventComment> negList = eventCommentMapper.getEventComment(eventName,neg);
        List<EventComment> midList = eventCommentMapper.getEventComment(eventName,mid);
        List<List<EventComment>> lists =new ArrayList<>();
        lists.add(posList);
        lists.add(negList);
        lists.add(midList);
        return lists;

    }

    public String getEventCommentGender(String eventName){
        EventCommentGender temp = eventCommentMapper.getEventCommentGender(eventName);
        System.out.println(JSONObject.toJSONString(temp));
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> man = new HashMap<>();
        man.put("name", "男");
        man.put("value", String.valueOf(temp.getManCommentProportion()));
        Map<String, String> woman = new HashMap<>();
        woman.put("name", "女");
        woman.put("value", String.valueOf(temp.getWomanCommentProportion()));
        result.add(man);
        result.add(woman);
        System.out.println(JSONObject.toJSONString(result));
        return JSONObject.toJSONString(result);
    }

    public String getWarningEvent(){
//        double thresholdTemp = 0.6;
         double thresholdTemp = thresholdMapper.getThreshold().getNegCommentProportionThreshold();
        List<Map<String, String>> result = eventCommentMapper.getWarningEvent(thresholdTemp);
        System.out.println(JSONObject.toJSONString(result));
        return JSONObject.toJSONString(result);
    }

    public String getTopNWarningPage(){
//        double thresholdTemp = 0.5;
        Threshold threshold = thresholdMapper.getThreshold();
        double thresholdTemp=threshold.getNegCommentProportionThreshold();
        String negCommentProportion="negCommentProportion";
        String rate="rate";
        List<Map<String, String>> temp = eventCommentMapper.getTopNWarningEvent(thresholdTemp,negCommentProportion);
        List<Map<String, String>> temp2 = eventCommentMapper.getTopNWarningEvent(thresholdTemp,rate);
        System.out.println("**********"+JSONObject.toJSONString(temp));
        System.out.println("%%%%%%%%%%%"+JSONObject.toJSONString(temp2));

        List<Map<String, String>> result1 = new ArrayList<>();
        List<Map<String, String>> result2 = new ArrayList<>();
        List<List<Map<String, String>>> results = new ArrayList<>();
        for (Map<String, String> element : temp) {
            Map<String, String> map = new HashMap<>();
            map.put("name", element.get("eventName"));
            map.put("value", String.valueOf(element.get("negCommentProportion")));
            result1.add(map);
        }
        for (Map<String, String> element : temp2) {
            Map<String, String> map = new HashMap<>();
            map.put("name", element.get("eventName"));
            map.put("value", String.valueOf(element.get("rate")));
            result2.add(map);
        }
        results.add(result1);
        results.add(result2);
//        System.out.println(JSONObject.toJSONString(results));
        return JSONObject.toJSONString(results);
    }
}


