package com.example.staj_deneme;

public class MachineModel {
    private int id;
    private String name;
    private String desc;
    private int number;
    private String imgURL;

    public MachineModel() {
    }

    public MachineModel(String name, String desc, int number, String imgURL) {

        this.name = name;
        this.desc = desc;
        this.number = number;
        this.imgURL = imgURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
