package com.pet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    // 添加 allowPublicKeyRetrieval=true 参数
    private static final String URL =
            "jdbc:mysql://localhost:3306/pet_service?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String URL_NO_DB =
            "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            createDatabaseIfNotExists();
            createTablesIfNotExists();
            initTestData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(URL_NO_DB, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS pet_service DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
            stmt.executeUpdate(sql);
            System.out.println("数据库 pet_service 已自动创建/已存在");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTablesIfNotExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 用户表
            String userTable = "CREATE TABLE IF NOT EXISTS `user` (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "phone VARCHAR(20) NOT NULL UNIQUE, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "nickname VARCHAR(50), " +
                    "address VARCHAR(255), " +
                    "role VARCHAR(20) NOT NULL DEFAULT 'user', " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(userTable);

            // 店铺表
            String shopTable = "CREATE TABLE IF NOT EXISTS shop (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "owner_id INT NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "address VARCHAR(255), " +
                    "phone VARCHAR(20), " +
                    "description TEXT, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(shopTable);

            // 服务表
            String serviceTable = "CREATE TABLE IF NOT EXISTS service_item (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "shop_id INT NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "type VARCHAR(20), " +
                    "price DOUBLE NOT NULL, " +
                    "description TEXT)";
            stmt.executeUpdate(serviceTable);

            // 预约表
            String appointmentTable = "CREATE TABLE IF NOT EXISTS appointment (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT NOT NULL, " +
                    "shop_id INT NOT NULL, " +
                    "service_id INT NOT NULL, " +
                    "appointment_time TIMESTAMP NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'pending', " +
                    "pay_status VARCHAR(20) DEFAULT 'unpaid', " +
                    "price DOUBLE NOT NULL, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(appointmentTable);

            // 评价表
            String evalTable = "CREATE TABLE IF NOT EXISTS evaluation (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "appointment_id INT NOT NULL, " +
                    "user_id INT NOT NULL, " +
                    "shop_id INT NOT NULL, " +
                    "content TEXT, " +
                    "score INT NOT NULL, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(evalTable);

            // 宠物表
            String petTable = "CREATE TABLE IF NOT EXISTS pet (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT NOT NULL, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "breed VARCHAR(50), " +
                    "age INT, " +
                    "gender VARCHAR(10), " +
                    "birth_date DATE, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(petTable);

            // 投诉表
            String complaintTable = "CREATE TABLE IF NOT EXISTS complaint (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT NOT NULL, " +
                    "shop_id INT NOT NULL, " +
                    "content TEXT, " +
                    "status VARCHAR(20) DEFAULT 'unhandled', " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "handle_time TIMESTAMP NULL, " +
                    "handle_result TEXT)";
            stmt.executeUpdate(complaintTable);

            // 操作日志表
            String logTable = "CREATE TABLE IF NOT EXISTS operation_log (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "user_id INT, " +
                    "action VARCHAR(100), " +
                    "detail TEXT, " +
                    "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(logTable);

            System.out.println("所有数据表已自动创建");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initTestData() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // 清理旧数据
            stmt.executeUpdate("DELETE FROM pet WHERE name='金毛'");
            stmt.executeUpdate("DELETE FROM service_item WHERE name='洗澡服务'");
            stmt.executeUpdate("DELETE FROM shop WHERE name='爱心宠物生活馆'");
            stmt.executeUpdate("DELETE FROM `user` WHERE phone='admin' OR phone='user' OR phone='vip' OR phone='shopowner'");

            // 管理员
            stmt.executeUpdate("INSERT INTO `user`(phone,password,nickname,role) " +
                    "VALUES('admin','admin','超级管理员','admin')");

            // 普通用户
            stmt.executeUpdate("INSERT INTO `user`(phone,password,nickname,address,role) " +
                    "VALUES('user','123456','测试用户','广州市天河区','user')");

            // VIP用户
            stmt.executeUpdate("INSERT INTO `user`(phone,password,nickname,address,role) " +
                    "VALUES('vip','123456','VIP会员','广州市越秀区','vip')");

            // 商家
            stmt.executeUpdate("INSERT INTO `user`(phone,password,nickname,address,role) " +
                    "VALUES('shopowner','123456','宠物店老板','广州市白云区','shop')");

            // 店铺
            stmt.executeUpdate("INSERT INTO shop(owner_id,name,address,phone,description) " +
                    "SELECT id,'爱心宠物生活馆','白云区嘉禾望岗','13900139000','主营洗澡、美容、寄养' " +
                    "FROM `user` WHERE phone='shopowner' LIMIT 1");

            // 宠物
            stmt.executeUpdate("INSERT INTO pet(user_id,name,breed,gender,age,birth_date) " +
                    "SELECT id,'金毛','金毛犬','母',2,'2024-01-15' " +
                    "FROM `user` WHERE phone='user' LIMIT 1");

            // 服务
            stmt.executeUpdate("INSERT INTO service_item(shop_id,name,type,price,description) " +
                    "SELECT id,'洗澡服务','洗护',60.00,'基础洗澡：洗毛、吹干、梳毛' " +
                    "FROM shop WHERE name='爱心宠物生活馆' LIMIT 1");

            // 寄养服务
            stmt.executeUpdate("INSERT INTO service_item(shop_id,name,type,price,description) " +
                    "SELECT id,'寄养服务','寄养',120.00,'舒适环境，专人照顾' " +
                    "FROM shop WHERE name='爱心宠物生活馆' LIMIT 1");

            // 美容服务
            stmt.executeUpdate("INSERT INTO service_item(shop_id,name,type,price,description) " +
                    "SELECT id,'美容造型','美容',200.00,'专业造型设计' " +
                    "FROM shop WHERE name='爱心宠物生活馆' LIMIT 1");

            System.out.println("✅ 测试数据初始化完成");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }
}