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
public class EventKeyword {
    private String eventName;
    private String eventKeyword;
    private String keywordCount;
    private String time;
}
