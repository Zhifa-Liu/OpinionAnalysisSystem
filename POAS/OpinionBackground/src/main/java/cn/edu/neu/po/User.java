package cn.edu.neu.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 32098
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String username;
    private String password;
    private String userType;
}


