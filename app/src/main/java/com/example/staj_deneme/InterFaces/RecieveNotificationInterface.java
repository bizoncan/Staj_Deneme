package com.example.staj_deneme.InterFaces;

import com.example.staj_deneme.Models.NotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RecieveNotificationInterface {
    @GET("api/Bildirimler")
    Call<List<NotificationModel>> getNotification();
    @POST("api/Bildirimler")
    Call<Void> addNotification(@Body NotificationModel notificationModel);
    @DELETE("api/Bildirimler/{id}")
    Call<Void> deleteNotification(@Path("id")int id);
}
