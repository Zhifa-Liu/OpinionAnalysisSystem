package cn.edu.neu.mapper;

import cn.edu.neu.po.AlterLog;
import cn.edu.neu.po.Event;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: POAS
 * @description: 管理记录
 * @author: superwasabi
 **/
@Repository
public interface LogMapper {
//    @Select("")
//    List<AlterLog> getLog();

    @Select("INSERT INTO logs VALUES (#{type}, #{operator}, now())")
    void setLog(String type,String operator);

    @Select("select *\n" +
            "from logs\n" +
            "order by times desc ")
    List<AlterLog> getLog();

}
