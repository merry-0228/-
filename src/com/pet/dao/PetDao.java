package com.pet.dao;

import com.pet.entity.Pet;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetDao {
    // 添加宠物档案
    public boolean addPet(Pet pet) throws SQLException {
        String sql = "INSERT INTO pet(user_id, name, breed, age, gender, birth_date, create_time) VALUES(?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pet.getUserId());
            pstmt.setString(2, pet.getName());
            pstmt.setString(3, pet.getBreed());
            pstmt.setInt(4, pet.getAge());
            pstmt.setString(5, pet.getGender());
            pstmt.setDate(6, pet.getBirthDate() != null ? new java.sql.Date(pet.getBirthDate().getTime()) : null);
            pstmt.setTimestamp(7, new java.sql.Timestamp(pet.getCreateTime().getTime()));
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 查询用户的所有宠物
    public List<Pet> findPetsByUserId(Integer userId) throws SQLException {
        String sql = "SELECT * FROM pet WHERE user_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Pet> pets = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setUserId(rs.getInt("user_id"));
                pet.setName(rs.getString("name"));
                pet.setBreed(rs.getString("breed"));
                pet.setAge(rs.getInt("age"));
                pet.setGender(rs.getString("gender"));
                pet.setBirthDate(rs.getDate("birth_date"));
                pet.setCreateTime(rs.getTimestamp("create_time"));
                pets.add(pet);
            }
            return pets;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 删除宠物档案
    public boolean deletePet(Integer petId) throws SQLException {
        String sql = "DELETE FROM pet WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, petId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 更新宠物信息
    public boolean updatePet(Pet pet) throws SQLException {
        String sql = "UPDATE pet SET name=?, breed=?, age=?, gender=?, birth_date=? WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getBreed());
            pstmt.setInt(3, pet.getAge());
            pstmt.setString(4, pet.getGender());
            pstmt.setDate(5, pet.getBirthDate() != null ? new java.sql.Date(pet.getBirthDate().getTime()) : null);
            pstmt.setInt(6, pet.getId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}