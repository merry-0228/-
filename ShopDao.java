package com.pet.dao;

import com.pet.entity.Shop;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {
    // 添加店铺
    public boolean addShop(Shop shop) throws SQLException {
        String sql = "INSERT INTO shop(owner_id, name, address, phone, description) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shop.getOwnerId());
            pstmt.setString(2, shop.getName());
            pstmt.setString(3, shop.getAddress());
            pstmt.setString(4, shop.getPhone());
            pstmt.setString(5, shop.getDescription());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 查询所有店铺
    public List<Shop> findAllShops() throws SQLException {
        String sql = "SELECT * FROM shop";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Shop> shops = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Shop shop = new Shop();
                shop.setId(rs.getInt("id"));
                shop.setOwnerId(rs.getInt("owner_id"));
                shop.setName(rs.getString("name"));
                shop.setAddress(rs.getString("address"));
                shop.setPhone(rs.getString("phone"));
                shop.setDescription(rs.getString("description"));
                shops.add(shop);
            }
            return shops;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 根据商家ID查询店铺
    public Shop findShopByOwnerId(Integer ownerId) throws SQLException {
        String sql = "SELECT * FROM shop WHERE owner_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ownerId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Shop shop = new Shop();
                shop.setId(rs.getInt("id"));
                shop.setOwnerId(rs.getInt("owner_id"));
                shop.setName(rs.getString("name"));
                shop.setAddress(rs.getString("address"));
                shop.setPhone(rs.getString("phone"));
                shop.setDescription(rs.getString("description"));
                return shop;
            }
            return null;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 删除店铺
    public boolean deleteShop(Integer shopId) throws SQLException {
        String sql = "DELETE FROM shop WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shopId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
