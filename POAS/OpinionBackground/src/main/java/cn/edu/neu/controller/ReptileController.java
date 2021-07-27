package cn.edu.neu.controller;

import cn.edu.neu.service.ReptileService;
import cn.edu.neu.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 32098
 */
@RestController
@RequestMapping("reptile")
@CrossOrigin
public class ReptileController {
    @Autowired
    private ReptileService reptileService;

    @RequestMapping(value = "setReptileParam", method = RequestMethod.POST)
    public boolean setReptileParam(@RequestBody Map<String, String> thresholdMap){
        String gap = thresholdMap.get("gap");
        String timeUnit = thresholdMap.get("time_unit");
        System.out.println(gap + " " + timeUnit);
        try{
            reptileService.setReptileParam(Integer.parseInt(gap), timeUnit);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}



