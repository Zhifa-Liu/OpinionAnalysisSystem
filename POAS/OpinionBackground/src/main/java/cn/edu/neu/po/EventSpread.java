package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 32098
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventSpread {
    private String eventName;
    private String spreadPeople;
    private String spreadPeopleType;
    private String spreadCount;
}
