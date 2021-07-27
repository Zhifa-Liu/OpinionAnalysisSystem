package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 与数据库的 event 表对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {
    private String eventName;
    private String eventType;
    private Double eventHot;
    private String startTime;
    private String endTime;
}


