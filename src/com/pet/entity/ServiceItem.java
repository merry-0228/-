package com.pet.entity;

public class ServiceItem {
    private Integer id;
    private Integer shopId; // 所属店铺ID
    private String name; // 服务名称
    private String type; // 服务类型：洗护、美容、寄养、医疗
    private Double price; // 价格
    private String description; // 服务描述

    public ServiceItem() {}

    public ServiceItem(Integer shopId, String name, String type, Double price, String description) {
        this.shopId = shopId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
