package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrdersActivity extends BaseActivity {
    BaseAdapter adapter;
    List<WorkOrderModel> workOrders;
    ListView workOrderListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_work_orders);
        workOrderListView = findViewById(R.id.workOrderListView);
        workOrders = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return workOrders.size();
            }

            @Override
            public Object getItem(int position) {
                return workOrders.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView=LayoutInflater.from(WorkOrdersActivity.this).inflate(R.layout.work_order_item_layout,parent,false);
                }
                TextView title = convertView.findViewById(R.id.workOrderTitle_txt);
                TextView desc = convertView.findViewById(R.id.workOrderDesc_txt);
                TextView startDate = convertView.findViewById(R.id.workOrderStartDate_txt);
                TextView endDate = convertView.findViewById(R.id.workOrderEndDate_txt);
                View light1 = convertView.findViewById(R.id.lightView1);
                View light2 = convertView.findViewById(R.id.lightView2);

                light1.setBackgroundResource(R.drawable.light_circle);  // veya varsayılan renk
                light2.setBackgroundResource(R.drawable.light_circle);

                title.setText(workOrders.get(position).getTitle());
                desc.setText(workOrders.get(position).getDesc());
                startDate.setText(workOrders.get(position).getWorkOrderStartDate());

                if(workOrders.get(position).isOpened()){
                    setGreen(light1);
                    startDate.setText(workOrders.get(position).getWorkOrderStartDate());
                }
                else{
                    startDate.setText("İş kaydı daha alınmadı.");
                }
                if(workOrders.get(position).isClosed()){
                    setGreen(light2);
                    endDate.setText(workOrders.get(position).getWorkOrderEndDate());
                }
                else{
                    endDate.setText("İş kaydı daha sonlanmadı.");
                }
                return convertView;
            }
        };
        workOrderListView.setAdapter(adapter);
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrders().enqueue(new Callback<List<WorkOrderModel>>() {
            @Override
            public void onResponse(Call<List<WorkOrderModel>> call, Response<List<WorkOrderModel>> response) {
                if (response.isSuccessful() && response.body() != null){
                    workOrders.addAll(response.body());

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
            public void onFailure(Call<List<WorkOrderModel>> call, Throwable t) {
                Log.e("pop",t.getMessage());
            }
        });
        workOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(WorkOrdersActivity.this,WorkOrderDetailActivity.class);
                sayfa.putExtra("workOrderId",workOrders.get(position).getId());
                startActivity(sayfa);
            }
        });
    }
    public void setGreen(View view) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.GREEN);
        //shape.setSize(50, 50); isteğe bağlı: sabit boyut
        view.setBackground(shape);
    }
}