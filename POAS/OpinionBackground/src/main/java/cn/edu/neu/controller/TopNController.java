package cn.edu.neu.controller;

import cn.edu.neu.service.TopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 32098
 */
@RestController
@RequestMapping("topN")
@CrossOrigin
public class TopNController {
    @Autowired
    private TopNService topnService;

    @RequestMapping(value = "setTopN", method = RequestMethod.POST)
    public boolean setTopN(@RequestBody Map<String, Integer> topnMap){
        int articleRank = topnMap.get("articleRank");
        int articleHotRank = topnMap.get("articleHotRank");
        int eventCommentRank = topnMap.get("eventCommentRank");
        System.out.println(articleRank + " " + articleHotRank + " " + eventCommentRank);
        try{
            topnService.setTopN(articleRank, articleHotRank, eventCommentRank);
            return true;
        }catch (Exception exception){
            return false;
        }
    }
}



