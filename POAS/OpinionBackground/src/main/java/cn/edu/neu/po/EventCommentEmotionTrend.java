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
public class EventCommentEmotionTrend {
    private String eventName;
    private double posCommentProportion;
    private double negCommentProportion;
    private String times;
}
