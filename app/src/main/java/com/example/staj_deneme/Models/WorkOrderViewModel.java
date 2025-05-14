package com.example.staj_deneme.Models;

import javax.crypto.Mac;

public class WorkOrderViewModel {
    WorkOrderModel workOrderModel;
    String machineName;
    MachineModel machineModel;
    public WorkOrderViewModel(WorkOrderModel workOrderModel, String machineName, MachineModel machineModel) {
        this.workOrderModel = workOrderModel;
        this.machineName = machineName;
        this.machineModel = machineModel;
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

    public MachineModel getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(MachineModel machineModel) {
        this.machineModel = machineModel;
    }
}
