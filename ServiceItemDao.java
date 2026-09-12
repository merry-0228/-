package com.pet.dao;

import com.pet.entity.ServiceItem;
import com.pet.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceItemDao {
    // 添加服务项目
    public boolean addService(ServiceItem service) throws SQLException {
        String sql = "INSERT INTO service_item(shop_id, name, type, price, description) VALUES(?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, service.getShopId());
            pstmt.setString(2, service.getName());
            pstmt.setString(3, service.getType());
            pstmt.setDouble(4, service.getPrice());
            pstmt.setString(5, service.getDescription());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 根据店铺ID查询服务
    public List<ServiceItem> findServicesByShopId(Integer shopId) throws SQLException {
        String sql = "SELECT * FROM service_item WHERE shop_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ServiceItem> services = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, shopId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServiceItem service = new ServiceItem();
                service.setId(rs.getInt("id"));
                service.setShopId(rs.getInt("shop_id"));
                service.setName(rs.getString("name"));
                service.setType(rs.getString("type"));
                service.setPrice(rs.getDouble("price"));
                service.setDescription(rs.getString("description"));
                services.add(service);
            }
            return services;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 查询所有服务
    public List<ServiceItem> findAllServices() throws SQLException {
        String sql = "SELECT * FROM service_item";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ServiceItem> services = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServiceItem service = new ServiceItem();
                service.setId(rs.getInt("id"));
                service.setShopId(rs.getInt("shop_id"));
                service.setName(rs.getString("name"));
                service.setType(rs.getString("type"));
                service.setPrice(rs.getDouble("price"));
                service.setDescription(rs.getString("description"));
                services.add(service);
            }
            return services;
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    // 删除服务
    public boolean deleteService(Integer serviceId) throws SQLException {
        String sql = "DELETE FROM service_item WHERE id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, serviceId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}
