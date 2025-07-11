package com.example.staj_deneme.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.staj_deneme.Adapter.SliderAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.InterFaces.ImageApiInterface;
import com.example.staj_deneme.Models.ErrorInfoModel;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.Models.ErrorResponseNoListModel;
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
    TextView machineId,machinePartId,errorType,errorDesc,errorStartDate,errorEndDate,errorUser;
    ErrorModel errorModel;
    ErrorInfoModel errorInfoModel;
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
        errorInfoModel = new ErrorInfoModel();
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
        errorUser=findViewById(R.id.errorUser_textview);
        int errorId = getIntent().getIntExtra("ErrorId",0);
        get_error_list(errorId);



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
    public void get_error_list(int errorId){
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getById(errorId).enqueue(new Callback<ErrorResponseNoListModel>() {
            @Override
            public void onResponse(Call<ErrorResponseNoListModel> call, Response<ErrorResponseNoListModel> response) {
                if(response.isSuccessful()&& response.body() != null){
                    errorModel = response.body().getErrorModel();
                    errorInfoModel=response.body().getErrorInfoModel();
                    fillList();
                    addImages();

                }
                else {
                    Log.d("amanin",response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<ErrorResponseNoListModel> call, Throwable t) {
                Log.d("amanin",t.getMessage());
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
                    Drawable vectorDrawable = getDrawable(R.drawable.baseline_no_photography_24);
                    sliderImages.add(Bitmap.createBitmap(
                            vectorDrawable.getIntrinsicWidth(),
                            vectorDrawable.getIntrinsicHeight(),
                            Bitmap.Config.ARGB_8888));
                }
            }
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
        machineId.setText("Makine Id: "+errorInfoModel.getMachineName());
        if (errorModel.getMachinePartId()!=null){
            machinePartId.setText("Makine parçası Id: "+errorInfoModel.getMachinePartName());
        }
        else{
            machinePartId.setText("Makine Parçası Id'si yok");
        }
        errorType.setText("Hata tipi: "+errorModel.getErrorType());
        errorDesc.setText("Hata açıklama: "+errorModel.getErrorDesc());
        errorStartDate.setText("Arıza kaydı giriş tarihi: "+start.toString());
        errorEndDate.setText("Arıza kaydı için geçen süre: "+durs);
        errorUser.setText("Arızayı giren kişi: "+ errorInfoModel.getUserName());
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
                Drawable vectorDrawable = getDrawable(R.drawable.baseline_no_photography_24);
                vectorDrawable = DrawableCompat.wrap(vectorDrawable);
                Bitmap bb = Bitmap.createBitmap(
                        vectorDrawable.getIntrinsicWidth(),
                        vectorDrawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bb);
                vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
                vectorDrawable.draw(canvas);

                sliderImages.add(bb);
                Drawable vectorDrawable_ = getDrawable(R.drawable.baseline_home_24);
                sliderAdapter.setImageList(sliderImages);
            }
        }
        else{
            Drawable vectorDrawable = getDrawable(R.drawable.baseline_no_photography_24);
            vectorDrawable = DrawableCompat.wrap(vectorDrawable);
            Bitmap bb = Bitmap.createBitmap(
                    vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bb);
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
            vectorDrawable.draw(canvas);

            sliderImages.add(bb);
            Drawable vectorDrawable_ = getDrawable(R.drawable.baseline_home_24);
            sliderAdapter.setImageList(sliderImages);
        }
    }
}