package com.example.staj_deneme.Models;

import com.google.type.DateTime;

import java.util.Date;

public class ErrorModel {
    public int id;
    public Integer machineId;
    public String errorDesc;
    public String errorDate;
    public String errorEndDate;
    public String errorType;
    public Integer machinePartId;
    public  String errorImage;
    public String errorImageType;
    public byte[] errorImageBytes;
    public ErrorModel() {
    }

    public ErrorModel(int id, Integer machineId, String errorDesc, String errorDate, String errorEndDate, String errorType, Integer machinePartId, String errorImage, String errorImageType, byte[] errorImageBytes) {
        this.id = id;
        this.machineId = machineId;
        this.errorDesc = errorDesc;
        this.errorDate = errorDate;
        this.errorEndDate = errorEndDate;
        this.errorType = errorType;
        this.machinePartId = machinePartId;
        this.errorImage = errorImage;
        this.errorImageType = errorImageType;
        this.errorImageBytes = errorImageBytes;
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

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public Integer getMachinePartId() {
        return machinePartId;
    }

    public void setMachinePartId(Integer machinePartId) {
        this.machinePartId = machinePartId;
    }

    public String getErrorImage() {
        return errorImage;
    }

    public void setErrorImage(String errorImage) {
        this.errorImage = errorImage;
    }

    public String getErrorImageType() {
        return errorImageType;
    }

    public void setErrorImageType(String errorImageType) {
        this.errorImageType = errorImageType;
    }

    public byte[] getErrorImageBytes() {
        return errorImageBytes;
    }

    public void setErrorImageBytes(byte[] errorImageBytes) {
        this.errorImageBytes = errorImageBytes;
    }

    public String getErrorEndDate() {
        return errorEndDate;
    }

    public void setErrorEndDate(String errorEndDate) {
        this.errorEndDate = errorEndDate;
    }
}
