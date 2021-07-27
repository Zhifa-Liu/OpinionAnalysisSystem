package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 32098
 *
 * 与数据库表 event_comment_keyword 对应 （ 除用于去重的 commentIds 外）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventCommentKeyword {
    private String eventName;
    /**
     * commentId：用于去重
     *
     */
    private List<String> commentIds;
    private List<String> keyword;
    private List<Integer> wordCount;
    private String time;
}
