package com.example.staj_deneme.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.staj_deneme.Adapter.SliderAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import android.Manifest;

import com.example.staj_deneme.InterFaces.ImageApiInterface;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.Models.ImageCollectionModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import android.util.Base64;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAddErrorActivity extends BaseActivity {
    EditText machineIdEditText,machinePartIdEditText,errorTypeEdt,errorDateEdt,errorDescEdt;

    Boolean isPhotoTaken = false;

    ArrayList<Object> sliderImages ;
    SliderAdapter sliderAdapter;
    ViewPager2 viewPager;
    ErrorModel errorModel;
    private static final int CAMERA_REQUEST = 100;
    private long startTime = 0L;
    private long timeElapsed = 0L;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;
    private String formatedTime ="00:00";
    SimpleDateFormat dateFormat;
    String currentDate;
    Date dateIn;
    Boolean isDone=false;
    List<Bitmap> tempBitmapPhotos;
    private  List<Uri> selectedImageUris = null;
    byte[] tempPhotoBytes;



    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_add_error);
        startTimer();
        machineIdEditText= findViewById(R.id.machineId_edt);
        machinePartIdEditText = findViewById(R.id.machinePartId_edt);
        machineIdEditText.setText(getIntent().getStringExtra("machineID"));
        machinePartIdEditText.setText(getIntent().getStringExtra("machinePartID"));
        errorDateEdt = findViewById(R.id.errorDate_edt);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        currentDate = dateFormat.format(new Date());
        errorDateEdt.setText(currentDate);
        errorTypeEdt=findViewById(R.id.errorType_edt);
        errorDescEdt = findViewById(R.id.errorDesc_edt);
        dateIn = new Date();

        tempBitmapPhotos = new ArrayList<>();
        selectedImageUris = new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);
        sliderImages =  new ArrayList<>();
        sliderAdapter = new SliderAdapter(NotificationAddErrorActivity.this ,sliderImages);
        viewPager.setAdapter(sliderAdapter);

        errorModel = new ErrorModel();
        get_user();
    }
    public void get_user(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        String us_na = sharedPreferences.getString("Username","");
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        us_na = "admin";

        errorInterface.getUserId(us_na).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!= null){
                    errorModel.setUserId(response.body());
                    Log.e("bubuş","kettttt");

                }
                else {
                    Log.e("API_DEBUG", "Failed to get user ID" + response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("API_DEBUG", "Failed to get user ID", t);
            }
        });



    }

    private void startTimer() {
        startTime = SystemClock.elapsedRealtime();
        isTimerRunning = true;
        timerHandler.postDelayed(timerRunnable, 10);
    }
    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        isTimerRunning = false;
        formatedTime = formatElapsedTime(timeElapsed);
    }
    private String formatElapsedTime(long timeInMillis) {
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public void hata_ekle(View view){

        ImageCollectionModel imageCollectionModel = new ImageCollectionModel();
        if(errorTypeEdt.getText().toString().isEmpty() || errorDescEdt.getText().toString().isEmpty() || errorDateEdt.getText().toString().isEmpty() || machineIdEditText.getText().toString().isEmpty()){
            Toast.makeText(NotificationAddErrorActivity.this,"Gerekli alanları doldurunuz",Toast.LENGTH_LONG).show();
            return;
        }
        errorModel.setMachineId(Integer.parseInt(machineIdEditText.getText().toString()));
        errorModel.setErrorType(errorTypeEdt.getText().toString());
        errorModel.setErrorDesc(errorDescEdt.getText().toString());
        errorModel.setErrorDate(currentDate);
        errorModel.setErrorEndDate(dateFormat.format(new Date()));


        if(!machinePartIdEditText.getText().toString().isEmpty()) errorModel.setMachinePartId(Integer.parseInt(machinePartIdEditText.getText().toString()));
        if(selectedImageUris.size() != 0) {
            try {
                byte[] imageBytes = convertImageToByteArray(selectedImageUris.get(0));

                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                errorModel.setErrorImage(base64Image);

                String imageType = getContentResolver().getType(selectedImageUris.get(0));
                errorModel.setErrorImageType(imageType);
                isPhotoTaken = true;
            } catch (IOException e) {
                Toast.makeText(NotificationAddErrorActivity.this, "Resim yüklenirken hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else if(tempBitmapPhotos.size() != 0){
            byte[] imageBytes = convertBitmapToByteArray(tempBitmapPhotos.get(0));
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            errorModel.setErrorImage(base64Image);
            String imageType = "image/jpeg";
            errorModel.setErrorImageType(imageType);

        }
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        String us_na = sharedPreferences.getString("Username","");
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getUserId(us_na).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!= null){
                    errorModel.setUserId(response.body());
                    add_error(errorModel);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }
    public void add_error(ErrorModel errorModel){
        if (!isDone){
            isDone = true;
            ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
            errorInterface.add(errorModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("Retrofit", "Response code: " + response.code());
                    Log.d("Retrofit", "Response message: " + response.message());
                    if(response.isSuccessful() ){
                        resimleri_ekle();
                        Toast.makeText(NotificationAddErrorActivity.this,"İşlem Başarılı",Toast.LENGTH_LONG).show();
                        Intent sayfa = new Intent(NotificationAddErrorActivity.this,TestActivity.class);
                        stopTimer();
                        Log.e("Geçen Zaman:",formatedTime);
                        del_not();
                        startActivity(sayfa);
                    }
                    else{
                        Toast.makeText(NotificationAddErrorActivity.this,"Bir hata meydana geldi"+response.errorBody(),Toast.LENGTH_LONG).show();
                        try {
                            String errorBody = response.errorBody() != null ?
                                    response.errorBody().string() : "No error body";
                            Log.e("Retrofit", "Error body: " + errorBody);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(NotificationAddErrorActivity.this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText(NotificationAddErrorActivity.this,"İşlem tamamlanıyor bekleyin",Toast.LENGTH_LONG).show();
        }
    }
    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUris.add(data.getData());
            sliderImages.add(data.getData());
            sliderAdapter.setImageList(sliderImages);
        }
        else if(requestCode== CAMERA_REQUEST && resultCode == RESULT_OK
                && data != null ){
            if (captureImageUri != null) {
                selectedImageUris.add(captureImageUri);
                sliderImages.add(captureImageUri);
                sliderAdapter.setImageList(sliderImages);
                Toast.makeText(this, "Image captured successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void takePicture(View view){
        requestCameraPermission();
    }
    private void requestCameraPermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[0]),
                    CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Uri captureImageUri;
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Camera_Image_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo captured by app");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        try {
            captureImageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
            );

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeElapsed = SystemClock.elapsedRealtime() - startTime;

            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
    private byte[] convertImageToByteArray(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        inputStream.close();
        return byteBuffer.toByteArray();
    }
    private byte[] convertBitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray=   stream.toByteArray();
        try{
            stream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return byteArray;
    }
    private byte[] compressImage(byte[] imageBytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }

    public void del_not(){
        RecieveNotificationInterface recieveNotificationInterface = RetrofitClient.getApiServiceNotification();
        int pi = Integer.parseInt(getIntent().getStringExtra("notificationId"));
        recieveNotificationInterface.deleteNotification(Integer.parseInt(getIntent().getStringExtra("notificationId"))).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.e("acaba","oldu mu");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    public void resimleri_ekle(){
        byte[] imageBytes;
        String base64Image;
        List<String> base64images = new ArrayList<>();
        if (tempBitmapPhotos.size()>1)
        {
            List<Bitmap> tt_ll;
            if (isPhotoTaken){
                tt_ll = tempBitmapPhotos.subList(0,tempBitmapPhotos.size());
            }
            else{
                tt_ll = tempBitmapPhotos.subList(1,tempBitmapPhotos.size());
            }
            for (Bitmap bb : tt_ll){
                imageBytes = convertBitmapToByteArray(bb);
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                base64images.add(base64Image);
            }

        }
        if (selectedImageUris.size()>1)
        {
            List<Uri> ss_uu = selectedImageUris.subList(1,selectedImageUris.size());
            for (Uri uu:ss_uu){
                try {
                    imageBytes = convertImageToByteArray(uu);
                    base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    base64images.add(base64Image);
                } catch (IOException e) {
                    Toast.makeText(NotificationAddErrorActivity.this, "Resim yüklenirken hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
        if(base64images.size()>0){
            ImageApiInterface imageApiInterface = RetrofitClient.getApiImageService();
            imageApiInterface.addImageData(base64images).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(NotificationAddErrorActivity.this,"oldu galiba",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(NotificationAddErrorActivity.this,"Bir hata meydana geldi"+response.errorBody(),Toast.LENGTH_LONG).show();
                        try {
                            String errorBody = response.errorBody() != null ?
                                    response.errorBody().string() : "No error body";
                            Log.e("Retrofit", "Error body: " + errorBody);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(NotificationAddErrorActivity.this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}