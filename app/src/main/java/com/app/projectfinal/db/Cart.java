package com.app.projectfinal.db;

import static com.app.projectfinal.utils.Constant.TABLE_NAME;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = TABLE_NAME)
public class Cart implements Parcelable {
    @PrimaryKey
    @NonNull
    public String idProduct;
    public String idShop;
    public String nameProduct;
    public String amount;
    public String price;
    public String imageOfProduct;
    public  String quantity;
    public  String description;
    public  String categoryName;
    public  String nameShop;
    public  String unit;


    protected Cart(Parcel in) {
        idProduct = in.readString();
        idShop = in.readString();
        nameProduct = in.readString();
        amount = in.readString();
        price = in.readString();
        imageOfProduct = in.readString();
        quantity = in.readString();
        description = in.readString();
        categoryName = in.readString();
        nameShop = in.readString();
        unit = in.readString();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }




    public Cart(@NonNull String idProduct, String idShop, String nameProduct, String amount, String price, String imageOfProduct, String nameShop, String unit, String quantity, String description, String categoryName) {
        this.idProduct = idProduct;
        this.idShop = idShop;
        this.nameProduct = nameProduct;
        this.amount = amount;
        this.price = price;
        this.imageOfProduct = imageOfProduct;
        this.nameShop = nameShop;
        this.unit= unit;
        this.quantity = quantity;
        this.description= description;
        this.categoryName= categoryName;

    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idProduct);
        parcel.writeString(idShop);
        parcel.writeString(nameProduct);
        parcel.writeString(amount);
        parcel.writeString(price);
        parcel.writeString(imageOfProduct);
        parcel.writeString(quantity);
        parcel.writeString(description);
        parcel.writeString(categoryName);
        parcel.writeString(nameShop);
        parcel.writeString(unit);
    }
}
