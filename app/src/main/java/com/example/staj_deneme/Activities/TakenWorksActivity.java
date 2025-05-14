package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
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

import com.example.staj_deneme.InterFaces.WorkInterface;
import com.example.staj_deneme.InterFaces.WorkOrderInterface;
import com.example.staj_deneme.Models.WorkModel;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.Models.WorkOrderViewModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakenWorksActivity extends BaseActivity {
    List<WorkOrderModel> workModelList ;
    List<String> machineNameList;
    ListView workModelListView;
    BaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_taken_works);
        workModelListView = findViewById(R.id.work_listview);
        workModelList = new ArrayList<>();
        machineNameList = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return workModelList.size();
            }

            @Override
            public Object getItem(int position) {
                return workModelList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = LayoutInflater.from(TakenWorksActivity.this).inflate(R.layout.work_item_layout,parent,false);
                }
                TextView title = convertView.findViewById(R.id.workTitle_txt);
                TextView desc = convertView.findViewById(R.id.workDesc_txt);
                TextView startDate = convertView.findViewById(R.id.workStartDate_txt);

                title.setText(workModelList.get(position).getTitle());
                desc.setText(workModelList.get(position).getDesc());
                startDate.setText(workModelList.get(position).getWorkOrderStartDate());

                return convertView;
            }
        };
        workModelListView.setAdapter(adapter);
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrders().enqueue(new Callback<List<WorkOrderViewModel>>() {
            @Override
            public void onResponse(Call<List<WorkOrderViewModel>> call, Response<List<WorkOrderViewModel>> response) {
                if ( response.isSuccessful() && response.body() != null){
                    for (WorkOrderViewModel w : response.body()) {
                        workModelList.add(w.getWorkOrderModel());
                        machineNameList.add(w.getMachineModel().getName());
                    }
                    userCheck();
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
            public void onFailure(Call<List<WorkOrderViewModel>> call, Throwable t) {
                Log.e("pop", t.getMessage());
            }

        });

        workModelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(TakenWorksActivity.this,AddWorkActivity.class);
                sayfa.putExtra("workOrderId",workModelList.get(position).getId());
                startActivity(sayfa);
            }
        });
    }
    public void userCheck(){
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String us_na = sp.getString("Username",null);
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        Log.e("qw","asadwa");
        workOrderInterface.getUserId(us_na).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body()!= null){
                    List<WorkOrderModel> ww = new ArrayList<>();
                    ww.addAll(workModelList);
                    for(WorkOrderModel w : ww){
                        if (w.userId != response.body()){
                            workModelList.remove(w);
                        }
                        if(w.isClosed()) {
                            workModelList.remove(w);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(TakenWorksActivity.this,"Henüz bir iş emri almadınız.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("aaaaaaaa", t.getMessage());
            }
        });
    }
}