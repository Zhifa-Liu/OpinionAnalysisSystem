package cn.edu.neu.mapper;

import cn.edu.neu.po.ReptileParam;
import cn.edu.neu.po.Threshold;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author 32098
 */
@Repository
public interface ReptileMapper {
    /**
     * 获取爬虫参数
     *
     * @return Threshold
     */
    @Select("SELECT * FROM reptile_param ORDER BY modify_time DESC limit 1")
    ReptileParam getReptileParam();

    /**
     * 设置爬虫参数
     *
     * @param reptileGap 爬虫间隔
     * @param gapTimeUnit 间隔时间单位
     */
    @Select("INSERT INTO reptile_param VALUES (#{reptileGap}, #{gapTimeUnit}, now())")
    void setReptileParam(int reptileGap, String gapTimeUnit);
}
