package cn.edu.neu.controller;

import cn.edu.neu.service.EventCommentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 32098
 */
@RestController
@RequestMapping("eventComment")
@CrossOrigin
public class EventCommentController {
    @Autowired
    private EventCommentService eventCommentService;

//    @RequestMapping(value = "eventCommentRank", method = RequestMethod.GET)
//    public String getEventCommentRank(@RequestParam("eventName") String eventName){
//        return eventCommentService.getEventCommentRank(eventName);
//    }

    @RequestMapping(value = "eventCommentLikeRank", method = RequestMethod.GET)
    public String getEventCommentLikeRank(@RequestParam("eventName") String eventName){
        return eventCommentService.getEventCommentLikeRank(eventName);
    }

    @RequestMapping(value = "eventCommentReplyRank", method = RequestMethod.GET)
    public String getEventCommentReplyRank(@RequestParam("eventName") String eventName){
        return eventCommentService.getEventCommentReplyRank(eventName);
    }

    @RequestMapping(value = "eventCommentKeyword", method = RequestMethod.GET)
    public String getEventCommentKeyword(@RequestParam("eventName") String eventName){
        return eventCommentService.getEventCommentKeyword(eventName);
    }

    @RequestMapping(value = "eventCommentEmotion", method = RequestMethod.GET)
    public String getEventCommentEmotionTrend(@RequestParam("eventName") String eventName){
        return eventCommentService.getEventCommentEmotionTrend(eventName);
    }

    @RequestMapping(value = "eventCommentGender", method = RequestMethod.GET)
    public String getEventCommentGender(@RequestParam("eventName") String eventName){
        return eventCommentService.getEventCommentGender(eventName);
    }

    @RequestMapping(value = "warnEvent", method = RequestMethod.GET)
    public String getWarnEvent(){
        return eventCommentService.getWarningEvent();
    }

    @RequestMapping(value = "warnEventTopN", method = RequestMethod.GET)
    public String getWarnEventTopN(){
        return eventCommentService.getTopNWarningPage();
    }
}


