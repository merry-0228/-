package com.pet.dao;

import com.pet.entity.Appointment;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {
    // 添加预约
    public boolean addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointment(user_id, shop_id, service_id, appointment_time, status, pay_status, price, create_time) VALUES(?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, appointment.getUserId());
            pstmt.setInt(2, appointment.getShopId());
            pstmt.setInt(3, appointment.getServiceId());
            pstmt.setTimestamp(4, new java.sql.Timestamp(appointment.getAppointmentTime().getTime()));
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getPayStatus());
            pstmt.setDouble(7, appointment.getPrice());
            pstmt.setTimestamp(8, new java.sql.Timestamp(appointment.getCreateTime().getTime()));
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 查询用户的预约
    public List<Appointment> findAppointmentsByUserId(Integer userId) throws SQLException {
        String sql = "SELECT * FROM appointment WHERE user_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Appointment> appointments = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setId(rs.getInt("id"));
                app.setUserId(rs.getInt("user_id"));
                app.setShopId(rs.getInt("shop_id"));
                app.setServiceId(rs.getInt("service_id"));
                app.setAppointmentTime(rs.getTimestamp("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setPayStatus(rs.getString("pay_status"));
                app.setPrice(rs.getDouble("price"));
                app.setCreateTime(rs.getTimestamp("create_time"));
                appointments.add(app);
            }
            return appointments;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 查询店铺的预约
    public List<Appointment> findAppointmentsByShopId(Integer shopId) throws SQLException {
        String sql = "SELECT * FROM appointment WHERE shop_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Appointment> appointments = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shopId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setId(rs.getInt("id"));
                app.setUserId(rs.getInt("user_id"));
                app.setShopId(rs.getInt("shop_id"));
                app.setServiceId(rs.getInt("service_id"));
                app.setAppointmentTime(rs.getTimestamp("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setPayStatus(rs.getString("pay_status"));
                app.setPrice(rs.getDouble("price"));
                app.setCreateTime(rs.getTimestamp("create_time"));
                appointments.add(app);
            }
            return appointments;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 查询所有预约，管理员用
    public List<Appointment> findAllAppointments() throws SQLException {
        String sql = "SELECT * FROM appointment";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Appointment> appointments = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment();
                app.setId(rs.getInt("id"));
                app.setUserId(rs.getInt("user_id"));
                app.setShopId(rs.getInt("shop_id"));
                app.setServiceId(rs.getInt("service_id"));
                app.setAppointmentTime(rs.getTimestamp("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setPayStatus(rs.getString("pay_status"));
                app.setPrice(rs.getDouble("price"));
                app.setCreateTime(rs.getTimestamp("create_time"));
                appointments.add(app);
            }
            return appointments;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 更新预约状态
    public boolean updateAppointmentStatus(Integer appId, String status) throws SQLException {
        String sql = "UPDATE appointment SET status=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, appId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 更新支付状态
    public boolean updatePayStatus(Integer appId, String payStatus) throws SQLException {
        String sql = "UPDATE appointment SET pay_status=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, payStatus);
            pstmt.setInt(2, appId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
