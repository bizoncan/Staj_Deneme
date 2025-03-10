package com.example.staj_deneme.Models;

public class NotificationModel {
    private int id;
    private Integer machineId;
    private String title;
    private String description;
    private Integer machinePartId;
    public NotificationModel() {
    }

    public NotificationModel(int id, Integer machineId, String title, String description, Integer machinePartId) {
        this.id = id;
        this.machineId = machineId;
        this.title = title;
        this.description = description;
        this.machinePartId = machinePartId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMachinePartId() {
        return machinePartId;
    }

    public void setMachinePartId(Integer machinePartId) {
        this.machinePartId = machinePartId;
    }
}
