package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ResponseModel;
import com.example.staj_deneme.Models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApiInterface {
    @POST("api/User")
    Call<Void> addUser(@Body UserModel userModel);
    @GET("api/User")
    Call<Boolean> checkUser(@Query("username") String username,@Query("email") String email);
    @GET("api/User/GetUsers")
    Call<ResponseModel> getUsers(@Query("username") String username, @Query("password") String password);

}
