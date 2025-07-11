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
        machinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sayfa = new Intent(TestActivity.this,MachineDetailActivity.class);
                sayfa.putExtra("machineId",Integer.toString(machineModelList.get(position).getId()));
                startActivity(sayfa);
            }
        });

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

        if(opened){
            transformationLayout.startTransform();

            opened=false;
        }
        else{
            transformationLayout.finishTransform();
            opened=true;
        }

    }

    @Override
    public void onNotificationButtonClicked() {
        bildirim_yukle(null);
    }
}