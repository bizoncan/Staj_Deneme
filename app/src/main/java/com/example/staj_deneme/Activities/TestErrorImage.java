package com.example.staj_deneme.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.Models.ErrorResponseModel;
import com.example.staj_deneme.Models.MachineModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestErrorImage extends BaseActivity {

    ListView errorListView;
    BaseAdapter adapter;
    List<String> typeList;
    List<String> descList;
    List<String> dateList;
    List<String> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_error_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        errorListView = findViewById(R.id.errrrrrrrrr);
        typeList = new ArrayList<>();
        descList = new ArrayList<>();
        dateList = new ArrayList<>();
        imageList = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return typeList.size();
            }

            @Override
            public Object getItem(int position) {
                return typeList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(TestErrorImage.this).inflate(R.layout.machine_layout,parent,false);
                }
                ImageView imgURL = convertView.findViewById(R.id.imgURL);
                TextView type = convertView.findViewById(R.id.Name_textview);
                TextView desc = convertView.findViewById(R.id.Number_textview);
                TextView date = convertView.findViewById(R.id.Desc_textview);

                type.setText(typeList.get(position));
                desc.setText(descList.get(position));
                date.setText(dateList.get(position));

                String imageData = imageList.get(position);

                if (imageData != null && !imageData.isEmpty()) {
                    try {
                        byte[] decodedBytes = Base64.decode(imageData, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                        imgURL.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                        imgURL.setImageResource(R.drawable.baseline_home_24);
                    }
                } else {
                    imgURL.setImageResource(R.drawable.baseline_home_24);
                }
                return convertView;
            }
        };
        errorListView.setAdapter(adapter);
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getAll().enqueue(new Callback<ErrorResponseModel>() {
            @Override
            public void onResponse(Call<ErrorResponseModel> call, Response<ErrorResponseModel> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    List<ErrorModel> errorModels = response.body().getErrorModelList();
                    if (errorModels.isEmpty()) {
                        Toast.makeText(TestErrorImage.this,"AAAAAAAAAAAAAAAAA",Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (ErrorModel e : errorModels) {
                        typeList.add(e.getErrorType());
                        descList.add(e.getErrorDesc());
                        dateList.add(e.getErrorDate());
                        imageList.add(e.getErrorImage());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ErrorResponseModel> call, Throwable t) {

            }
        });


    }

}