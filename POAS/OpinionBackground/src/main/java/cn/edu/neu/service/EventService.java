package cn.edu.neu.service;

import cn.edu.neu.mapper.EventMapper;
import cn.edu.neu.po.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 32098
 */
@Service
public class EventService {
    @Autowired
    private EventMapper eventMapper;

    /**
     * 获取所有事件
     *
     * @return List<Event>
     */
    public List<Event> getAllEvent(){
        return eventMapper.getAllEvent();
    }

    /**
     * 获取事件增长率
     *
     * @return List<EventHotIncrease>
     */
    public List<EventHotIncrease> getEventHotIncrease(){
        return eventMapper.getEventHotIncrease();
    }

    public List<EventTrendIncrease> getEventTrendIncrease(String eventName){
        System.out.println(eventMapper.getEventTrendIncrease(eventName));
        return eventMapper.getEventTrendIncrease(eventName);
    }

    /**
     * 获取某一类型的所有事件
     *
     * @param eventType 事件类型
     * @return List<Event>
     */
    public List<Event> getEventByType(String eventType){
        return eventMapper.getEventByType(eventType);
    }

    /**
     * 获取不同领域下的时间热度
     *
     * @return JSONString
     */
    public List<EventTypeHot> getEventTypeHot(){
        List<EventTypeHot> eventTypeHotList = eventMapper.getEventTypeHot();
        System.out.println(eventTypeHotList);
        return eventTypeHotList;
    }

    /**
     * 获取所有事件所有文章的地域分布
     *
     * @return JSONString
     */
    public Map<String, Integer> getAllEventArticleRegionDistribute(){
        List<EventRegion> allEventArticleRegions = eventMapper.getAllEventArticleRegion();
        Map<String,Integer> map = new HashMap<String,Integer>();
        for (int i = 0; i < allEventArticleRegions.size(); i++) {
            EventRegion objs = allEventArticleRegions.get(i);
            map.put(objs.getArticleRegion(), objs.getValue());
        }
        return map;
    }

    /**
     * 获取事件关键词
     *
     * @param eventName 事件名
     * @return JSONString
     */
    public String getEventKeyword(String eventName){
        EventKeyword rowEventKeyword = eventMapper.getEventKeyword(eventName);
        String keywordStr = rowEventKeyword.getEventKeyword();
        String countStr = rowEventKeyword.getKeywordCount();
        String[] keywordArray = keywordStr.substring(1, keywordStr.length()-1).split(",");
        String[] countArray = countStr.substring(1, countStr.length()-1).split(",");
        assert keywordArray.length == countArray.length;
        List<Map<String, String>> eventKeyword = new ArrayList<>();
        for(int i=0; i<keywordArray.length; i++){
            Map<String, String> map = new HashMap<>(5);
            map.put("name", keywordArray[i].replace("'", ""));
            map.put("value", countArray[i]);
            eventKeyword.add(map);
        }
        System.out.println(JSONObject.toJSONString(eventKeyword));
        return JSONObject.toJSONString(eventKeyword);
    }

    /**
     * 获取事件走势
     *
     * @param eventName 事件走势
     * @return JSONString
     */
    public String getEventTrend(String eventName){
        List<EventTrend> eventTrendList = eventMapper.getEventTrendHour(eventName);
        List<EventTrend> eventTrendDayList = eventMapper.getEventTrendDay(eventName);

        System.out.println(eventTrendList.size()+" "+eventTrendDayList.size());
        List<String> times = new ArrayList<>();
        List<String> timesDay = new ArrayList<>();
        List<Long> infos = new ArrayList<>();
        List<Long> attentions = new ArrayList<>();
        List<Long> infosDay = new ArrayList<>();
        List<Long> attentionsDay = new ArrayList<>();
        for(int i=0; i<eventTrendDayList.size(); i++){
            EventTrend eventTrend = eventTrendDayList.get(i);
            timesDay.add(eventTrend.getTime());
            infosDay.add(eventTrend.getEventInfoCount());
            attentionsDay.add(eventTrend.getEventAttentionCount());
        }
        for(int i=0; i<eventTrendList.size(); i++){
            EventTrend eventTrend = eventTrendList.get(i);
            times.add(eventTrend.getTime());
            infos.add(eventTrend.getEventInfoCount());
            attentions.add(eventTrend.getEventAttentionCount());
        }
        Map<String, Object> map = new HashMap<>(6);
        map.put("times", times);
        map.put("infos", infos);
        map.put("attentions", attentions);
        map.put("times_day", timesDay);
        map.put("infos_day", infosDay);
        map.put("attentions_day", attentionsDay);
        System.out.println(JSONObject.toJSONString(map));
        return JSONObject.toJSONString(map);
    }

    /**
     * 获取事件核心传播人
     *
     * @param eventName 事件名
     * @return JSONString
     */
    public List<EventSpread> getEventSpread(String eventName){
        int topN = 7;
        List<EventSpread> eventSpreadA = eventMapper.getEventSpread(eventName, "媒体", topN);
        for(EventSpread eventSpread:eventSpreadA){
            System.out.println(eventSpread.getEventName()+" "+eventSpread.getSpreadPeople());
        }
        List<EventSpread> eventSpreadB = eventMapper.getEventSpread(eventName, "网民", topN);
        List<EventSpread> results = new ArrayList<>();
        results.addAll(eventSpreadA);
        results.addAll(eventSpreadB);
        return results;
    }
}

