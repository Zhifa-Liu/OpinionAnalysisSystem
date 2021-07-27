package cn.edu.neu.controller;

import cn.edu.neu.service.EventSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 32098
 */
@RestController
@RequestMapping("eventES")
@CrossOrigin
public class EventSearchController {
    @Autowired
    private EventSearchService eventSearchService;

    @RequestMapping(value = "eventSearch", method = RequestMethod.GET)
    public String searchEvent(@RequestParam("searchInput") String searchInput){
        System.out.println(searchInput);
        return eventSearchService.searchByInput(searchInput);
    }

    @RequestMapping(value = "eventSearchAll", method = RequestMethod.GET)
    public String searchAllEvent(){
        return eventSearchService.searchAllEvent();
    }
}
