package com.example.coosinmanager.model;

public class NhanVien {

    String name;
    String type;
    String phone;
    String ID;

    public NhanVien(String name, String type, String phone, String ID) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
