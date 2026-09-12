package com.pet.entity;

public class Shop {
    private Integer id;
    private Integer ownerId; // 商家用户ID
    private String name; // 店铺名称
    private String address; // 店铺地址
    private String phone; // 联系电话
    private String description; // 店铺描述

    public Shop() {}

    public Shop(Integer ownerId, String name, String address, String phone, String description) {
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
