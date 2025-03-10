package com.example.staj_deneme.Models;

public class MachinePartModel {
    int Id;
    Integer machineId;
    String name;
    String desc;
    int number;

    public MachinePartModel() {
    }

    public MachinePartModel(int id, Integer machineId, String name, String desc, int number) {
        Id = id;
        this.machineId = machineId;
        this.name = name;
        this.desc = desc;
        this.number = number;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
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
}
