package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author 32098
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventTrend {
    private String eventName;
    private Long eventInfoCount;
    private Long eventAttentionCount;
    private String time;
}
