package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 辅助@entity(Article)，使与原始JSON数据对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleComment {
    private String commentId;
    private String commenterId;
    private String commenterName;
    private String commenterGender;
    private String commentText;
    private Integer commentReply;
    private Integer commentLike;
}
