package com.example.staj_deneme.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.InterFaces.UserApiInterface;
import com.example.staj_deneme.Models.UserModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRegisterActivity extends AppCompatActivity {
    UserModel userModel;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.namer_edittext);
        password = findViewById(R.id.passwordr_edittext);
        userModel = new UserModel();
    }
    public void kaydol(View view){
        String kk = email.getText().toString().trim();
        userModel.setEmail(email.getText().toString().trim());
        userModel.setPasswordHash(password.getText().toString().trim());
        UserApiInterface userApiInterface = RetrofitClient.getApiServiceUser();
        userApiInterface.addUser(userModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.e("hadi","oglus");
                }
                else{
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
                Log.e("hadi",t.getMessage());
            }
        });
    }
}