package com.app.projectfinal.model;

public class SliderItem {
    //set to String, if you want to add image url from internet
    private int image;

    public SliderItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
