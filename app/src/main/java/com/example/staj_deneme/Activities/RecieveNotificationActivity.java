package com.example.staj_deneme.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Handler;

import com.example.staj_deneme.Models.NotificationModel;
import com.example.staj_deneme.Models.NotificationResponseModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.RetrofitClient;

public class RecieveNotificationActivity extends AppCompatActivity {
    boolean opened=false;
    TextView notificationText;
    BaseAdapter adapter;
    ListView notificationsListView;
    List<String> titleList,machineIdList,descList,idList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recieve_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if(response.body()!= null && response.isSuccessful()){
                    notificationText.setText(Integer.toString(response.body().getNotificationList().size()));
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
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

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
                    convertView = LayoutInflater.from(RecieveNotificationActivity.this).inflate(R.layout.notification_layout,parent,false);
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
                handler.postDelayed(this, POLL_INTERVAL);
            }
        };

        handler.post(pollRunnable);
    }
    public void checkForNewNotifications(){
        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if(response.body()!= null && response.isSuccessful()){
                    List<NotificationModel> notifications = response.body().getNotificationList();
                    for(NotificationModel n: notifications){
                        if(!idList.contains(Integer.toString(n.getId()))){
                            idList.add(Integer.toString(n.getId()));
                            titleList.add(n.getTitle());
                            machineIdList.add(Integer.toString(n.getMachineId()));
                            descList.add(n.getDescription());
                            notificationText.setText(Integer.toString(response.body().getNotificationList().size()));
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
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

            }
        });


    }

}