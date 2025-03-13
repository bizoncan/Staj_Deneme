package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.staj_deneme.InterFaces.UserExistCallback;
import com.example.staj_deneme.Models.UserModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.protobuf.StringValue;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestRegisterActivity extends AppCompatActivity {
    UserModel userModel;
    EditText emailEditText,passwordEditText,usernameEditText;
    TextView hataMesaji;

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
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.passwordr_edittext);
        usernameEditText = findViewById(R.id.namer_edittext);
        userModel = new UserModel();
        hataMesaji = findViewById(R.id.hataMesaji_textView);
    }
    public void kaydol(View view){
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() ){
            Toast.makeText(TestRegisterActivity.this, "Bir alanı boş bıraktınız", Toast.LENGTH_SHORT).show();
            hataMesaji.setText("Bir alanı boş bıraktınız");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(TestRegisterActivity.this, "Lütfen geçerli bir email giriniz", Toast.LENGTH_LONG).show();
            hataMesaji.setText("Bir alanı boş bıraktınız");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }
        if (!passwordCheck(password)){
            return;
        }
        isUserExist(username, email, new UserExistCallback() {
            @Override
            public void onResult(boolean exists) {
                if(!exists){
                    userModel.setEmail(email);
                    userModel.setPasswordHash(password);
                    userModel.setUserName(username);
                    UserApiInterface userApiInterface = RetrofitClient.getApiServiceUser();
                    userApiInterface.addUser(userModel).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(TestRegisterActivity.this,"Hesap oluşturma başarılı",Toast.LENGTH_LONG).show();
                                Intent sayfa = new Intent(TestRegisterActivity.this,TestLoginActivity.class);
                                startActivity(sayfa);
                            }
                            else{
                                try {
                                    String errorBody = response.errorBody() != null ?
                                            response.errorBody().string() : "No error body";
                                    Log.e("Retrofit", "Error body: " + errorBody);
                                    Toast.makeText(TestRegisterActivity.this, errorBody,Toast.LENGTH_LONG).show();


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("hadi",t.getMessage());
                            Toast.makeText(TestRegisterActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    Toast.makeText(TestRegisterActivity.this,"Bu kullanıcı adı yada email başka bir kullanıcı tarafından kullanılıyor.",Toast.LENGTH_LONG).show();
                    hataMesaji.setText("Bu kullanıcı adı yada email başka bir kullanıcı tarafından kullanılıyor");
                    hataMesaji.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public  boolean passwordCheck(String p){
        String specialCharacters = "!@#$%^&*()_+{}[]|:;<>,.?/~`";
        boolean cc = false;
        if (!p.chars().anyMatch(Character::isAlphabetic)){
            Toast.makeText(TestRegisterActivity.this,"Şifre en az bir harf içermeli",Toast.LENGTH_LONG).show();
            hataMesaji.setText("Şifre en az bir harf içermeli");
            hataMesaji.setVisibility(View.VISIBLE);
            return false;
        }
        if (p.length()<9){
           Toast.makeText(TestRegisterActivity.this,"Şifre dokuz karakterden uzun olamalıdır",Toast.LENGTH_LONG).show();
            hataMesaji.setText("Şifre dokuz karakterden uzun olamalıdır");
            hataMesaji.setVisibility(View.VISIBLE);
           return false;
       }
       for(char ch: specialCharacters.toCharArray()) {
           if (p.contains(String.valueOf(ch))) {
               cc = true;
               break;
           }

       }
       if (!cc) {
           Toast.makeText(TestRegisterActivity.this,"Şifre en az bir özel karakter içermeli",Toast.LENGTH_LONG).show();
           hataMesaji.setText("Şifre en az bir özel karakter içermeli");
           hataMesaji.setVisibility(View.VISIBLE);
           return false;
       }
       if (!p.chars().anyMatch(Character::isUpperCase)){
           Toast.makeText(TestRegisterActivity.this,"Şifre en az bir büyük harf içermeli",Toast.LENGTH_LONG).show();
           hataMesaji.setText("Şifre en az bir büyük harf içermeli");
           hataMesaji.setVisibility(View.VISIBLE);
           return false;
       }
        if (!p.chars().anyMatch(Character::isDigit)){
            Toast.makeText(TestRegisterActivity.this,"Şifre en az bir rakam içermeli",Toast.LENGTH_LONG).show();
            hataMesaji.setText("Şifre en az bir rakam içermeli");
            hataMesaji.setVisibility(View.VISIBLE);
            return false;
        }

       return true;
    }
    public  void isUserExist(String username, String email,UserExistCallback userExistCallback){

        UserApiInterface userApiInterface = RetrofitClient.getApiServiceUser();
        userApiInterface.checkUser(username,email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() != null){
                    userExistCallback.onResult(response.body());
                }
                else{
                    userExistCallback.onResult(true);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                userExistCallback.onResult(true);
            }
        });
    }
}