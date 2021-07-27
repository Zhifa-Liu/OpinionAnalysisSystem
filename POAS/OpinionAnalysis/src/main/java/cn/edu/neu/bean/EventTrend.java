package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author 32098
 *
 * 与数据库表 event_trend 对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventTrend {
    private String eventName;
    private Long eventInfoCount;
    private Long eventAttentionCount;
    private String time;
    private Long statTimeStamp;
}
