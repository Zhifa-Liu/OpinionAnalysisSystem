package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCommentKeyword {
    private String eventName;
    private String commentKeyword;
    private String keywordCount;
    private String time;
}

