package com.example.staj_deneme.Models;

public class WorkModel {
    public int id;
    public String title;
    public String desc;
    public String workOrderStartDate;
    public String workOrderEndDate;
    public boolean isOpened;
    public boolean isClosed;
    public Integer machineId;
    public Integer machinePartId;
    public Integer userId;
    public int workOrderId;

    public WorkModel(int id, String title, String desc, String workOrderStartDate, String workOrderEndDate, boolean isOpened, boolean isClosed, Integer machineId, Integer machinePartId, Integer userId, int workOrderId) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.workOrderStartDate = workOrderStartDate;
        this.workOrderEndDate = workOrderEndDate;
        this.isOpened = isOpened;
        this.isClosed = isClosed;
        this.machineId = machineId;
        this.machinePartId = machinePartId;
        this.userId = userId;
        this.workOrderId = workOrderId;
    }

    public WorkModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWorkOrderStartDate() {
        return workOrderStartDate;
    }

    public void setWorkOrderStartDate(String workOrderStartDate) {
        this.workOrderStartDate = workOrderStartDate;
    }

    public String getWorkOrderEndDate() {
        return workOrderEndDate;
    }

    public void setWorkOrderEndDate(String workOrderEndDate) {
        this.workOrderEndDate = workOrderEndDate;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getMachinePartId() {
        return machinePartId;
    }

    public void setMachinePartId(Integer machinePartId) {
        this.machinePartId = machinePartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
