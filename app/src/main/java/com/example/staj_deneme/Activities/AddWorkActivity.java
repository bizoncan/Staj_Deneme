package com.example.staj_deneme.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AppCompatActivity;

import com.example.staj_deneme.Models.WorkImagePostModel;
import com.google.android.material.appbar.MaterialToolbar;

import com.example.staj_deneme.Adapter.SliderAdapter;
import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.InterFaces.ImageApiInterface;
import com.example.staj_deneme.InterFaces.WorkInterface;
import com.example.staj_deneme.InterFaces.WorkOrderInterface;
import com.example.staj_deneme.Models.ErrorIdModel;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.Models.WorkModel;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkActivity extends AppCompatActivity {
    EditText workTypeEdt, workDateEdt, workDescEdt;
    TextInputLayout machineDropdownLayout, machinePartDropdownLayout;
    AutoCompleteTextView machineDropdown, machinePartDropdown;
    ArrayAdapter<String> adapter, mpAdapter;
    List<String> machineNameList = new ArrayList<>();
    List<Integer> machineIdList = new ArrayList<>();
    List<String> machinePartNameList = new ArrayList<>();
    List<Integer> machinePartIdList = new ArrayList<>();
    Integer machineId, machinePartId;
    String machineName, machinePartName;
    ArrayList<Object> sliderImages;
    SliderAdapter sliderAdapter;
    WorkOrderModel workOrderModel;
    WorkModel workModel;
    boolean isMachinePartFilled = false;
    boolean isFinishing;
    private static final int CAMERA_REQUEST = 100;
    private long startTime = 0L;
    private long timeElapsed = 0L;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;
    private String formatedTime = "00:00";
    SimpleDateFormat dateFormat;
    String currentDate;
    Date dateIn;

    ViewPager2 viewPager;

    Integer currentId;

    Boolean isPhotoTaken = false;

    List<Bitmap> tempBitmapPhotos;
    private List<Uri> selectedImageUris = null;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_work);
        currentId =0;
        isFinishing = false;

        setupToolbar();
        machineDropdown = findViewById(R.id.machineId_spinner);
        adapter = new ArrayAdapter<>(AddWorkActivity.this, R.layout.dropdown_item, machineNameList);
        workModel = new WorkModel();
        fillMachineSpinner();

        machineDropdown.setAdapter(adapter);

        machineDropdown.setOnItemClickListener((parent, view, position, id) -> {
            machineName = parent.getItemAtPosition(position).toString();
            if (machinePartName != "-") {
                machineId = machineIdList.get(machineNameList.indexOf(machineName) - 1);
            } else {
                machineId = null;
            }
            fillMachinePartSpinner();
        });

        machinePartDropdown = findViewById(R.id.machinePartId_spinner);
        mpAdapter = new ArrayAdapter<>(AddWorkActivity.this, R.layout.dropdown_item, machinePartNameList);

        machinePartDropdown.setOnItemClickListener((parent, view, position, id) -> {
            machinePartName = parent.getItemAtPosition(position).toString();
            if (machinePartName != "-") {
                machinePartId = machinePartIdList.get(machinePartNameList.indexOf(machinePartName) - 1);
            } else {
                machinePartId = null;
            }
        });
        machinePartDropdown.setAdapter(mpAdapter);

        workDateEdt = findViewById(R.id.workEndDate_edittext);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));// Türkiye Saat Dilimi
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        currentDate = dateFormat1.format(new Date());
        workDateEdt.setText(currentDate);
        workTypeEdt = findViewById(R.id.workTitle_edittext);
        workDescEdt = findViewById(R.id.workDesc_edittext);
        dateIn = new Date();

        machineDropdownLayout = findViewById(R.id.machineId_spinner_layout);
        machineDropdownLayout = findViewById(R.id.machineId_spinner_layout);
        machinePartDropdownLayout = findViewById(R.id.machinePartId_spinner_layout);

        machineDropdownLayout.setBoxStrokeColor(Color.RED);
        machinePartDropdownLayout.setBoxStrokeColor(Color.RED);
        tempBitmapPhotos = new ArrayList<>();
        selectedImageUris = new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);
        sliderImages = new ArrayList<>();
        sliderAdapter = new SliderAdapter(AddWorkActivity.this, sliderImages);
        viewPager.setAdapter(sliderAdapter);

    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("İş Ekle");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fillMachineSpinner() {
        ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
        errorInterface.getNames().enqueue(new Callback<List<ErrorIdModel>>() {
            @Override
            public void onResponse(Call<List<ErrorIdModel>> call, Response<List<ErrorIdModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    machineNameList.add("-");
                    for (ErrorIdModel e : response.body()) {
                        machineIdList.add(e.getId());
                        machineNameList.add(e.getName());
                    }
                }
                getWorkOrderInfos();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ErrorIdModel>> call, Throwable t) {

            }
        });
    }

    private void fillMachinePartSpinner() {
        if (machineId != null) {
            machinePartIdList.clear();
            machinePartNameList.clear();
            ErrorInterface errorInterface = RetrofitClient.getApiServiceError();
            errorInterface.getPartNames(machineId).enqueue(new Callback<List<ErrorIdModel>>() {
                @Override
                public void onResponse(Call<List<ErrorIdModel>> call, Response<List<ErrorIdModel>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        machinePartNameList.add("-");
                        for (ErrorIdModel e : response.body()) {
                            machinePartIdList.add(e.getId());
                            machinePartNameList.add(e.getName());
                        }
                    }
                    getWorkOrderInfos();
                    mpAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<List<ErrorIdModel>> call, Throwable t) {

                }
            });
        }

    }

    public void getWorkOrderInfos() {
        WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
        workOrderInterface.getWorkOrder(getIntent().getIntExtra("workOrderId", 0))
                .enqueue(new Callback<WorkOrderModel>() {
                    @Override
                    public void onResponse(Call<WorkOrderModel> call, Response<WorkOrderModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            workOrderModel = response.body();
                            checkOpenedReports(workOrderModel.id);
                            checkUser();
                            if (workOrderModel.getMachineId() != null) {
                                machineId = workOrderModel.getMachineId();
                                if (workOrderModel.getMachinePartId() != null) {
                                    machinePartId = workOrderModel.getMachinePartId();
                                }
                                new Handler().postDelayed(() -> {
                                    setDefaultSelections();
                                }, 500);
                            }
                        }
                        else{
                            new AlertDialog.Builder(AddWorkActivity.this)
                                    .setTitle("Hata")
                                    .setMessage("İş emri kaldırıldı veya bir hata meydana geldi.")
                                    .setCancelable(false)
                                    .setPositiveButton("Tamam", (dialog, which) -> {
                                        Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WorkOrderModel> call, Throwable t) {
                    }
                });
    }

    private void checkOpenedReports(int id) {
        WorkInterface workInterface = RetrofitClient.getApiWorkService();
        workInterface.getWork(id).enqueue(new Callback<WorkModel>() {
            @Override
            public void onResponse(Call<WorkModel> call, Response<WorkModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    workModel = response.body();
                    if (workModel.isOpened()) {
                        workTypeEdt.setText(workModel.getTitle());
                        workDescEdt.setText(workModel.getDesc());

                    }
                } else {
                    Toast.makeText(AddWorkActivity.this, "Geçerli işe ait devam eden bir rapor bulunamadı" ,
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WorkModel> call, Throwable t) {

            }
        });
    }

    private void checkUser() {
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int UserId = sp.getInt("UserId", 0);
        if (workOrderModel.getUserId() != UserId) {
            new AlertDialog.Builder(this)
                    .setTitle("Hata")
                    .setMessage("Bu iş emri başka birisine atandı!")
                    .setCancelable(false)
                    .setPositiveButton("Tamam", (dialog, which) -> {
                        Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .show();

        }
    }

    private void setDefaultSelections() {
        if (machineId != null) {

            int machineIndex = machineIdList.indexOf(machineId) + 1; // çünkü "-" ekledin başa
            if (machineIndex > 0 && machineIndex < machineNameList.size()) {
                machineDropdown.setText(machineNameList.get(machineIndex), false);
                machineDropdown.setEnabled(false); // Kitlemek için
                machineDropdownLayout.setEnabled(false);
                // Dropdown tıklama olayını devre dışı bırak
                machineDropdown.setOnItemClickListener(null);
                if (!isMachinePartFilled) {
                    fillMachinePartSpinner();
                    isMachinePartFilled = true;
                }
            }
        }
        if (machinePartId != null) {
            int machineIndex = machinePartIdList.indexOf(machinePartId) + 1; // çünkü "-" ekledin başa
            if (machineIndex > 0 && machineIndex < machinePartNameList.size()) {
                machinePartDropdown.setText(machinePartNameList.get(machineIndex), false);
                machinePartDropdown.setEnabled(false); // Kitlemek için
                machinePartDropdownLayout.setEnabled(false);
                // Dropdown tıklama olayını devre dışı bırak
                machinePartDropdown.setOnItemClickListener(null);
            }
        }
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seç"), PICK_IMAGE_REQUEST);
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

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public void takePicture(View view) {
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }

        /*
         * if (ContextCompat.checkSelfPermission(this,
         * Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
         * PackageManager.PERMISSION_GRANTED) {
         * permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
         * }
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    private Uri captureImageUri;

    private void openCamera() {
        // Create content values with metadata for the new image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Camera_Image_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo captured by app");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        try {
            // Insert a new entry in the MediaStore and get the resulting URI
            captureImageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            // Create camera intent and tell it to save the full-resolution image to our URI
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);

            // Start camera activity without checking resolveActivity (optional safety
            // check)
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUris.add(data.getData());
            sliderImages.add(data.getData());
            sliderAdapter.setImageList(sliderImages);
            // Optionally show the selected image in an ImageView
            // imageView.setImageURI(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (captureImageUri != null) {
                // Add the URI to your collections
                selectedImageUris.add(captureImageUri);
                sliderImages.add(captureImageUri);
                sliderAdapter.setImageList(sliderImages);
                // Optional: Display a success message
                Toast.makeText(this, "Resim başarıyla eklendi.", Toast.LENGTH_SHORT).show();
            }
            /*
             * tempBitmapPhotos.add((Bitmap) data.getExtras().get("data"));
             * sliderImages.add( data.getExtras().get("data"));
             * sliderAdapter.setImageList(sliderImages);
             */
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

    public void hata_ekle(View view) {

        if (workTypeEdt.getText().toString().isEmpty() || workDescEdt.getText().toString().isEmpty()
                || workDateEdt.getText().toString().isEmpty()) {
            Toast.makeText(AddWorkActivity.this, "Gerekli alanları doldurunuz", Toast.LENGTH_LONG).show();
            return;
        }
        workModel.setMachineId(machineId);
        workModel.setMachinePartId(machinePartId);
        workModel.setTitle(workTypeEdt.getText().toString());
        workModel.setDesc(workDescEdt.getText().toString());
        workModel.setWorkOrderStartDate(workOrderModel.getWorkOrderTempStartDate());
        workModel.setWorkOrderEndDate(dateFormat.format(new Date()));
        workModel.setUserId(workOrderModel.getUserId());
        workModel.setWorkOrderId(workOrderModel.getId());


        workModel.setPastWork(false);
        workOrderModel.setWorkOrderEndDate(dateFormat.format(new Date()));

        isFinishing = true;
        add_work(workModel);
        updateOrders();
    }
    public void is_ekle(View view){
        if (workTypeEdt.getText().toString().isEmpty() || workDescEdt.getText().toString().isEmpty()
                || workDateEdt.getText().toString().isEmpty()) {
            Toast.makeText(AddWorkActivity.this, "Gerekli alanları doldurunuz", Toast.LENGTH_LONG).show();
            return;
        }
        workModel.setMachineId(machineId);
        workModel.setMachinePartId(machinePartId);
        workModel.setTitle(workTypeEdt.getText().toString());
        workModel.setDesc(workDescEdt.getText().toString());
        workModel.setWorkOrderStartDate(workOrderModel.getWorkOrderTempStartDate());
        workModel.setWorkOrderEndDate(dateFormat.format(new Date()));
        workModel.setUserId(workOrderModel.getUserId());
        workModel.setWorkOrderId(workOrderModel.getId());


        workModel.setPastWork(false);
        workOrderModel.setWorkOrderEndDate(dateFormat.format(new Date()));

        add_work(workModel);

    }

    private void updateOrders() {
        if (!workOrderModel.isClosed()) {
            workOrderModel.setClosed(true);
            WorkOrderInterface workOrderInterface = RetrofitClient.getApiWorkOrderService();
            workOrderInterface.updateWorkOrder(workOrderModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddWorkActivity.this, "İslem başarılı", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    public void add_work(WorkModel workModel) {
        if (isFinishing) {
           if(workModel.isOpened()){
                if(!workModel.isClosed()){
                    workModel.setClosed(true);
                    WorkInterface workInterface = RetrofitClient.getApiWorkService();
                    workInterface.updateWork(workModel).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d("Retrofit", "Response code: " + response.code());
                            Log.d("Retrofit", "Response message: " + response.message());
                            if (response.isSuccessful()) {
                                currentId = response.body();
                                resimleri_ekle();
                                Toast.makeText(AddWorkActivity.this, "İşlem Başarılı", Toast.LENGTH_LONG).show();
                                Intent sayfa = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                startActivity(sayfa);
                            } else {
                                Toast.makeText(AddWorkActivity.this, "Bir hata meydana geldi" + response.errorBody(),
                                        Toast.LENGTH_LONG).show();
                                try {
                                    String errorBody = response.errorBody() != null ? response.errorBody().string()
                                            : "No error body";
                                    Log.e("Retrofit", "Error body: " + errorBody);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                new AlertDialog.Builder(AddWorkActivity.this)
                                        .setTitle("Hata")
                                        .setMessage("İşlem sırasında bir hata meydana geldi işlem tamamlanamadı.")
                                        .setCancelable(false)
                                        .setPositiveButton("Tamam", (dialog, which) -> {
                                            Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Toast.makeText(AddWorkActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AddWorkActivity.this, "İşlem tamamlanıyor bekleyin", Toast.LENGTH_LONG).show();
                }
           }
           else{
               if (!workModel.isClosed()) {
                   workModel.setClosed(true);
                   workModel.setOpened(true);
                   WorkInterface workInterface = RetrofitClient.getApiWorkService();
                   workInterface.addWork(workModel).enqueue(new Callback<Integer>() {
                       @Override
                       public void onResponse(Call<Integer> call, Response<Integer> response) {
                           Log.d("Retrofit", "Response code: " + response.code());
                           Log.d("Retrofit", "Response message: " + response.message());
                           if (response.isSuccessful()) {
                               currentId = response.body();
                               resimleri_ekle();
                               Toast.makeText(AddWorkActivity.this, "İşlem Başarılı", Toast.LENGTH_LONG).show();
                               Intent sayfa = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                               startActivity(sayfa);
                           } else {
                               Toast.makeText(AddWorkActivity.this, "Bir hata meydana geldi" + response.errorBody(),
                                       Toast.LENGTH_LONG).show();
                               try {
                                   String errorBody = response.errorBody() != null ? response.errorBody().string()
                                           : "No error body";
                                   Log.e("Retrofit", "Error body: " + errorBody);

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                               new AlertDialog.Builder(AddWorkActivity.this)
                                       .setTitle("Hata")
                                       .setMessage("İşlem sırasında bir hata meydana geldi işlem tamamlanamadı.")
                                       .setCancelable(false)
                                       .setPositiveButton("Tamam", (dialog, which) -> {
                                           Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                           startActivity(intent);
                                           finish();
                                       })
                                       .show();
                           }
                       }

                       @Override
                       public void onFailure(Call<Integer> call, Throwable t) {
                           Toast.makeText(AddWorkActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                       }
                   });
               } else {
                   Toast.makeText(AddWorkActivity.this, "İşlem tamamlanıyor bekleyin", Toast.LENGTH_LONG).show();
               }
           }
        }
        else{
            if(workModel.isOpened()){
                WorkInterface workInterface = RetrofitClient.getApiWorkService();
                workInterface.updateWork(workModel).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.d("Retrofit", "Response code: " + response.code());
                        Log.d("Retrofit", "Response message: " + response.message());
                        if (response.isSuccessful()) {
                            currentId = response.body();
                            resimleri_ekle();
                            Toast.makeText(AddWorkActivity.this, "İşlem Başarılı", Toast.LENGTH_LONG).show();
                            Intent sayfa = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                            startActivity(sayfa);
                        } else {
                            Toast.makeText(AddWorkActivity.this, "Bir hata meydana geldi" + response.errorBody(),
                                    Toast.LENGTH_LONG).show();
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string()
                                        : "No error body";
                                Log.e("Retrofit", "Error body: " + errorBody);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new AlertDialog.Builder(AddWorkActivity.this)
                                    .setTitle("Hata")
                                    .setMessage("İşlem sırasında bir hata meydana geldi işlem tamamlanamadı.")
                                    .setCancelable(false)
                                    .setPositiveButton("Tamam", (dialog, which) -> {
                                        Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(AddWorkActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                workModel.setOpened(true);
                WorkInterface workInterface = RetrofitClient.getApiWorkService();
                workInterface.addWork(workModel).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.d("Retrofit", "Response code: " + response.code());
                        Log.d("Retrofit", "Response message: " + response.message());
                        if (response.isSuccessful()) {
                            currentId = response.body();
                            resimleri_ekle();
                            Toast.makeText(AddWorkActivity.this, "İşlem Başarılı", Toast.LENGTH_LONG).show();
                            Intent sayfa = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                            startActivity(sayfa);
                        } else {
                            Toast.makeText(AddWorkActivity.this, "Bir hata meydana geldi" + response.errorBody(),
                                    Toast.LENGTH_LONG).show();
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string()
                                        : "No error body";
                                Log.e("Retrofit", "Error body: " + errorBody);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            new AlertDialog.Builder(AddWorkActivity.this)
                                    .setTitle("Hata")
                                    .setMessage("İşlem sırasında bir hata meydana geldi işlem tamamlanamadı.")
                                    .setCancelable(false)
                                    .setPositiveButton("Tamam", (dialog, which) -> {
                                        Intent intent = new Intent(AddWorkActivity.this, WorkOrdersActivity.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(AddWorkActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public void resimleri_ekle() {
        byte[] imageBytes;
        String base64Image;
        List<String> base64images = new ArrayList<>();
        if (tempBitmapPhotos.size() > 0) {

            for (Bitmap bb : tempBitmapPhotos) {
                imageBytes = convertBitmapToByteArray(bb);
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                base64images.add(base64Image);
            }

        }
        if (selectedImageUris.size() > 0) {
            for (Uri uu : selectedImageUris) {
                try {
                    imageBytes = convertImageToByteArray(uu);
                    base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    base64images.add(base64Image);
                } catch (IOException e) {
                    Toast.makeText(AddWorkActivity.this, "Resim yüklenirken hata oluştu: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
        if (base64images.size() > 0) {
            WorkImagePostModel workImagePostModel = new WorkImagePostModel(base64images, currentId);
            ImageApiInterface imageApiInterface = RetrofitClient.getApiImageService();
            imageApiInterface.addImageDataWork(workImagePostModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddWorkActivity.this, "Resimler başarıyla eklendi.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddWorkActivity.this, "Resimler eklenirken bir hata meydana geldi." + response.errorBody(),
                                Toast.LENGTH_LONG).show();
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string()
                                    : "No error body";
                            Log.e("Retrofit", "Error body: " + errorBody);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddWorkActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void fotografKaldir(View view){
        if(sliderImages != null && sliderImages.size() > 0){
            new AlertDialog.Builder(AddWorkActivity.this)
                    .setTitle("Uyarı")
                    .setMessage("Bu fotoğrafı silmek istediğinizden emin misiniz.")
                    .setCancelable(false)
                    .setPositiveButton("Sil", (dialog, which) -> {
                        sliderImages.remove(viewPager.getCurrentItem());
                        selectedImageUris.remove(viewPager.getCurrentItem());
                        sliderAdapter.setImageList(sliderImages);
                        Toast.makeText(AddWorkActivity.this, "Fotoğraf silindi", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("İptal", (dialog, which) -> {
                        // Kullanıcı iptal ettiğinde yapılacak işlemler
                    })
                    .show();

        }
        else{
            new AlertDialog.Builder(AddWorkActivity.this)
                    .setTitle("Uyarı")
                    .setMessage("Herhangi bir fotoğraf eklenmedi.")
                    .setCancelable(false)
                    .setPositiveButton("Tamam", (dialog, which) -> {
                        sliderImages.remove(viewPager.getCurrentItem());
                        selectedImageUris.remove(viewPager.getCurrentItem());
                        sliderAdapter.setImageList(sliderImages);
                    })
                    .show();
        }
    }
}