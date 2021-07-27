package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 与数据库表 event_comment_stat 对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCommentStat {
    private String eventName;
    private Double manCommentProportion;
    private Double womanCommentProportion;
    private Double posCommentProportion;
    private Double negCommentProportion;
    private String time;
}
