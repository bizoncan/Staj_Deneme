package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ErrorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ErrorInterface {
    @GET("api/Error")
    Call<List<ErrorModel>> getAll();
    @POST("api/Error")
    @Headers({"Content-Type: application/json"})
    Call<Void> add(@Body ErrorModel errorModel);
}
