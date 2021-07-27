package cn.edu.neu.controller;

import cn.edu.neu.po.AlterLog;
import cn.edu.neu.po.Event;
import cn.edu.neu.service.EventSearchService;
import cn.edu.neu.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: POAS
 * @description: 管理员使用记录
 * @author: superwasabi
 **/
@RestController
@RequestMapping("log")
@CrossOrigin
public class LogController {
    @Autowired
    private LogService logService;

    @RequestMapping(value = "getLog", method = RequestMethod.GET)
    public List<AlterLog> getLog(){
        return logService.getLog();
    }

}
