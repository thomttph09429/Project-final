package com.app.projectfinal.db;

import static com.app.projectfinal.utils.Constant.TABLE_NAME;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = TABLE_NAME)
public class Cart {
    @PrimaryKey
    @NonNull
    public String idProduct;
    public String idShop;
    public String nameProduct;
    public String amount;
    public String price;
    public String imageOfProduct;
    public  String nameShop;

    public Cart(@NonNull String idProduct, String idShop, String nameProduct, String amount, String price, String imageOfProduct, String nameShop) {
        this.idProduct = idProduct;
        this.idShop = idShop;
        this.nameProduct = nameProduct;
        this.amount = amount;
        this.price = price;
        this.imageOfProduct = imageOfProduct;
        this.nameShop = nameShop;
    }

    public String getNameShop() {
        return nameShop;
    }

    public void setNameShop(String nameShop) {
        this.nameShop = nameShop;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdShop() {
        return idShop;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageOfProduct() {
        return imageOfProduct;
    }

    public void setImageOfProduct(String imageOfProduct) {
        this.imageOfProduct = imageOfProduct;
    }
}
