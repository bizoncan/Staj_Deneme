package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.staj_deneme.Adapter.WorkOrderAdapter;
import com.example.staj_deneme.InterFaces.WorkOrderInterface;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.Models.WorkOrderViewModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrdersActivity extends BaseActivity {
    private WorkOrderAdapter adapter;
    private List<String> machineNameList;
    private List<WorkOrderModel> workOrders;
    private RecyclerView workOrderRecyclerView;
    private Integer UserId;
    private SwipeRefreshLayout srl;
    private TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_work_orders);

        initializeViews();

        setupRecyclerView();
        setupSwipeRefresh();
        loadWorkOrders();
    }

    private void initializeViews() {
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        UserId = sp.getInt("UserId", 0);
        emptyTextView = findViewById(R.id.empty_textView);
        srl = findViewById(R.id.swipeRefreshLayout);
        workOrderRecyclerView = findViewById(R.id.workOrderListView);

        machineNameList = new ArrayList<>();
        workOrders = new ArrayList<>();
    }



    private void setupRecyclerView() {
        adapter = new WorkOrderAdapter(workOrders, machineNameList);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workOrderRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, view) -> {
            Intent intent = new Intent(WorkOrdersActivity.this, WorkOrderDetailActivity.class);
            intent.putExtra("workOrderId", workOrders.get(position).getId());
            startActivity(intent);
        });
    }

    private void setupSwipeRefresh() {
        srl.setOnRefreshListener(this::refreshWorkOrders);
    }

    private void loadWorkOrders() {
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrders().enqueue(new Callback<List<WorkOrderViewModel>>() {
            @Override
            public void onResponse(Call<List<WorkOrderViewModel>> call, Response<List<WorkOrderViewModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateWorkOrdersList(response.body());
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<WorkOrderViewModel>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(WorkOrdersActivity.this, "Bağlantı hatası oluştu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshWorkOrders() {
        workOrders.clear();
        machineNameList.clear();
        adapter.notifyDataSetChanged();
        loadWorkOrders();
        srl.setRefreshing(false);
    }

    private void updateWorkOrdersList(List<WorkOrderViewModel> tempWorkOrder) {
        for (WorkOrderViewModel w : tempWorkOrder) {
            if (w.getWorkOrderModel().getUserId() == UserId && !w.getWorkOrderModel().isClosed()) {
                workOrders.add(w.getWorkOrderModel());
                if(w.getMachineName() != null)
                    machineNameList.add(w.getMachineName());
                else{
                    machineNameList.add("Makine adı yok");
                }

            }
        }
        updateEmptyState();
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (workOrders.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
    }

    private void handleError(Response<List<WorkOrderViewModel>> response) {
        try {
            Log.e("API ERROR", "Response Code: " + response.code() +
                    " | Message: " + response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}