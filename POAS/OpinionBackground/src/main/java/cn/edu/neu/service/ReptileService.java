package cn.edu.neu.service;

import cn.edu.neu.mapper.LogMapper;
import cn.edu.neu.mapper.ReptileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 32098
 */
@Service
public class ReptileService {
    @Autowired
    private ReptileMapper reptileMapper;
    @Autowired
    private LogMapper logMapper;



    public void setReptileParam(int gap, String timeUnit){

        reptileMapper.setReptileParam(gap, timeUnit);
        logMapper.setLog("爬虫参数配置","设置数据爬取间隔="+gap+timeUnit);
    }
}
