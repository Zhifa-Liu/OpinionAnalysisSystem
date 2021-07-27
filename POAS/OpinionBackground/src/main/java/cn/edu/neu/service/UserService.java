package cn.edu.neu.service;

import cn.edu.neu.mapper.LogMapper;
import cn.edu.neu.mapper.UserMapper;
import cn.edu.neu.po.TestUser;
import cn.edu.neu.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 32098
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogMapper logMapper;

    public User login(String username, String psw){
        System.out.println(username+" "+psw+" "+userMapper.login(username, psw));
        if(userMapper.login(username, psw)!=null){
            logMapper.setLog("用户登录","用户"+username+"登陆系统");
        }
        return userMapper.login(username, psw);
    }

    public List<TestUser> getAllUser(){
        List<TestUser> users = userMapper.getAllUser();
        for(TestUser user: users){
            String hobby = user.getHobby();
            System.out.println(hobby);
            String[] hobbys = hobby.substring(1, hobby.length()-1).split(",");
            for (String s : hobbys) {
                System.out.println(s);
            }
        }
        return userMapper.getAllUser();
    }
}
