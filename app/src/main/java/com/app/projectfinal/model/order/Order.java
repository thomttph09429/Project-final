package com.app.projectfinal.model.order;

import java.util.List;

public class Order {
    private int total;
    private int totalPrice;
    private List<ItemOrder> itemOrders;
    private String name_store;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private  String id;
    private int status;
    private String customerName;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_store() {
        return name_store;
    }

    public void setName_store(String name_store) {
        this.name_store = name_store;
    }


    public Order(int total, int totalPrice, List<ItemOrder> itemOrders, String name_store, String id, int status, String customerName) {
        this.total = total;
        this.totalPrice = totalPrice;
        this.itemOrders = itemOrders;
        this.name_store = name_store;
        this.id = id;
        this.status = status;
        this.customerName = customerName;
    }

    public Order(int total, int totalPrice, List<ItemOrder> itemOrders, String name_store, String id, int status) {
        this.total = total;
        this.totalPrice = totalPrice;
        this.itemOrders = itemOrders;
        this.name_store = name_store;
        this.id = id;
        this.status = status;
    }

    public List<ItemOrder> getItemOrders() {
        return itemOrders;
    }

    public void setItemOrders(List<ItemOrder> itemOrders) {
        this.itemOrders = itemOrders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

}
