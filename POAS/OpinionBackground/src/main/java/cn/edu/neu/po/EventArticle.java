package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 时间文章
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventArticle {
    private String name;
    private Double value;
    private Double attitudesCount;
    private Double commentsCount;
    private Double reportsCount;
    private String articleText;
}
