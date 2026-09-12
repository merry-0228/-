package com.pet.dao;

import com.pet.entity.User;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public boolean register(User user) throws SQLException {
        String sql = "INSERT INTO user(phone,password,nickname,address,role) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPhone());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getRole());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public User login(String phone, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE phone=? AND password=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                return user;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    public List<User> findAllUsers() throws SQLException {
        String sql = "SELECT * FROM user";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setPhone(rs.getString("phone"));
                user.setNickname(rs.getString("nickname"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
            return users;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    public boolean isPhoneExists(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE phone=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 更新用户信息
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET nickname=?, address=?, password=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getNickname());
            pstmt.setString(2, user.getAddress());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 根据ID查找用户
    public User findUserById(Integer id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setPhone(rs.getString("phone"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                return user;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 删除用户
    public boolean deleteUser(Integer userId) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}