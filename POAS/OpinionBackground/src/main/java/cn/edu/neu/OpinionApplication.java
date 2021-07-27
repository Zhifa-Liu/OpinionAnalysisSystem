package cn.edu.neu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 32098
 */
@MapperScan("cn.edu.neu.mapper")
@SpringBootApplication
public class OpinionApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpinionApplication.class, args);
    }
}

