package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.InterFaces.WorkOrderInterface;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrderDetailActivity extends BaseActivity {
    WorkOrderModel workOrderModel;
    TextView title,desc,startDate,endDate;
    View light1,light2;
    Button workOrderAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_work_order_detail);
        title = findViewById(R.id.workOrderTitleDetail_txt);
        desc = findViewById(R.id.workOrderDescDetail_txt);
        startDate = findViewById(R.id.workOrderStartDateDetail_txt);
        endDate = findViewById(R.id.workOrderEndDateDetail_txt);
        light1 = findViewById(R.id.lightDetailView1);
        light2 = findViewById(R.id.lightDetailView2);
        workOrderAdd = findViewById(R.id.workOrderGet_btn);
        int id = getIntent().getIntExtra("workOrderId",0);
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrder(id).enqueue(new Callback<WorkOrderModel>() {
            @Override
            public void onResponse(Call<WorkOrderModel> call, Response<WorkOrderModel> response) {
                if (response.isSuccessful() && response.body()!= null){
                    workOrderModel = response.body();
                    fillPage();
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
            public void onFailure(Call<WorkOrderModel> call, Throwable t) {
                Log.e("pop",t.getMessage());
            }
        });
    }
    public void fillPage(){
        title.setText(workOrderModel.getTitle());
        desc.setText(workOrderModel.getDesc());


        if(workOrderModel.isOpened()){
            setGreen(light1);
            startDate.setText("İşe başlama tarihi: "+workOrderModel.getWorkOrderStartDate());
        }
        else{
            startDate.setText("İş birisi tarafından alınmadı");
        }
        if(workOrderModel.isClosed){
            setGreen(light2);
            endDate.setText("İş bitiş tarihi: "+workOrderModel.getWorkOrderEndDate());
        }
        else{
            endDate.setText("İş henüz sonlanmadı");
        }
    }
    public void setGreen(View view) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.GREEN);
        //shape.setSize(50, 50); isteğe bağlı: sabit boyut
        view.setBackground(shape);
    }
    public void isEmriAl(View view){
        if(workOrderModel.isOpened()){
            workOrderAdd.setClickable(false);
            workOrderAdd.setFocusable(false);
            Toast.makeText(WorkOrderDetailActivity.this,"Bu iş halihazırda alınmış durumda.",Toast.LENGTH_LONG).show();
        }
        else if(workOrderModel.isClosed()){
            workOrderAdd.setClickable(false);
            workOrderAdd.setFocusable(false);
            Toast.makeText(WorkOrderDetailActivity.this,"Bu iş tamamlanmış durumda.",Toast.LENGTH_LONG).show();
        }
        else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));//Türkiye Saat Dilimi
            String currentDate = dateFormat.format(new Date());
            workOrderModel.setWorkOrderTempStartDate(currentDate);
            workOrderModel.setOpened(true);
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
            String us_na = sharedPreferences.getString("Username","");
            WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
            workOrderInterface.getUserId(us_na).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful() && response.body()!= null){
                        workOrderModel.setUserId(response.body());
                        addInfos();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }
    public void addInfos(){
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.updateWorkOrder(workOrderModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(WorkOrderDetailActivity.this,"İş başarılı şekilde tanımlandı. ",Toast.LENGTH_LONG).show();
                    Intent sayfa = new Intent(WorkOrderDetailActivity.this, WorkOrdersActivity.class);
                    startActivity(sayfa);
                }
                else{
                    Toast.makeText(WorkOrderDetailActivity.this,"İş tanımlanırken bir hata meydana geldi: "+ response.errorBody(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WorkOrderDetailActivity.this,"İş tanımlanırken bir hata meydana geldi: "+ t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}