package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ImageCollectionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageApiInterface {

    @POST("api/ImageData")
    Call<Void> addImageData(@Body List<String> imageCollectionModel);
}
