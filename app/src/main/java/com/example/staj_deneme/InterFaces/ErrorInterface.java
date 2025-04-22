package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.ErrorIdModel;
import com.example.staj_deneme.Models.ErrorInfoModel;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.Models.ErrorResponseModel;
import com.example.staj_deneme.Models.ErrorResponseNoListModel;
import com.example.staj_deneme.Models.ErrorSupInfoModel;
import com.example.staj_deneme.Models.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ErrorInterface {
    @GET("api/Error")
    Call<ErrorResponseModel> getAll();
    @GET("api/Error/Machine/{machineId}")
    Call<ErrorResponseModel> getByMachineId(@Path("machineId")int id);
    @GET("api/Error/MachinePart/{machinePartId}")
    Call<ErrorResponseModel> getByMachinePartId(@Path("machinePartId")int id);
    @POST("api/Error")
    @Headers({"Content-Type: application/json"})
    Call<Void> add(@Body ErrorModel errorModel);
    @GET("api/Error/GetUserId")
    Call<Integer> getUserId(@Query("username") String username);
    @GET("api/Error/GetIdName")
    Call<List<ErrorIdModel>> getNames();
    @GET("api/Error/GetPartIdName")
    Call<List<ErrorIdModel>> getPartNames(@Query("id")int id);
    @GET("api/Error/GetInfoNames")
    Call<List<ErrorInfoModel>> getErrorInfos();
    @GET("api/Error/FilterListView")
        Call<List<ErrorSupInfoModel>> filterListView(@Query("s1") String s1,
                                              @Query("s2") String s2,
                                              @Query("s3") String s3,
                                              @Query("s4") String s4,
                                              @Query("s5") String s5);
    @GET("api/Error/{id}")
    Call<ErrorResponseNoListModel> getById(@Path("id")int id);
    @GET("api/Error/GetMachineNameAndPart/{id}")
    Call<ErrorInfoModel> getMachineNameAndPart(@Path("id") int id);
    @GET("api/Error/GetErrorSupModel")
    Call<List<ErrorSupInfoModel>> getErrorSupModel();
    @GET("api/Error/GetErrorSupModelByMachine/{machineId}")
    Call<List<ErrorSupInfoModel>> getErrorSupModelByMachine(@Path("machineId")int machineId);
    @GET("api/Error/GetErrorSupModelByMachinePart/{machinePartId}")
    Call<List<ErrorSupInfoModel>> getErrorSupModelByMachinePart(@Path("machinePartId")int machinePartId);
}
