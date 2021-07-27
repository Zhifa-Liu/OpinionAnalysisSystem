package cn.edu.neu.mapper;

import cn.edu.neu.po.TestUser;
import cn.edu.neu.po.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 32098
 *
 * for clickhouse test
 */
@Repository
public interface UserMapper {
    /**
     * get all user
     * @return user list
     */
    @Select("select * from poas.user limit 1")
    List<TestUser> getAllUser();

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    @Select("SELECT * FROM poas.user WHERE username=#{username} AND password=#{password}")
    User login(String username, String password);
}
