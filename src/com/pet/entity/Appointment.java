package com.pet.entity;

import java.util.Date;

public class Appointment {
    private Integer id;
    private Integer userId; // 用户ID
    private Integer shopId; // 店铺ID
    private Integer serviceId; // 服务ID
    private Date appointmentTime; // 预约时间
    // 状态：pending(待接单), accepted(已接单), completed(已完成), cancelled(已取消)
    private String status;
    // 支付状态：unpaid(未支付), paid(已支付)
    private String payStatus;
    private Double price; // 预约价格
    private Date createTime; // 创建时间

    public Appointment() {}

    public Appointment(Integer userId, Integer shopId, Integer serviceId, Date appointmentTime, Double price) {
        this.userId = userId;
        this.shopId = shopId;
        this.serviceId = serviceId;
        this.appointmentTime = appointmentTime;
        this.price = price;
        this.status = "pending";
        this.payStatus = "unpaid";
        this.createTime = new Date();
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public Integer getServiceId() { return serviceId; }
    public void setServiceId(Integer serviceId) { this.serviceId = serviceId; }
    public Date getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(Date appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPayStatus() { return payStatus; }
    public void setPayStatus(String payStatus) { this.payStatus = payStatus; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
