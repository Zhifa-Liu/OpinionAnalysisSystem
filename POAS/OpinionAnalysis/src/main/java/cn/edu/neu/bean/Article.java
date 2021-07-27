package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 32098
 *
 * 与原始JSON数据对应
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Article {
    /**
     * 事件类型
     */
    private String field;

    /**
     * 用户名：即文章作者
     */
    private String userName;

    /**
     * 用户ID：即作者ID
     */
    private String userId;

    /**
     * 用户类型：即作者类型(媒体 or 网民)
     */
    private String userType;

    /**
     * 作者性别
     */
    private String gender;

    /**
     * 作者地域：当作文章发文地
     */
    private String location;

    /**
     * 作者粉丝数量
     */
    private int fansCount;

    /**
     * 文章ID
     */
    private String blogId;

    /**
     * 主题
     */
    private String theme;

    /**
     * 文章时间
     */
    private String createDate;

    /**
     * 文章内容
     */
    private String text;

    /**
     * 点赞量
     */
    private int attitudesCount;

    /**
     * 评论量
     */
    private int commentsCount;

    /**
     * 转发量
     */
    private int reportsCount;

    /**
     * 爬取时间
     */
    private String getTime;

    /**
     * 评论
     */
    private List<ArticleComment> comments;
}
