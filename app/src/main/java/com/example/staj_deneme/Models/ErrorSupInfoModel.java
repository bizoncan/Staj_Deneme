package com.example.staj_deneme.Models;

public class ErrorSupInfoModel {
    int id;
    String machineName;
    String machinePartName;
    Integer machinePartId;
    Integer machineId;
    String userName;
    String startDate;
    String endDate;
    String errorImage;
    String errorType;
    String errorDesc;

    public int getId() {
        return id;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getMachinePartName() {
        return machinePartName;
    }

    public Integer getMachinePartId() {
        return machinePartId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public String getUserName() {
        return userName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getErrorImage() {
        return errorImage;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
