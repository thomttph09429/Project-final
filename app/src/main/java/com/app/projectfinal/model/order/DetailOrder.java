package com.app.projectfinal.model.order;

import java.util.List;

public class DetailOrder {
    private  String id,code;
    private String userId;
    private int status;
    private String note;
    private String createdAt,createdBy,updatedAt,updatedBy,userName, phone_store;
    private String addressId;
    private String customerName;
    private int customerPhone;
    private String location;
    private List<ItemOrder> products;
    private int totalPrice;
    private String store_id;
    private String image_store,name_store;

    public DetailOrder(String id, String code, String userId, int status, String note, String createdAt, String createdBy, String updatedAt, String updatedBy, String userName, String phone_store, String addressId, String customerName, int customerPhone, String location, List<ItemOrder> products, int totalPrice, String store_id, String image_store, String name_store) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.userName = userName;
        this.phone_store = phone_store;
        this.addressId = addressId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.location = location;
        this.products = products;
        this.totalPrice = totalPrice;
        this.store_id = store_id;
        this.image_store = image_store;
        this.name_store = name_store;
    }

    public String getPhone_store() {
        return phone_store;
    }

    public void setPhone_store(String phone_store) {
        this.phone_store = phone_store;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(int customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ItemOrder> getProducts() {
        return products;
    }

    public void setProducts(List<ItemOrder> products) {
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getImage_store() {
        return image_store;
    }

    public void setImage_store(String image_store) {
        this.image_store = image_store;
    }

    public String getName_store() {
        return name_store;
    }

    public void setName_store(String name_store) {
        this.name_store = name_store;
    }



}
