package com.pet.dao;

import com.pet.entity.Complaint;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComplaintDao {
    // 添加投诉
    public boolean addComplaint(Complaint complaint) throws SQLException {
        String sql = "INSERT INTO complaint(user_id, shop_id, content, status, create_time) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, complaint.getUserId());
            pstmt.setInt(2, complaint.getShopId());
            pstmt.setString(3, complaint.getContent());
            pstmt.setString(4, complaint.getStatus());
            pstmt.setTimestamp(5, new java.sql.Timestamp(complaint.getCreateTime().getTime()));
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 查询所有投诉，管理员用
    public List<Complaint> findAllComplaints() throws SQLException {
        String sql = "SELECT * FROM complaint";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Complaint> complaints = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Complaint complaint = new Complaint();
                complaint.setId(rs.getInt("id"));
                complaint.setUserId(rs.getInt("user_id"));
                complaint.setShopId(rs.getInt("shop_id"));
                complaint.setContent(rs.getString("content"));
                complaint.setStatus(rs.getString("status"));
                complaint.setCreateTime(rs.getTimestamp("create_time"));
                complaint.setHandleTime(rs.getTimestamp("handle_time"));
                complaint.setHandleResult(rs.getString("handle_result"));
                complaints.add(complaint);
            }
            return complaints;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 处理投诉
    public boolean handleComplaint(Integer complaintId, String result) throws SQLException {
        String sql = "UPDATE complaint SET status=?, handle_time=?, handle_result=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "handled");
            pstmt.setTimestamp(2, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(3, result);
            pstmt.setInt(4, complaintId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
