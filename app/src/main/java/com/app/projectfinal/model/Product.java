package com.app.projectfinal.model;

public class Product {

    private String idShop, idCategory, idUnit, quantity, price, name, image, info, dateCreate, createBy, date_Update, updateBy, status;

    public String getIdShop() {
        return idShop;
    }

    public Product( String price, String name,String image) {
        this.price = price;
        this.name = name;
        this.image= image;
    }

    public void setIdShop(String idShop) {
        this.idShop = idShop;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdUnit() {
        return idUnit;
    }

    public void setIdUnit(String idUnit) {
        this.idUnit = idUnit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getDate_Update() {
        return date_Update;
    }

    public void setDate_Update(String date_Update) {
        this.date_Update = date_Update;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product(String idShop, String idCategory, String idUnit, String quantity, String price, String name, String image, String info, String dateCreate, String createBy, String date_Update, String updateBy, String status) {
        this.idShop = idShop;
        this.idCategory = idCategory;
        this.idUnit = idUnit;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.image = image;
        this.info = info;
        this.dateCreate = dateCreate;
        this.createBy = createBy;
        this.date_Update = date_Update;
        this.updateBy = updateBy;
        this.status = status;
    }
}
