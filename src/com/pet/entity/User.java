package com.pet.entity;

public class User {
    private Integer id;
    private String phone;
    private String password;
    private String nickname;
    private String address;
    // 角色：admin(管理员), shop(商家), user(普通用户), vip(VIP用户)
    private String role;

    public User() {}

    public User(String phone, String password, String nickname, String address, String role) {
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.role = role;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
