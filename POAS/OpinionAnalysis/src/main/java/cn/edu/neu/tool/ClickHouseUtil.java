package cn.edu.neu.tool;

import cn.edu.neu.cons.ClickHouseConstant;

import java.sql.*;

/**
 * @program: POAS
 * @description: clickhouse
 * @author: superwasabi
 **/
public class ClickHouseUtil {
    private static Connection connection = null;

    /**
     * 获取 ClickHouse 连接
     * @return ClickHouse Connection
     */
    public static Connection getConnection(){
//        ClickHouseProperties properties = new ClickHouseProperties();
//        properties.setUser(ClickHouseConstant.USERNAME);
//        properties.setPassword(ClickHouseConstant.PASSWORD);
//        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(ClickHouseConstant.URL, properties);
//        try {
//            return clickHouseDataSource.getConnection();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return null;
        try {
            Class.forName(ClickHouseConstant.DRIVER);
            if(connection==null){
                connection = DriverManager.getConnection(ClickHouseConstant.URL, ClickHouseConstant.USERNAME, ClickHouseConstant.PASSWORD);
            }
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 关闭 ClickHouse 连接
     * @param connection connection
     */
    public static void closeConnection(Connection connection){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 执行 ClickHouse SQL
     *
     * @param sql sql
     * @return boolean
     */
    public static boolean executeSql(String sql){
        if(connection == null){
            connection = getConnection();
        }
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        if(connection==null){
            connection = getConnection();
        }
        System.out.println(executeSql(ClickHouseConstant.CREATE_DATABASE_POAS));
    }
}
