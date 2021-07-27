package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 时间文章发表时间线
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleTime {
    private String articleAuthor;
    private String articleTime;
    private String articleText;
}
