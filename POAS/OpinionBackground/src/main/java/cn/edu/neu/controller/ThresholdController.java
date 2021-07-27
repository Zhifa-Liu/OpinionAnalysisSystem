package cn.edu.neu.controller;

import cn.edu.neu.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 32098
 */
@RestController
@RequestMapping("threshold")
@CrossOrigin
public class ThresholdController {
    @Autowired
    private ThresholdService thresholdService;

    @RequestMapping(value = "setThreshold", method = RequestMethod.POST)
    public boolean setThreshold(@RequestBody Map<String, Double> thresholdMap){
        try {
            double negCommentProportionThreshold = thresholdMap.get("negCommentProportionThreshold");
//            double negArticleProportionThreshold = thresholdMap.get("negArticleProportionThreshold");
//            System.out.println(negCommentProportionThreshold + " " + negArticleProportionThreshold);
            System.out.println(negCommentProportionThreshold);
            thresholdService.setThreshold(negCommentProportionThreshold);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}


