package com.example.staj_deneme.Activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import com.example.staj_deneme.Adapter.ErrorAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorDetailsActivity extends BaseActivity {
    ErrorAdapter adapter;
    ListView errorDetailListView;
    List<ErrorModel> errorModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_error_details);
    errorDetailListView = findViewById(R.id.errorDetails_listview);
    errorModelList = new ArrayList<>();
    adapter = new ErrorAdapter(errorModelList,ErrorDetailsActivity.this);
    errorDetailListView.setAdapter(adapter);
    ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
    errorInterface.getAll().enqueue(new Callback<List<ErrorModel>>() {
        @Override
        public void onResponse(Call<List<ErrorModel>> call, Response<List<ErrorModel>> response) {
            if(response.body() != null && response.isSuccessful() ){
                for(ErrorModel e: response.body()){
                    errorModelList.add(e);
                }
                adapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<List<ErrorModel>> call, Throwable t) {
            Toast.makeText(ErrorDetailsActivity.this,"Noluyor böyle ulan",Toast.LENGTH_LONG).show();
        }
    });

    }
}