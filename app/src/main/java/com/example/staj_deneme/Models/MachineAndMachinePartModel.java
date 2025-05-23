package com.example.staj_deneme.Models;

import java.util.List;

public class MachineAndMachinePartModel {
    public MachineAndMachinePartModel(List<MachineModel> machineModels, List<MachinePartModel> machinePartModels) {
        this.machineModels = machineModels;
        this.machinePartModels = machinePartModels;
    }

    public MachineAndMachinePartModel() {
    }

    List<MachineModel> machineModels;
    List<MachinePartModel> machinePartModels;

    public List<MachineModel> getMachineModels() {
        return machineModels;
    }

    public void setMachineModels(List<MachineModel> machineModels) {
        this.machineModels = machineModels;
    }

    public List<MachinePartModel> getMachinePartModels() {
        return machinePartModels;
    }

    public void setMachinePartModels(List<MachinePartModel> machinePartModels) {
        this.machinePartModels = machinePartModels;
    }
}
