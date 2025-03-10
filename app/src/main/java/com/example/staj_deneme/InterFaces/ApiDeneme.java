package com.example.staj_deneme.InterFaces;


import com.example.staj_deneme.Models.MachineModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiDeneme {
    @GET("api/AndroidDeneme")
    Call<List<MachineModel>> getMachines();
}
