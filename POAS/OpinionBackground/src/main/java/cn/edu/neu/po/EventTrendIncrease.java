package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 事件增长速率
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventTrendIncrease {
    private String time;
    private Double eventInfoIncrease;
    private Double eventAttentionIncrease;
}
