package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.WorkModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WorkInterface {
    @POST("api/Work")
    Call<Integer> addWork(@Body WorkModel workModel);
    @GET("api/Work")
    Call<List<WorkModel>> getWorks();
    @GET("api/Work/GetWork")
    Call<WorkModel> getWork(@Query("id") int id);
    @POST("api/Work/UpdateWork")
    Call<Integer> updateWork(@Body WorkModel workModel);
}
