package com.example.staj_deneme.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddErrorManuel extends BaseActivity {
    EditText machineIdEditText,machinePartIdEditText,errorTypeEdt,errorDateEdt,errorDescEdt;

    private static final int CAMERA_REQUEST = 100;
    private long startTime = 0L;
    private long timeElapsed = 0L;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;
    private String formatedTime ="00:00";
    SimpleDateFormat dateFormat;
    String currentDate;
    Date dateIn;

    Bitmap tempBitmapPhoto;
    private Uri selectedImageUri = null;
    byte[] tempPhotoBytes;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_error_manuel);


        machineIdEditText= findViewById(R.id.manuelMachineId_edittext);
        machinePartIdEditText = findViewById(R.id.manuelMachinePartId_edittext);

        errorDateEdt = findViewById(R.id.manuelerrordate_edittext);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));//Türkiye Saat Dilimi
        currentDate = dateFormat.format(new Date());
        errorDateEdt.setText(currentDate);
        errorTypeEdt=findViewById(R.id.manuelErrorType_edittext);
        errorDescEdt = findViewById(R.id.manuelErrorDesc_edittext);
        dateIn = new Date();
    }
    public void hata_ekle(View view){
        ErrorModel errorModel = new ErrorModel();
        if(errorTypeEdt.getText().toString().isEmpty() || errorDescEdt.getText().toString().isEmpty() || errorDateEdt.getText().toString().isEmpty() || machineIdEditText.getText().toString().isEmpty()){
            Toast.makeText(AddErrorManuel.this,"Gerekli alanları doldurunuz",Toast.LENGTH_LONG).show();
            return;
        }
        errorModel.setMachineId(Integer.parseInt(machineIdEditText.getText().toString()));
        errorModel.setErrorType(errorTypeEdt.getText().toString());
        errorModel.setErrorDesc(errorDescEdt.getText().toString());
        errorModel.setErrorDate(currentDate);
        errorModel.setErrorEndDate(dateFormat.format(new Date()));


        if(!machinePartIdEditText.getText().toString().isEmpty()) errorModel.setMachinePartId(Integer.parseInt(machinePartIdEditText.getText().toString()));
        if(selectedImageUri != null) {
            try {
                // Convert image to byte array
                byte[] imageBytes = convertImageToByteArray(selectedImageUri);

                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // Set the Base64 string to your error model
                errorModel.setErrorImage(base64Image);

                // If you need to include the image type/extension
                String imageType = getContentResolver().getType(selectedImageUri);
                errorModel.setErrorImageType(imageType);

                // Submit to API
            } catch (IOException e) {
                Toast.makeText(AddErrorManuel.this, "Resim yüklenirken hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else if(tempBitmapPhoto != null){
            byte[] imageBytes = convertBitmapToByteArray(tempBitmapPhoto);

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
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });


        errorInterface.add(errorModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Retrofit", "Response code: " + response.code());
                Log.d("Retrofit", "Response message: " + response.message());
                if(response.isSuccessful() ){
                    Toast.makeText(AddErrorManuel.this,"İşlem Başarılı",Toast.LENGTH_LONG).show();
                    Intent sayfa = new Intent(AddErrorManuel.this,TestActivity.class);
                    del_not();
                    startActivity(sayfa);
                }
                else{
                    Toast.makeText(AddErrorManuel.this,"Bir hata meydana geldi"+response.errorBody(),Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddErrorManuel.this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
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
    private byte[] convertImageToByteArray(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // This buffer size can be adjusted
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
    public void takePicture(View view){
        requestCameraPermission();
    }
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Optionally show the selected image in an ImageView
            // imageView.setImageURI(selectedImageUri);
        }
        else if(requestCode== CAMERA_REQUEST && resultCode == RESULT_OK
                && data != null ){
            tempBitmapPhoto = (Bitmap) data.getExtras().get("data");

        }
    }
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeElapsed = SystemClock.elapsedRealtime() - startTime;
            // You can log the time for debugging if needed
            // Log.d("Timer", "Time elapsed: " + timeElapsed + " ms");

            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure to stop the timer when the activity is destroyed
        timerHandler.removeCallbacks(timerRunnable);
    }
}
