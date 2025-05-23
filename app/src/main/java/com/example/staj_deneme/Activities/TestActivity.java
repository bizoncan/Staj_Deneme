package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;

import com.example.staj_deneme.Adapter.MachineAdapter;
import com.example.staj_deneme.InterFaces.MachineApiInterface;
import com.example.staj_deneme.Models.MachineModel;
import com.example.staj_deneme.Models.NotificationModel;
import com.example.staj_deneme.Models.NotificationResponseModel;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.RetrofitClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.skydoves.transformationlayout.TransformationLayout;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends BaseActivity implements BaseActivity.NotificationButtonListener {
    boolean opened=true;
    ImageButton notificationButton;
    TransformationLayout transformationLayout;
    TextView notificationText;
    //BaseAdapter adapter;
    MachineAdapter m_adapter;
    ListView notificationsListView,machinesListView;
    List<String> titleList,machineIdList,descList,idList,machinePartIdList,typeList;
    List<MachineModel> machineModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        notificationButton = findViewById(R.id.notification_btn);
        findViewById(R.id.QRTara_btn).setOnClickListener(v -> {
            new IntentIntegrator(TestActivity.this).initiateScan();
        });
        transformationLayout = findViewById(R.id.transition_layout);
        setNotificationButtonListener(this);
       /* RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
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
        });*/


        //notificationsListView = findViewById(R.id.notificationsbase_listview);
        machineModelList = new ArrayList<>();
        machinesListView = findViewById(R.id.machines_listview);
        m_adapter = new MachineAdapter(this,machineModelList );
        MachineApiInterface machineApiInterface = RetrofitClient.getApiServiceMachine();
        machineApiInterface.getAll().enqueue(new Callback<List<MachineModel>>() {
            @Override
            public void onResponse(Call<List<MachineModel>> call, Response<List<MachineModel>> response) {
                if(response.body() != null && response.isSuccessful()){
                    for (MachineModel m: response.body()){

                        machineModelList.add(m);
                    }
                    m_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MachineModel>> call, Throwable t) {

            }
        });
        machinesListView.setAdapter(m_adapter);
        titleList = new ArrayList<>();
        machineIdList = new ArrayList<>();
        descList = new ArrayList<>();
        idList= new ArrayList<>();
        machinePartIdList = new ArrayList<>();
        typeList = new ArrayList<>();
        /*adapter = new BaseAdapter() {
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
                    convertView = LayoutInflater.from(TestActivity.this).inflate(R.layout.notification_layout,parent,false);
                }

                TextView title_text = convertView.findViewById(R.id.title_textview);
                TextView machineId_text = convertView.findViewById(R.id.machineId_textview);
                TextView machinePartId_text= convertView.findViewById(R.id.machinePartId_textview);
                TextView desc_text = convertView.findViewById(R.id.description_textview);
                TextView type_text = convertView.findViewById(R.id.type_txt);

                title_text.setText(titleList.get(position));
                machineId_text.setText(machineIdList.get(position));
                machinePartId_text.setText(machinePartIdList.get(position));
                desc_text.setText(descList.get(position));
                type_text.setText(typeList.get(position));
                return convertView;
            }
        };
        notificationText=findViewById(R.id.notification_textview);
        notificationsListView.setAdapter(adapter);*/
       /* notificationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(TestActivity.this, NotificationAddErrorActivity.class);
                sayfa.putExtra("machineID",machineIdList.get(position));
                sayfa.putExtra("machinePartID",machinePartIdList.get(position));
                sayfa.putExtra("notificationId",idList.get(position));
                startActivity(sayfa);

            }
        });*/
        machinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(TestActivity.this,MachineDetailActivity.class);
                sayfa.putExtra("machineId",Integer.toString(machineModelList.get(position).getId()));
                startActivity(sayfa);
            }
        });
        //startDatabasePolling();

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
        //checkForNewNotifications();

        if(opened){
            transformationLayout.startTransform();

            opened=false;
        }
        else{
            transformationLayout.finishTransform();
            opened=true;
        }

    }

    /*private void startDatabasePolling() {
        final int POLL_INTERVAL = 10000; // 10 saniye

        Handler handler = new Handler();
        Runnable pollRunnable = new Runnable() {
            @Override
            public void run() {
                checkForNewNotifications();
                handler.postDelayed(this, POLL_INTERVAL); // Tekrar çalıştır
            }
        };

        handler.post(pollRunnable); // Başlat
    }*/
    /*public void checkForNewNotifications(){
        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if(response.body()!= null && response.isSuccessful()){
                    List<NotificationModel> notifications = response.body().getNotificationList();
                    List<WorkOrderModel> works = response.body().getWorkNotificationList();
                    for(NotificationModel n: notifications){
                        if(!idList.contains(Integer.toString(n.getId()))){
                            typeList.add("Arıza");
                            idList.add(Integer.toString(n.getId()));
                            titleList.add(n.getTitle());
                            if(n.getMachineId()== null)machineIdList.add(null);
                            else machineIdList.add(Integer.toString(n.getMachineId()));
                            if(n.getMachinePartId()==null)machinePartIdList.add(null) ;
                            else machinePartIdList.add(Integer.toString(n.getMachinePartId()));
                            descList.add(n.getDescription());
                            not_size = response.body().getNotificationList().size() +
                                    response.body().getWorkNotificationList().size();
                            notificationText.setText(Integer.toString(not_size));
                        }

                    }
                    for(WorkOrderModel w: works){
                        if(!idList.contains(Integer.toString(w.getId()))){
                            typeList.add("İş Emri");
                            idList.add(Integer.toString(w.getId()));
                            titleList.add(w.getTitle());
                            if(w.getMachineId()== null)machineIdList.add(null);
                            else machineIdList.add(Integer.toString(w.getMachineId()));
                            if(w.getMachinePartId()==null)machinePartIdList.add(null) ;
                            else machinePartIdList.add(Integer.toString(w.getMachinePartId()));
                            descList.add(w.getDesc());
                            not_size = response.body().getNotificationList().size() +
                                    response.body().getWorkNotificationList().size();
                            notificationText.setText(Integer.toString(not_size));
                        }

                    }
                    adadpter.notifyDataSetChanged();
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


    }*/

    @Override
    public void onNotificationButtonClicked() {
        bildirim_yukle(null);
    }
}