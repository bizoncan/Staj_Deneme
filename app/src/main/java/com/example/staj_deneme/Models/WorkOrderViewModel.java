package com.example.staj_deneme.Models;

public class WorkOrderViewModel {
    WorkOrderModel workOrderModel;
    String machineName;

    public WorkOrderViewModel(WorkOrderModel workOrderModel, String machineName) {
        this.workOrderModel = workOrderModel;
        this.machineName = machineName;
    }

    public WorkOrderModel getWorkOrderModel() {
        return workOrderModel;
    }

    public void setWorkOrderModel(WorkOrderModel workOrderModel) {
        this.workOrderModel = workOrderModel;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
}
