package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.Models.NotificationModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.RetrofitClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrScanActivity extends AppCompatActivity {
    boolean opened=false;
    TextView notificationText;
    BaseAdapter adapter;
    ListView notificationsListView;
    List<String> titleList,machineIdList,descList,idList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_scan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.QRTara_btn).setOnClickListener(v -> {
            new IntentIntegrator(QrScanActivity.this).initiateScan();
        });

        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if(response.body()!= null && response.isSuccessful()){
                    notificationText.setText(Integer.toString(response.body().size()));
                }
                else{
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
        notificationsListView = findViewById(R.id.notificationsbase_listview);
        titleList = new ArrayList<>();
        machineIdList = new ArrayList<>();
        descList = new ArrayList<>();
        idList= new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return titleList.size();
            }

            @Override
            public Object getItem(int position) {
                return titleList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(QrScanActivity.this).inflate(R.layout.notification_layout,parent,false);
                }
                TextView title_text = convertView.findViewById(R.id.title_textview);
                TextView machineId_text = convertView.findViewById(R.id.machineId_textview);
                TextView desc_text = convertView.findViewById(R.id.description_textview);

                title_text.setText(titleList.get(position));
                machineId_text.setText(machineIdList.get(position));
                desc_text.setText(descList.get(position));
                return convertView;
            }
        };
        notificationText=findViewById(R.id.notification_textview);
        notificationsListView.setAdapter(adapter);
        startDatabasePolling();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"QR kod okunamadı",Toast.LENGTH_LONG).show();

            }
            else{
                String qrContent= result.getContents();

                Toast.makeText(this, qrContent, Toast.LENGTH_LONG);
                Intent sayfa= new Intent(getApplicationContext(), MainActivity.class);
                sayfa.putExtra("QR",qrContent);
                startActivity(sayfa);
            }
        }
    }
    public void bildirim_yukle(View view){
        checkForNewNotifications();

        if(opened){
            notificationsListView.setVisibility(View.GONE);
            opened=false;
        }
        else{
            notificationsListView.setVisibility(View.VISIBLE);
            opened=true;
        }

    }
    private void startDatabasePolling() {
        final int POLL_INTERVAL = 2500;

        Handler handler = new Handler();
        Runnable pollRunnable = new Runnable() {
            @Override
            public void run() {
                checkForNewNotifications();
                handler.postDelayed(this, POLL_INTERVAL); // Tekrar çalıştır
            }
        };

        handler.post(pollRunnable); // Başlat
    }
    public void checkForNewNotifications(){
        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if(response.body()!= null && response.isSuccessful()){
                    List<NotificationModel> notifications = response.body();
                    for(NotificationModel n: notifications){
                        if(!idList.contains(Integer.toString(n.getId()))){
                            idList.add(Integer.toString(n.getId()));
                            titleList.add(n.getTitle());
                            machineIdList.add(Integer.toString(n.getMachineId()));
                            descList.add(n.getDescription());
                            notificationText.setText(Integer.toString(response.body().size()));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
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
}