package com.app.projectfinal.model.address;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Districts  implements Parcelable {

    private  String name ;
    private  int code;
    private List<Wards> wards;

    public Districts(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public Districts(String name, int code, List<Wards> wards) {
        this.name = name;
        this.code = code;
        this.wards = wards;
    }

    protected Districts(Parcel in) {
        name = in.readString();
        code = in.readInt();
    }

    public static final Creator<Districts> CREATOR = new Creator<Districts>() {
        @Override
        public Districts createFromParcel(Parcel in) {
            return new Districts(in);
        }

        @Override
        public Districts[] newArray(int size) {
            return new Districts[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Wards> getWards() {
        return wards;
    }

    public void setWards(List<Wards> wards) {
        this.wards = wards;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(code);
    }
}
