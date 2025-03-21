package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ImageCollectionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ImageApiInterface {

    @POST("api/ImageData")
    Call<Void> addImageData(@Body List<String> imageCollectionModel);
    @GET("api/ImageData/{errorId}")
    Call<List<String>> getImages(@Path("errorId") int errorId);
}
