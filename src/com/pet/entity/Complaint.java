package com.pet.entity;

import java.util.Date;

public class Complaint {
    private Integer id;
    private Integer userId; // 用户ID
    private Integer shopId; // 店铺ID
    private String content; // 投诉内容
    // 状态：unhandled(未处理), handled(已处理)
    private String status;
    private Date createTime; // 投诉时间
    private Date handleTime; // 处理时间
    private String handleResult; // 处理结果

    public Complaint() {}

    public Complaint(Integer userId, Integer shopId, String content) {
        this.userId = userId;
        this.shopId = shopId;
        this.content = content;
        this.status = "unhandled";
        this.createTime = new Date();
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
}
