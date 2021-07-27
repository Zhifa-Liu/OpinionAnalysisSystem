package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * 处理后的事件文章表
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventArticle {
    private String field;
    private String eventName;
    private String articleId;
    private String articleAuthor;
    private String authorId;
    private String authorGender;
    private String authorType;
    private String articleTime;
    private String articleText;
    private Integer attitudesCount;
    private Integer commentsCount;
    private Integer reportsCount;
    private Double articleHot;
    private String articleRegion;
    /**
     * private String sourceArticleId;
     */
    private String spiderTime;
}
