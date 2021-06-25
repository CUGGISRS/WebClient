package com.lomoye.easy.utils;

import com.alibaba.fastjson.JSONObject;
import com.lomoye.easy.model.ExportParams;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.*;

/**
 * @description:
 * @author: gsy
 * @create: 2020-10-21 12:03
 **/

public class DBUtil {
    static String ip = "127.0.0.1";
    static int port = 3306;
    static String database = "test";
    static String encoding = "UTF-8";
    static String loginName = "root";
    static String password = "123456";
    static {
        try {
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        //本机
        //String url = String.format("jdbc:mysql://127.0.0.1:3306/portalsystem?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true");
        //服务器
        String url = String.format("jdbc:mysql://192.168.8.166:3306/portalsystem?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true");

        return DriverManager.getConnection(url, loginName, password);
    }

    //测试用例
    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT * FROM com_article_info";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String id = resultSet.getString("id");
            String title = resultSet.getString("title");
            System.out.println(id+title);
        }
        System.out.println(getConnection());
    }

    public Integer insert(JSONObject result, Connection connection, PreparedStatement preparedStatement, ExportParams exportParams){
        int i = 0;
//      Connection connection = null;
//      StringBuilder sql = new StringBuilder("insert into article" );
        String sql = "INSERT INTO com_article_info(title,content,pub_time,simple,author) VALUES (?,?,?,?,?)";
        try {
//            connection = getConnection();
            //主表插入数据
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //标题
            preparedStatement.setString(1, result.getString("title"));
            //正文内容
            preparedStatement.setString(2, result.getString("content"));
            //发布时间 暂不处理
            preparedStatement.setString(3, result.getString("time"));
            //简介
            preparedStatement.setString(4, result.getString("simple"));
            //作者
            preparedStatement.setString(5, result.getString("author"));


            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            long aLong = 0;
            if(generatedKeys.next()){
                 aLong = generatedKeys.getLong(1);
            }

            //中间表插入数据
            String sql02 = "INSERT com_article_system_info(article_id,system_id,article_typeid) VALUES(?,?,?)";
            PreparedStatement preparedStatement02 = connection.prepareStatement(sql02, Statement.RETURN_GENERATED_KEYS);
            preparedStatement02.setInt(1, (int) aLong);
            //输出表，系统类型
            preparedStatement02.setString(2, exportParams.getSysType());
            //输出表，文章类型
            preparedStatement02.setString(3, exportParams.getArticleType());

            preparedStatement02.executeUpdate();
            System.out.println("中间表插入成功");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return i;
    }

    //市场行情专用插入方法
    public Integer insertMarket(JSONObject result, Connection connection, PreparedStatement preparedStatement, ExportParams exportParams){
        int i = 0;

        String sql = "INSERT INTO dzsw_marketing_article_info(title,content,product_category,source,pub_time) VALUES (?,?,?,?,?)";
        try {
            //主表插入数据
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //标题
            preparedStatement.setString(1, result.getString("title"));
            //正文内容
            preparedStatement.setString(2, result.getString("content"));
            //分类，暂时写死为水果
            preparedStatement.setString(3, "水果");
            //作者
            preparedStatement.setString(4, result.getString("author"));
            //发布时间
            preparedStatement.setString(5, result.getString("time"));

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            long aLong = 0;
            if(generatedKeys.next()){
                aLong = generatedKeys.getLong(1);
            }

            System.out.println("市场行情专用插入方法");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return i;
    }
}
