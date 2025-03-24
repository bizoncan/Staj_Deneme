package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.example.staj_deneme.Adapter.ErrorAdapter;
import com.example.staj_deneme.Adapter.ExpandableListViewAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.Models.ErrorInfoModel;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.skydoves.transformationlayout.TransformationLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorDetailsActivity extends BaseActivity {
    ErrorAdapter adapter;
    ListView errorDetailListView;
    List<ErrorModel> errorModelList;
    String i_machine_part_name,i_machine_name;
    ExpandableListViewAdapter adapterEx;
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    TransformationLayout transformationLayout;
    Boolean opened= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_error_details);
    errorDetailListView = findViewById(R.id.errorDetails_listview);
    transformationLayout = findViewById(R.id.error_transformation_layout);
    errorModelList = new ArrayList<>();
    adapter = new ErrorAdapter(errorModelList,ErrorDetailsActivity.this);
    errorDetailListView.setAdapter(adapter);
    errorDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent sayfa = new Intent(ErrorDetailsActivity.this, ErrorInfoActivity.class);
            sayfa.putExtra("ErrorId",errorModelList.get(position).getId());
            startActivity(sayfa);
        }
    });
    expandableListView = findViewById(R.id.ex_listview);

    listGroup = new ArrayList<>();
    listItem = new HashMap<>();
    init_data();
    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
        @Override
        public void onGroupExpand(int groupPosition) {
            // Do nothing - this allows multiple groups to be expanded
        }
    });
    adapterEx = new ExpandableListViewAdapter(ErrorDetailsActivity.this,listGroup,listItem);
    expandableListView.setAdapter(adapterEx);
    if(getIntent().getStringExtra("machineId")== null){
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getAll().enqueue(new Callback<List<ErrorModel>>() {
            @Override
            public void onResponse(Call<List<ErrorModel>> call, Response<List<ErrorModel>> response) {
                if(response.body() != null && response.isSuccessful() ){
                    errorModelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan"+response.errorBody().toString(),Toast.LENGTH_LONG).show();
                    Log.e("HATA:", response.errorBody().toString());

                }
                Log.d("API_RESPONSE", "Response code: " + response.code());
            }

            @Override
            public void onFailure(Call<List<ErrorModel>> call, Throwable t) {
                Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan" + t.getMessage(),Toast.LENGTH_LONG).show();
                Log.e("HATA33:", t.getMessage());
            }
        });}
    else if (getIntent().getStringExtra("machinePartId")==null && getIntent().getStringExtra("machineId")!=null){
        int mm = Integer.parseInt(getIntent().getStringExtra("machineId"));
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getByMachineId(mm).enqueue(new Callback<List<ErrorModel>>() {
            @Override
            public void onResponse(Call<List<ErrorModel>> call, Response<List<ErrorModel>> response) {
                if(response.body() != null && response.isSuccessful() ){
                    errorModelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ErrorModel>> call, Throwable t) {
                Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
                Log.e("pop",t.getMessage());
            }
        });
    }
    else{
        int mm = Integer.parseInt(getIntent().getStringExtra("machinePartId"));
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getByMachinePartId(mm).enqueue(new Callback<List<ErrorModel>>() {
            @Override
            public void onResponse(Call<List<ErrorModel>> call, Response<List<ErrorModel>> response) {
                if(response.body() != null && response.isSuccessful() ){
                    errorModelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ErrorModel>> call, Throwable t) {
                Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
                Log.e("pop",t.getMessage());
            }
        });
    }
    }
    public void gorunur_ol(View view){
        if(opened){
            transformationLayout.startTransform();
            opened = false;
        }
        else {
            transformationLayout.finishTransform();
            opened=true;
        }
    }
    private void init_data(){
        List<ErrorInfoModel> tempErrorList=new ArrayList<>();
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getErrorInfos().enqueue(new Callback<List<ErrorInfoModel>>() {
            @Override
            public void onResponse(Call<List<ErrorInfoModel>> call, Response<List<ErrorInfoModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    for(ErrorInfoModel e: response.body()){
                        tempErrorList.add(e);
                    }
                    addItems(tempErrorList);
                }
            }

            @Override
            public void onFailure(Call<List<ErrorInfoModel>> call, Throwable t) {

            }
        });


    }
    public void addItems(List<ErrorInfoModel> tempErrorList){

        listGroup.add("Tarih");
        listGroup.add("Makineler");
        listGroup.add("Makine Parçaları");
        listGroup.add("Fotoğraf");
        listGroup.add("Arızayı Giren Kişi");

        List<String> tarihList = new ArrayList<>();
        tarihList.add("Bugün");
        tarihList.add("Bu hafta");
        tarihList.add("Bu ay");


        List<String> makineFilterList = new ArrayList<>();
        for(ErrorInfoModel e: tempErrorList){
            if (getIntent().getStringExtra("machineId")==null){
                if (!makineFilterList.contains(e.getMachineName())){
                    makineFilterList.add(e.getMachineName());
                }
            }
            else {
                if (e.getMachineId() == Integer.parseInt(getIntent().getStringExtra("machineId"))){
                    if (!makineFilterList.contains(e.getMachineName())){
                        makineFilterList.add(e.getMachineName());
                        i_machine_name = e.getMachineName();
                    }
                }
            }


        }

        List<String> makineParcaFilterList = new ArrayList<>();
        for(ErrorInfoModel e: tempErrorList){
            if (e.getMachinePartName() != null){
                if (getIntent().getStringExtra("machinePartId")==null){
                    if (!makineParcaFilterList.contains(e.getMachinePartName())){
                        makineParcaFilterList.add(e.getMachinePartName());
                    }
                }
                else {
                    if (e.getMachinePartId() == Integer.parseInt(getIntent().getStringExtra("machinePartId"))){
                        if (!makineParcaFilterList.contains(e.getMachinePartName())){
                            makineParcaFilterList.add(e.getMachinePartName());
                            i_machine_part_name = e.getMachinePartName();
                        }
                    }
                }
            }

        }

        List<String> fotoList = new ArrayList<>();
        fotoList.add("Var");
        fotoList.add("Yok");

        List<String> userInputList = new ArrayList<>();
        for(ErrorInfoModel e: tempErrorList){
            if (e.getUserName() != null){
                if (!userInputList.contains(e.getUserName())){
                    userInputList.add(e.getUserName());
                }
            }

        }


        listItem.put(listGroup.get(0),tarihList);
        listItem.put(listGroup.get(1),makineFilterList);
        listItem.put(listGroup.get(2),makineParcaFilterList);
        listItem.put(listGroup.get(3),fotoList);
        listItem.put(listGroup.get(4),userInputList);
        adapterEx.notifyDataSetChanged();
    }
    public void filtrele(View view){
        StringBuilder selectedText = new StringBuilder();
        HashMap<String, String> selectedValues = new HashMap<>();
        List<String> groupNameList = new ArrayList<>();
        List<String> childNameList= new ArrayList<>();
        for (int groupPosition : adapterEx.selectedItems.keySet()) {
            for (int childPosition : adapterEx.selectedItems.get(groupPosition).keySet()) {
                if (adapterEx.selectedItems.get(groupPosition).get(childPosition)) {
                    String groupName = listGroup.get(groupPosition);
                    selectedValues.put(groupName, listItem.get(groupName).get(childPosition));

                }
            }
        }
        String s1,s2,s3,s4,s5;
        List<String> selectedItems = new ArrayList<>();
        if(selectedValues.keySet().contains("Tarih")){
            selectedItems.add(selectedValues.get("Tarih"));
            s1= selectedValues.get("Tarih");
        }
        else{
            selectedItems.add(null);
            s1=null;
        }
        if (selectedValues.keySet().contains("Makineler")){
            selectedItems.add(selectedValues.get("Makineler"));
            s2 = selectedValues.get("Makineler");
        }
        else {
            selectedItems.add(null);
            s2=null;
        }
        if (selectedValues.keySet().contains("Makine Parçaları")){
            selectedItems.add(selectedValues.get("Makine Parçaları"));
            s3=selectedValues.get("Makine Parçaları");
        }
        else {
            selectedItems.add(null);
            s3=null;
        }
        if (selectedValues.keySet().contains("Fotoğraf")){
            selectedItems.add(selectedValues.get("Fotoğraf"));
            s4 = selectedValues.get("Fotoğraf");
        }
        else {
            selectedItems.add(null);
            s4 = null;
        }
        if (selectedValues.keySet().contains("Arızayı Giren Kişi")){
            selectedItems.add(selectedValues.get("Arızayı Giren Kişi"));
            s5 = selectedValues.get("Arızayı Giren Kişi");
        }
        else {
            selectedItems.add(null);
            s5 = null;
        }
        if (i_machine_part_name!= null)
        {
            s3 = i_machine_part_name;
        }
        if(i_machine_name!=null){
            s2= i_machine_name;
        }
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.filterListView(s1,s2,s3,s4,s5).enqueue(new Callback<List<ErrorModel>>() {
            @Override
            public void onResponse(Call<List<ErrorModel>> call, Response<List<ErrorModel>> response) {
                Log.d("API_RESPONSE", "Request URL: " + call.request().url());
                Log.d("API_RESPONSE", "Response code: " + response.code());

                if (!response.isSuccessful()) {
                    try {
                        Log.e("API_RESPONSE", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Could not read error body");
                    }
                }
                errorModelList.clear();
                if(response.body() != null && response.isSuccessful())
                {
                    errorModelList.addAll(response.body());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<ErrorModel>> call, Throwable t) {

            }
        });
    }
}