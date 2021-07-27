package cn.edu.neu.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 32098
 *
 * 与数据库表 event_keyword 对应（ 除用于去重的 articleIds 外）
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventKeyword {
    private String eventName;
    /**
     * articleIds：用于去重
     *
     */
    private List<String> articleIds;
    private List<String> keyword;
    private List<Integer> wordCount;
    private String time;
}

