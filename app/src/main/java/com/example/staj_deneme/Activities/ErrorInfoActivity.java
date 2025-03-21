package com.example.staj_deneme.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.staj_deneme.Adapter.SliderAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.InterFaces.ImageApiInterface;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorInfoActivity extends BaseActivity {
    TextView machineId,machinePartId,errorType,errorDesc,errorStartDate,errorEndDate;
    ErrorModel errorModel;
    ArrayList<Object> sliderImages ;
    SliderAdapter sliderAdapter;
    ViewPager2 viewPager;
    List<String> imageCollectionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_error_info);
        imageCollectionList = new ArrayList<>();

        errorModel = new ErrorModel();
        viewPager = findViewById(R.id.viewPager);
        sliderImages =  new ArrayList<>();
        sliderAdapter = new SliderAdapter(ErrorInfoActivity.this,sliderImages);
        
        viewPager.setAdapter(sliderAdapter);
        machineId = findViewById(R.id.errorMachineId_textview);
        machinePartId = findViewById(R.id.errorMachinePartId_textview);
        errorType = findViewById(R.id.errorType_textview);
        errorDesc = findViewById(R.id.errorDesc_textview);
        errorStartDate= findViewById(R.id.errorStartDate_textview);
        errorEndDate= findViewById(R.id.errorEndDate_textview);
        int errorId = getIntent().getIntExtra("ErrorId",0);
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getById(errorId).enqueue(new Callback<ErrorModel>() {
            @Override
            public void onResponse(Call<ErrorModel> call, Response<ErrorModel> response) {
                if(response.isSuccessful()&& response.body() != null){
                    errorModel = response.body();
                    fillList();
                    addImages();

                }
                else {
                    Log.d("amanin",response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<ErrorModel> call, Throwable t) {
                Log.d("amanin",t.getMessage());
            }
        });
        ImageApiInterface imageApiInterface = RetrofitClient.getApiImageService();
        imageApiInterface.getImages(errorId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful() && response.body() != null){
                    imageCollectionList.addAll(response.body());
                    fillImageCollection();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }

    private void fillImageCollection() {
        if (imageCollectionList.size() > 0 ) {
            for(String s : imageCollectionList){
                try {
                    byte[] decodedBytes = Base64.decode(s, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    sliderImages.add(bitmap);
                    sliderAdapter.setImageList(sliderImages);

                } catch (Exception e) {
                    Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                    sliderImages.add(R.drawable.baseline_home_24);
                }
            }
        }
        else{
            sliderImages.add(R.drawable.baseline_home_24);
        }
    }

    private void fillList() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(errorModel.getErrorDate(),dateFormat);
        LocalDateTime end = LocalDateTime.parse(errorModel.getErrorEndDate(),dateFormat);
        Duration durr = Duration.between(start, end);
        Long hours= durr.toHours();
        Long minutes= durr.toMinutes()%60;
        Long seconds= durr.getSeconds()%60;
        String durs = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours,minutes, seconds);
        machineId.setText("Makine Id: "+errorModel.getMachineId().toString());
        if (machinePartId!=null){
            machinePartId.setText("Makine parçası Id: "+errorModel.getMachinePartId().toString());
        }
        else{
            machinePartId.setText("Makine Parçası Id'si yok: "+errorModel.getMachinePartId().toString());
        }
        errorType.setText("Hata tipi: "+errorModel.getErrorType());
        errorDesc.setText("Hata açıklama: "+errorModel.getErrorDesc());
        errorStartDate.setText("Arıza kaydı giriş tarihi: "+start.toString());
        errorEndDate.setText("Arıza kaydı için geçen süre: "+durs);

    }

    private void addImages() {
        if (errorModel.getErrorImage() != null) {
            try {
                byte[] decodedBytes = Base64.decode(errorModel.getErrorImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                sliderImages.add(bitmap);
                sliderAdapter.setImageList(sliderImages);

            } catch (Exception e) {
                Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                sliderImages.add(R.drawable.baseline_home_24);
            }
        }
        else{
            sliderImages.add(R.drawable.baseline_home_24);
        }
    }
}