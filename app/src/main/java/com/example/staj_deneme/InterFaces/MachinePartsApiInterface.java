package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.MachinePartModel;

import java.util.List;

import javax.crypto.Mac;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MachinePartsApiInterface {
    @GET("api/MachinePart")
    Call<List<MachinePartModel>> getMachinePart(@Query("id")int id);
    @POST("api/MachinePart")
    Call<Void> addMachinePart(@Body MachinePartModel machinePartModel);

}
