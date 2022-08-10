package com.app.projectfinal.db;

import static com.app.projectfinal.utils.Constant.TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = TABLE_NAME)
public class Card {
    @PrimaryKey
    private String idProduct;
    private String idShop;
    private String nameProduct;
    private String amount;
    private String price;
    private String imageOfProduct;

}
