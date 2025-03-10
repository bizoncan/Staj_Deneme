package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.NotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RecieveNotificationInterface {
    @GET("api/Bildirimler")
    Call<List<NotificationModel>> getNotification();
    @POST("api/Bildirimler")
    Call<Void> addNotification(@Body NotificationModel notificationModel);

}
