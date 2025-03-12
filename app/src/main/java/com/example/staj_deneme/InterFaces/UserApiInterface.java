package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiInterface {
    @POST("api/User")
    Call<Void> addUser(@Body UserModel userModel);
}
