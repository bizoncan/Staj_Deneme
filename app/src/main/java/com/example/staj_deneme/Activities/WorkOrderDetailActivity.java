package com.example.staj_deneme.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.staj_deneme.InterFaces.WorkOrderInterface;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrderDetailActivity extends AppCompatActivity {
    private WorkOrderModel workOrderModel;
    private TextView title, desc, startDate, endDate, workOrderUser;
    private MaterialButton workOrderAdd;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_detail);

        initializeViews();
        setupToolbar();
        loadWorkOrderData();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.workOrderTitleDetail_txt);
        desc = findViewById(R.id.workOrderDescDetail_txt);
        startDate = findViewById(R.id.workOrderStartDateDetail_txt);
        endDate = findViewById(R.id.workOrderEndDateDetail_txt);
        workOrderAdd = findViewById(R.id.workOrderGet_btn);
        workOrderUser = findViewById(R.id.workOrderUser_txt);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadWorkOrderData() {
        int id = getIntent().getIntExtra("workOrderId", 0);
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrder(id).enqueue(new Callback<WorkOrderModel>() {
            @Override
            public void onResponse(Call<WorkOrderModel> call, Response<WorkOrderModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    workOrderModel = response.body();
                    if (workOrderModel.getUserId() != null) {
                        setUserName(workOrderModel.getUserId());
                    } else {
                        workOrderUser.setText("İşi alan kullanıcı: Bu iş henüz alınmamış.");
                    }
                    fillPage();
                }
                else {
                    new AlertDialog.Builder(WorkOrderDetailActivity.this)
                            .setTitle("Hata")
                            .setMessage("İş emri kaldırıldı veya bir hata meydana geldi.")
                            .setCancelable(false)
                            .setPositiveButton("Tamam", (dialog, which) -> {
                                Intent intent = new Intent(WorkOrderDetailActivity.this, WorkOrdersActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<WorkOrderModel> call, Throwable t) {
                Log.e("pop", t.getMessage());
            }
        });
    }

    private void fillPage() {
        title.setText(workOrderModel.getTitle());
        desc.setText(workOrderModel.getDesc());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(workOrderModel.getWorkOrderStartDate());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String formattedDate = outputFormat.format(date);

        if (workOrderModel.isOpened()) {
            startDate.setText("Başlangıç: " + formattedDate);
        }

        if (workOrderModel.isClosed) {
            endDate.setText("Bitiş: " + workOrderModel.getWorkOrderEndDate());
            workOrderAdd.setEnabled(false);
            workOrderAdd.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        } else {
            endDate.setText("İş henüz sonlanmadı");
        }
    }

    private void setUserName(Integer userId) {
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getUserName(userId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    workOrderUser.setText("Kullanıcı: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("pop", t.getMessage());
            }
        });
    }

    private void setGreen(View view) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.GREEN);
        view.setBackground(shape);
    }

    public void isEmriAl(View view) {
        Intent intent = new Intent(WorkOrderDetailActivity.this, AddWorkActivity.class);
        intent.putExtra("workOrderId", workOrderModel.getId());
        startActivity(intent);
    }

    private void addInfos() {
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.updateWorkOrder(workOrderModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WorkOrderDetailActivity.this, "İş başarılı şekilde tanımlandı.", Toast.LENGTH_LONG)
                            .show();
                    Intent sayfa = new Intent(WorkOrderDetailActivity.this, WorkOrdersActivity.class);
                    startActivity(sayfa);
                    finish();
                } else {
                    Toast.makeText(WorkOrderDetailActivity.this,
                                    "İş tanımlanırken bir hata meydana geldi: " + response.errorBody(), Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WorkOrderDetailActivity.this,
                        "İş tanımlanırken bir hata meydana geldi: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}