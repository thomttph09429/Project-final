package com.app.projectfinal.model.order;

import java.util.List;

public class Order {
    private int total;
    private int totalPrice;
    private List<ItemOrder> itemOrders;

    public Order(int total, int totalPrice, List<ItemOrder> itemOrders) {
        this.total = total;
        this.totalPrice = totalPrice;
        this.itemOrders = itemOrders;
    }

    public Order(int total, int totalPrice) {
        this.total = total;
        this.totalPrice = totalPrice;
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
