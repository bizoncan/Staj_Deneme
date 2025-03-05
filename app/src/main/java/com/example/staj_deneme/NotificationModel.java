package com.example.staj_deneme;

public class NotificationModel {
    private int id;
    private int machineId;
    private String title;
    private String description;

    public NotificationModel() {
    }

    public NotificationModel(int id, int machineId, String title, String description) {
        this.id = id;
        this.machineId = machineId;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
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
}
