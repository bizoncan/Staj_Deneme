package com.example.staj_deneme;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiDeneme {
    @GET("api/AndroidDeneme")
    Call<List<MachineModel>> getMachines();
}
