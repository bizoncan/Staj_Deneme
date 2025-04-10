package com.example.staj_deneme.Models;

public class WorkOrderModel {
    public int id;
    public String title;
    public String desc;
    public boolean isClosed;
    public boolean isOpened;
    public String workOrderStartDate;
    public String workOrderEndDate;
    public Integer machineId;
    public Integer machinePartId;
    public Integer userId;

    public WorkOrderModel(int id, String title, String desc, boolean isClosed, boolean isOpened, String workOrderStartDate, String workOrderEndDate, Integer machineId, Integer machinePartId, Integer userId) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.isClosed = isClosed;
        this.isOpened = isOpened;
        this.workOrderStartDate = workOrderStartDate;
        this.workOrderEndDate = workOrderEndDate;
        this.machineId = machineId;
        this.machinePartId = machinePartId;
        this.userId = userId;
    }

    public WorkOrderModel() {
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

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getWorkOrderStartDate() {
        return workOrderStartDate;
    }

    public void setWorkOrderStartDate(String workOrderStarDate) {
        this.workOrderStartDate = workOrderStarDate;
    }

    public String getWorkOrderEndDate() {
        return workOrderEndDate;
    }

    public void setWorkOrderEndDate(String getWorkOrderStarDate) {
        this.workOrderEndDate = getWorkOrderStarDate;
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
}
