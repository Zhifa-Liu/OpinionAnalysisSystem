package cn.edu.neu.controller;

import cn.edu.neu.po.*;
import cn.edu.neu.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 32098
 */
@RestController
@RequestMapping("event")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @RequestMapping(value = "eventList", method = RequestMethod.GET)
    public List<Event> getAllEvent(){
        return (List<Event>) eventService.getAllEvent();
    }

    @RequestMapping(value = "eventSubList", method = RequestMethod.GET)
    public List<Event> getEventByType(@RequestParam("eventType") String eventType){
        return eventService.getEventByType(eventType);
    }

    @RequestMapping(value = "eventTypeHot", method = RequestMethod.GET)
    public List<EventTypeHot> getEventTypeHot(){
        return eventService.getEventTypeHot();
    }

    @RequestMapping(value = "allEventArticleRegion", method = RequestMethod.GET)
    public Map<String, Integer> getAllEventArticleRegionDistribute(){
        return eventService.getAllEventArticleRegionDistribute();
    }

    @RequestMapping(value = "eventKeyword", method = RequestMethod.GET)
    public String getEventKeyword(@RequestParam("eventName") String eventName){
        return eventService.getEventKeyword(eventName);
    }

    @RequestMapping(value = "eventTrend", method = RequestMethod.GET)
    public String getEventTrend(@RequestParam("eventName") String eventName){
        return eventService.getEventTrend(eventName);
    }

    @RequestMapping(value = "eventSpread", method = RequestMethod.GET)
    public List<EventSpread> getEventSpread(@RequestParam("eventName") String eventName){
        return eventService.getEventSpread(eventName);
    }

    @RequestMapping(value = "eventHotWarning", method = RequestMethod.GET)
    public List<EventHotIncrease> getEventHotIncrease(){
        return eventService.getEventHotIncrease();
    }

    @RequestMapping(value = "eventTrendIncrease", method = RequestMethod.GET)
    public List<EventTrendIncrease> getEventTrendIncrease(@RequestParam("eventName") String eventName){
        return eventService.getEventTrendIncrease(eventName);
    }

}
