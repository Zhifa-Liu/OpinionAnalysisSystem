package cn.edu.neu.service;

import cn.edu.neu.mapper.EventMapper;
import cn.edu.neu.mapper.LogMapper;
import cn.edu.neu.po.AlterLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: POAS
 * @description: 管理员日志
 * @author: superwasabi
 **/
@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    /**
     * 获取日志
     * @return
     */
    public List<AlterLog> getLog(){
        return logMapper.getLog();
    }

    public void setLog(AlterLog alterLog){
        String type=alterLog.getType();
        String operator=alterLog.getOperator();
        System.out.println(type);
        logMapper.setLog(type, operator);
    }
}
