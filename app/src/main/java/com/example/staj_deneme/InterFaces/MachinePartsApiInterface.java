package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.MachinePartModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MachinePartsApiInterface {
    @GET("api/MachinePart")
    Call<List<MachinePartModel>> getMachinePart();
    @POST("api/MachinePart")
    Call<Void> addMachinePart(MachinePartModel machinePartModel);

}
