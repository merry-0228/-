package com.pet.dao;

import com.pet.entity.Evaluation;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDao {
    // 添加评价
    public boolean addEvaluation(Evaluation evaluation) throws SQLException {
        String sql = "INSERT INTO evaluation(appointment_id, user_id, shop_id, content, score, create_time) VALUES(?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, evaluation.getAppointmentId());
            pstmt.setInt(2, evaluation.getUserId());
            pstmt.setInt(3, evaluation.getShopId());
            pstmt.setString(4, evaluation.getContent());
            pstmt.setInt(5, evaluation.getScore());
            pstmt.setTimestamp(6, new java.sql.Timestamp(evaluation.getCreateTime().getTime()));
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 查询店铺的评价
    public List<Evaluation> findEvaluationsByShopId(Integer shopId) throws SQLException {
        String sql = "SELECT * FROM evaluation WHERE shop_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Evaluation> evaluations = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shopId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Evaluation eval = new Evaluation();
                eval.setId(rs.getInt("id"));
                eval.setAppointmentId(rs.getInt("appointment_id"));
                eval.setUserId(rs.getInt("user_id"));
                eval.setShopId(rs.getInt("shop_id"));
                eval.setContent(rs.getString("content"));
                eval.setScore(rs.getInt("score"));
                eval.setCreateTime(rs.getTimestamp("create_time"));
                evaluations.add(eval);
            }
            return evaluations;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 查询所有评价，管理员用
    public List<Evaluation> findAllEvaluations() throws SQLException {
        String sql = "SELECT * FROM evaluation";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Evaluation> evaluations = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Evaluation eval = new Evaluation();
                eval.setId(rs.getInt("id"));
                eval.setAppointmentId(rs.getInt("appointment_id"));
                eval.setUserId(rs.getInt("user_id"));
                eval.setShopId(rs.getInt("shop_id"));
                eval.setContent(rs.getString("content"));
                eval.setScore(rs.getInt("score"));
                eval.setCreateTime(rs.getTimestamp("create_time"));
                evaluations.add(eval);
            }
            return evaluations;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }
}
