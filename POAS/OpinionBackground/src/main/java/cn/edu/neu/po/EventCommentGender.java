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
public class EventCommentGender {
    private String eventName;
    private Double manCommentProportion;
    private Double womanCommentProportion;
    private String time;
}
