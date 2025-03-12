package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ErrorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ErrorInterface {
    @GET("api/Error")
    Call<List<ErrorModel>> getAll();
    @GET("api/Error/Machine/{machineId}")
    Call<List<ErrorModel>> getByMachineId(@Path("machineId")int id);
    @GET("api/Error/MachinePart/{machinePartId}")
    Call<List<ErrorModel>> getByMachinePartId(@Path("machinePartId")int id);
    @POST("api/Error")
    @Headers({"Content-Type: application/json"})
    Call<Void> add(@Body ErrorModel errorModel);

}
