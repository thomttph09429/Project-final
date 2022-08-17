package com.app.projectfinal.model.address;

import java.util.List;

public class Province {

    private  String name;
    private  int code;
    private  List<Districts> districts;
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

    public List<Districts> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Districts> districts) {
        this.districts = districts;
    }

    public Province(String name, int code, List<Districts> districts) {
        this.name = name;
        this.code = code;
        this.districts = districts;
    }

}
