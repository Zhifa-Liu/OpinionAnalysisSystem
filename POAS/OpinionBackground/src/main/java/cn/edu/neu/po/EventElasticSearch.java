package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author 32098
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "event")
public class EventElasticSearch {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String eventName;
    @Field(type = FieldType.Double)
    private Double eventHot;
    /**
     * FieldType.Keyword: 不能分词
     */
    @Field(type = FieldType.Keyword)
    private String eventType;
    /**
     * index = false: 不能查询
     */
    @Field(type = FieldType.Keyword, index = false)
    private String startTime;
    @Field(type = FieldType.Keyword, index = false)
    private String endTime;
}

