package com.app.projectfinal.model;

public class UserDetail {
    private String id;
    private String userName;
    private String phone;

    public UserDetail(String id, String userName, String phone, String email, String image1, String image2, String image3, String name, String dateOfBirth, String createdAt, String updatedAt, String storeId, int role, int isOnline, int status) {
        this.id = id;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.storeId = storeId;
        this.role = role;
        this.isOnline = isOnline;
        this.status = status;
    }

    private String email;
    private String image1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String image2;
    private String image3;
    private String name;
    private String dateOfBirth;
    private String createdAt;
    private String updatedAt;
    private String storeId;
    private int role, isOnline, status;
}
