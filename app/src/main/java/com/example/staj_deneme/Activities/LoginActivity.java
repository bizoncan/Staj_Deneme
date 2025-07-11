package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    TextView hataMesaji;
    EditText kullaniciNo,sifre;
    FirebaseAuth myAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myAuth = FirebaseAuth.getInstance();
        hataMesaji = findViewById(R.id.hataMesaji_textView);
        kullaniciNo = findViewById(R.id.kullaniciNo_edt);
        sifre = findViewById(R.id.kullaniciSifre_edt);
        firestore = FirebaseFirestore.getInstance();
    }
    public void giris(View view){
        if(!isInt(kullaniciNo.getText().toString().trim())){
            hataMesaji.setText("Kullanıcı numarası tam sayı olmalı");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }

        firestore.collection("Kullanicilar").document(kullaniciNo.getText().toString().trim()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                String email = documentSnapshot.getString("Email").trim();
                if(email==null) {
                    hataMesaji.setVisibility(View.VISIBLE);
                    hataMesaji.setText("HATA: Kullanıcı bulunamadı");
                }
                else{
                    emailIleGiris(email);
                    Toast.makeText(this,"Giriş başarılı",Toast.LENGTH_LONG).show();
                }
            }
            else{
                hataMesaji.setVisibility(View.VISIBLE);
                hataMesaji.setText("HATA: Bu kullanıcı numarası bulunamadı.");
            }
        }).addOnFailureListener(e->{
            hataMesaji.setVisibility(View.VISIBLE);
            hataMesaji.setText("HATA: "+ e.getMessage());
        });
    }
    public void emailIleGiris(String email){
        myAuth.signInWithEmailAndPassword(email,sifre.getText().toString().trim()).addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Toast.makeText(this,"Giriş başarılı",Toast.LENGTH_LONG).show();
                Intent sayfa = new Intent(this, TestActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("kullaniciNo", kullaniciNo.getText().toString().trim());
                editor.apply();
                startActivity(sayfa);
            }
            else{
                Toast.makeText(this,"Giriş başarısız"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        })   ;
    }
    public void hesapOlustur(View view){
        Intent sayfa = new Intent(this, SignUpActivity.class);
        startActivity(sayfa);
    }
    public boolean isInt(String s){
        try{
            int no = Integer.parseInt(s);
        }catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }
}