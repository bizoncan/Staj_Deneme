package com.example.staj_deneme;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNotificationTestActivity extends AppCompatActivity {
    EditText title,desc;
    TextView notificationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_notification_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationText = findViewById(R.id.notification_textview);
        title = findViewById(R.id.title_edt);
        desc = findViewById(R.id.desc_edt);
        notificationText = findViewById(R.id.notification_textview);
        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder machineData = new StringBuilder();
                    List<NotificationModel> notss = response.body();
                    if (notss.isEmpty()) {
                        notificationText.setText("Makine bulunamadı!");
                        return;
                    }
                    for (NotificationModel notificationModel : notss) {
                        machineData.append("ID: ").append(notificationModel.getId())
                                .append(" | İsim: ").append(notificationModel.getTitle())
                                .append(" | Açıklama: ").append(notificationModel.getDescription())
                                .append(" | Makine: ").append(notificationModel.getMachineId())
                                .append("\n\n");
                    }
                    notificationText.setText(machineData.toString());
                } else {
                    try {
                        Log.e("API ERROR", "Response Code: " + response.code() +
                                " | Message: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

            }
        });

    }
    public void bildirimEkle(View view){
        int machine_id = 2;
        NotificationModel n = new NotificationModel();
        n.setMachineId(machine_id);
        n.setTitle(title.getText().toString());
        n.setDescription(desc.getText().toString());

        Log.d("Retrofit", "Sending notification: " +
                "MachineId: " + n.getMachineId() +
                ", Title: " + n.getTitle() +
                ", Description: " + n.getDescription());

        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        Call<Void> call = recieveNotification.addNotification(n);

        Log.d("Retrofit", "Call created: " + call.request().url());
        Log.d("Retrofit", "Request method: " + call.request().method());
        Log.d("Retrofit", "Request body: " + call.request().body());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Retrofit", "Response code: " + response.code());
                Log.d("Retrofit", "Response message: " + response.message());

                if (!response.isSuccessful()) {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "No error body";
                        Log.e("Retrofit", "Error body: " + errorBody);
                        notificationText.setText("Veri ekleme başarılı");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Network Error", t);
            }
        });
    }
}