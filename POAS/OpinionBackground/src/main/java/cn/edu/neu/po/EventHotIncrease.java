package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 事件热度增长率
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventHotIncrease {
    private String name;
    private Double eventHot;
    private Double value;
}
