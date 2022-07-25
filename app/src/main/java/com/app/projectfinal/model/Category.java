package com.app.projectfinal.model;

public class Category {

    private String nameCategory, imageCategory;

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    public Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
