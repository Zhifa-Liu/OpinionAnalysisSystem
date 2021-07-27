package cn.edu.neu.controller;

import cn.edu.neu.po.User;
import cn.edu.neu.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 32098
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public User login(@RequestParam("username") String username, @RequestParam("password") String password){
        System.out.println(username+" "+password);
        return userService.login(username, password);
    }

    @RequestMapping(value = "getAllUser", method = RequestMethod.GET)
    public String getAllUser(){
        return JSONObject.toJSONString(userService.getAllUser());
    }
}
