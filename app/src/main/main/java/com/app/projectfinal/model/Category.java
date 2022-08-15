package com.app.projectfinal.model;

public class Category {

    private String nameCategory, imageCategory, idCategory;

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

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



    public Category(String nameCategory, String idCategory) {
        this.nameCategory = nameCategory;
        this.idCategory = idCategory;
    }
}
