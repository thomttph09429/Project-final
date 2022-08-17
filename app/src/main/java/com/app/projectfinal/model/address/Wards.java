package com.app.projectfinal.model.address;

public class Wards {
    private  String name;
    private  int code;

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

    public Wards(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
