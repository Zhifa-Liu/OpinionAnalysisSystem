package cn.edu.neu.bean;

/**
 * @author 32098
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 与数据库表 event_spread 对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventSpread {
    private String eventName;
    private String spreadPeople;
    private String spreadPeopleType;
    private Long spreadCount;
}
