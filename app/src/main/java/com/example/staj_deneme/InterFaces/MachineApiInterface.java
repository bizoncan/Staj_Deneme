package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.MachineModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MachineApiInterface {
    @GET("api/Machine")
    Call<List<MachineModel>> getAll();
    @POST("api/Machine")
    Call<Void> add(@Body MachineModel machineModel);

}
