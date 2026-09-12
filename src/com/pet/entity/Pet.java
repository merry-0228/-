package com.pet.entity;

import java.util.Date;
import java.util.Calendar;

public class Pet {
    private Integer id;
    private Integer userId;
    private String name;
    private String breed;
    private Integer age;
    private String gender;
    private Date birthDate;
    private Date createTime;

    public Pet() {}

    public Pet(Integer userId, String name, String breed, Integer age, String gender) {
        this.userId = userId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.createTime = new Date();
    }

    // 计算年龄显示（x岁x月）
    public String getAgeDisplay() {
        if (birthDate == null) {
            return age != null ? age + "岁" : "未知";
        }
        Calendar now = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);

        int years = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        int months = now.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
        if (months < 0) {
            years--;
            months += 12;
        }
        if (years < 0) years = 0;
        if (years == 0 && months == 0) return "1个月";
        if (years == 0) return months + "个月";
        if (months == 0) return years + "岁";
        return years + "岁" + months + "个月";
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        // 自动计算年龄
        if (birthDate != null) {
            Calendar now = Calendar.getInstance();
            Calendar birth = Calendar.getInstance();
            birth.setTime(birthDate);
            this.age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            if (now.get(Calendar.MONTH) < birth.get(Calendar.MONTH) ||
                    (now.get(Calendar.MONTH) == birth.get(Calendar.MONTH) &&
                            now.get(Calendar.DAY_OF_MONTH) < birth.get(Calendar.DAY_OF_MONTH))) {
                this.age--;
            }
            if (this.age < 0) this.age = 0;
        }
    }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}