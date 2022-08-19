package com.app.projectfinal.model.address;

public class AddressUser {
    private  String id;
    private  String userId;
    private  String storeId;
    private  String customerName;
    private  String phone;
    private  String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public AddressUser(String id, String userId, String storeId, String customerName, String phone, String location) {
        this.id = id;
        this.userId = userId;
        this.storeId = storeId;
        this.customerName = customerName;
        this.phone = phone;
        this.location = location;
    }
}
