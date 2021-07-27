package cn.edu.neu.service;

import cn.edu.neu.mapper.LogMapper;
import cn.edu.neu.mapper.ThresholdMapper;
import cn.edu.neu.po.Threshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 32098
 */
@Service
public class ThresholdService {
    @Autowired
    private ThresholdMapper thresholdMapper;
    @Autowired
    private LogMapper logMapper;

    public Threshold getThreshold(){
        return thresholdMapper.getThreshold();
    }

    public void setThreshold(double negCommentProportionThreshold){
        thresholdMapper.setThreshold(negCommentProportionThreshold);
        logMapper.setLog("事件舆论预警阈值配置","设置事件情感占比预警阈值="+negCommentProportionThreshold);
    }
}
