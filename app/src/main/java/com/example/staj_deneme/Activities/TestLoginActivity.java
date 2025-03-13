package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.InterFaces.UserApiInterface;
import com.example.staj_deneme.Models.ResponseModel;
import com.example.staj_deneme.Models.UserModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestLoginActivity extends AppCompatActivity {
    EditText userNameEditText,passwordEditText;
    TextView hataMesaji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userNameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.kullaniciSifre_edittext);
        hataMesaji = findViewById(R.id.hataMesaji_textView);
    }
    public void giris(View view) {
        String username = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(TestLoginActivity.this, "Bir alanı boş bıraktınız", Toast.LENGTH_SHORT).show();
            hataMesaji.setText("Bir alanı boş bıraktınız");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }
        UserApiInterface userApiInterface = RetrofitClient.getApiServiceUser();
        userApiInterface.getUsers(username, password).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseModel responseModel = response.body();
                    if(responseModel.isSuccess()){
                        SharedPreferences sharedPreferences= getSharedPreferences("UserPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Username",username);
                        editor.apply();
                        Toast.makeText(TestLoginActivity.this,"Giriş başarılı",Toast.LENGTH_LONG).show();
                        Intent sayfa = new Intent(TestLoginActivity.this,TestActivity.class);
                        startActivity(sayfa);
                    }
                    else{
                        hataMesaji.setText("Hatalı deneme: " + responseModel.getMessage());
                        hataMesaji.setVisibility(View.VISIBLE);
                    }
                } else {
                    hataMesaji.setText("Bir hata meydana geldi: " + response.errorBody().toString());
                    hataMesaji.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                hataMesaji.setText("Bir hata meydana geldi: " + t.getMessage());
                hataMesaji.setVisibility(View.VISIBLE);
            }
        });

    }
    public void hesapOlustur(View view){
        Intent sayfa = new Intent(TestLoginActivity.this,TestRegisterActivity.class);
        startActivity(sayfa);
    }
}