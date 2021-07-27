package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 32098
 *
 * for clickhouse test
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TestUser {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String hobby;
}
