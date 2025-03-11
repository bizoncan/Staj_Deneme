package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.InterFaces.MachinePartsApiInterface;
import com.example.staj_deneme.Models.MachinePartModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MachineDetailActivity extends BaseActivity {
    ListView machinePartListView;
    List<String> nameList,descList,numberList;
    List<Integer> machinePartIdList;
    BaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_machine_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        machinePartListView= findViewById(R.id.machinePart_listview);
        nameList = new ArrayList<>();
        descList = new ArrayList<>();
        numberList = new ArrayList<>();
        machinePartIdList = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return nameList.size();
            }

            @Override
            public Object getItem(int position) {
                return nameList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(MachineDetailActivity.this).inflate(R.layout.past_errors_layout,parent,false);
                }
                TextView name = convertView.findViewById(R.id.arizaT_textview);
                TextView desc = convertView.findViewById(R.id.arizaAcikla_textview);
                TextView number = convertView.findViewById(R.id.arizaZaman_textview);


                name.setText(nameList.get(position));
                desc.setText(descList.get(position));
                number.setText(numberList.get(position));
                return convertView;
            }
        };
        machinePartListView.setAdapter(adapter);

        MachinePartsApiInterface machinePartsApiInterface = RetrofitClient.getApiServiceMachinePart();
        int id=Integer.parseInt(getIntent().getStringExtra("machineId"));
        machinePartsApiInterface.getMachinePart(id).enqueue(new Callback<List<MachinePartModel>>() {
            @Override
            public void onResponse(Call<List<MachinePartModel>> call, Response<List<MachinePartModel>> response) {
                if (response.body() != null && response.isSuccessful()){
                    for(MachinePartModel m : response.body()){
                        nameList.add(m.getName());
                        descList.add(m.getDesc());
                        numberList.add(Integer.toString(m.getNumber()));
                        machinePartIdList.add(m.getId());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MachinePartModel>> call, Throwable t) {
                Log.e("RetrofitError", "Hata oluştu: " + t.getMessage());
            }
        });
        machinePartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(MachineDetailActivity.this, ErrorDetailsActivity.class);
                sayfa.putExtra("machinePartId",machinePartIdList.get(position));
                startActivity(sayfa);
            }
        });
    }
    public void hatalari_gor(View view){
        Intent sayfa = new Intent(MachineDetailActivity.this,ErrorDetailsActivity.class);
        sayfa.putExtra("machineId",getIntent().getStringExtra("machineId"));
        startActivity(sayfa);
    }
}