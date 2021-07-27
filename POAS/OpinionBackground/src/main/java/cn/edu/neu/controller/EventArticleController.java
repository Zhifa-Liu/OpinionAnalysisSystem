package cn.edu.neu.controller;

import cn.edu.neu.po.ArticleTime;
import cn.edu.neu.service.EventArticleService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 32098
 */
@RestController
@RequestMapping("eventArticle")
@CrossOrigin
public class EventArticleController {
    @Autowired
    private EventArticleService eventArticleService;

    @RequestMapping(value = "eventArticleRank", method = RequestMethod.GET)
    public String getEventArticleRank(@RequestParam("eventName") String eventName){
        return eventArticleService.getEventArticleRank(eventName);
    }

    @RequestMapping(value = "articleRegionDistribute", method = RequestMethod.GET)
    public Map<String, Integer> getEventEventArticleRegionDistribute(@RequestParam("eventName") String eventName){
        return eventArticleService.getEventArticleRegionDistribute(eventName);
    }
    @RequestMapping(value = "articleTimeline", method = RequestMethod.GET)
    public String getEventArticleTimeline(@RequestParam("eventName") String eventName){
        return eventArticleService.getEventArticleTimeline(eventName);
    }
}
