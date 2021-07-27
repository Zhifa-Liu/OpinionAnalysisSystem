package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: POAS
 * @description: 管理日志
 * @author: superwasabi
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlterLog {
    private String type;
    private String operator;
    private String times;
}
