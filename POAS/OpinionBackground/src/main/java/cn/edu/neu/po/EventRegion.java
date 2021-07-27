package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 领域热度
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventRegion {
    private String articleRegion;
    private Integer value;
}
