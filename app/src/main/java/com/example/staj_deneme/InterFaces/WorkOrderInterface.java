package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.WorkOrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WorkOrderInterface {
    @GET("api/WorkOrder")
    Call<List<WorkOrderModel>> getWorkOrders();
    @GET("api/WorkOrder/{id}")
    Call<WorkOrderModel> getWorkOrder(@Path("id") int id);
    @PUT("api/WorkOrder")
    Call<Void> updateWorkOrder(@Body WorkOrderModel workOrderModel);
    @GET("api/WorkOrder/GetUserId")
    Call<Integer> getUserId(@Query("username") String username);
    @GET("api/WorkOrder/GetUserName")
    Call<String> getUserName(@Query("userId") int userId);
}
