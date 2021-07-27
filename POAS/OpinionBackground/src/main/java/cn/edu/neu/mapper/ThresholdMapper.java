package cn.edu.neu.mapper;

import cn.edu.neu.po.Threshold;
import cn.edu.neu.po.TopN;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 32098
 */
@Repository
public interface ThresholdMapper {
    /**
     * 获取阈值
     *
     * @return Threshold
     */
    @Select("SELECT * FROM threshold ORDER BY modify_time DESC limit 1")
    Threshold getThreshold();

    /**
     * 设置阈值
     *
     * @param negCommentProportionThreshold 事件负面评论占比阈值
     */
    @Select("INSERT INTO threshold VALUES (#{negCommentProportionThreshold},now())")
    void setThreshold(double negCommentProportionThreshold);
}
