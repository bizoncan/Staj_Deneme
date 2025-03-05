package com.example.staj_deneme;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class ApiTestActivity extends AppCompatActivity {
    TextView machiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_api_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        machiness = findViewById(R.id.machine_TextView);


        ApiDeneme apiDeneme = RetrofitClient.getApiService();
        apiDeneme.getMachines().enqueue(new Callback<List<MachineModel>>() {
            @Override
            public void onResponse(Call<List<MachineModel>> call, Response<List<MachineModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder machineData = new StringBuilder();
                    List<MachineModel> machines = response.body();
                    if (machines.isEmpty()) {
                        machiness.setText("Makine bulunamadı!");
                        return;
                    }
                    for (MachineModel machineModel : machines) {
                        machineData.append("ID: ").append(machineModel.getId())
                                .append(" | İsim: ").append(machineModel.getName())
                                .append(" | Açıklama: ").append(machineModel.getDesc())
                                .append(" | Sayı: ").append(machineModel.getNumber())
                                .append(" | Resim URL'si").append(machineModel.getImgURL())
                                .append("\n\n");
                    }
                    machiness.setText(machineData.toString());
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
            public void onFailure(Call<List<MachineModel>> call, Throwable t) {
                Log.e("Retrofit", "URL called: " + call.request().url());
                Log.e("Retrofit", "Error message: " + t.getMessage(), t);
                machiness.setText("Bağlantı hatası: " + t.getMessage());
            }
        });
    }
}