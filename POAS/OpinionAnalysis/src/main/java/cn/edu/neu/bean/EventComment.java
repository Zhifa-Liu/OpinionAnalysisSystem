package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 处理后的事件评论表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventComment {
    private String eventName;
    private String commentId;
    private String commentText;
    private Integer commentLike;
    private Integer commentReply;
    private String commentEmotion;
    private String commenterGender;
    private String spiderTime;
}


