package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.WorkModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WorkInterface {
    @POST("api/Work")
    Call<Void> addWork(@Body WorkModel workModel);
    @GET("api/Work")
    Call<List<WorkModel>> getWorks();
}
