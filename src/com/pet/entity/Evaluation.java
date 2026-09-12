package com.pet.entity;

import java.util.Date;

public class Evaluation {
    private Integer id;
    private Integer appointmentId; // 关联预约ID
    private Integer userId; // 用户ID
    private Integer shopId; // 店铺ID
    private String content; // 评价内容
    private Integer score; // 评分1-5
    private Date createTime; // 评价时间

    public Evaluation() {}

    public Evaluation(Integer appointmentId, Integer userId, Integer shopId, String content, Integer score) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.shopId = shopId;
        this.content = content;
        this.score = score;
        this.createTime = new Date();
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
