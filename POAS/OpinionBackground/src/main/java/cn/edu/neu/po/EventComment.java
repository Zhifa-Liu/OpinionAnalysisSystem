package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 事件评论
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventComment {
    private String commentText;
    private String commentEmotion;
    private String timetmp;
}
