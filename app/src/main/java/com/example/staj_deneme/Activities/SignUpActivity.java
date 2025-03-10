package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText kullaniciNo,kullaniciEmail,kullaniciSifre;
    TextView hataMesaji;
    FirebaseAuth myAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        kullaniciEmail=findViewById(R.id.kullaniciEmail_edt);
        kullaniciNo=findViewById(R.id.kullaniciNo_edt);
        kullaniciSifre=findViewById(R.id.kullaniciSifre_edt);
        hataMesaji = findViewById(R.id.hataMesaji_textView);
        firestore = FirebaseFirestore.getInstance();
        myAuth = FirebaseAuth.getInstance();
    }
    public void kaydol(View view){
        String email= kullaniciEmail.getText().toString().trim();
        String sifre= kullaniciSifre.getText().toString().trim();
        String no= kullaniciNo.getText().toString().trim();
        if(email.isEmpty() || sifre.isEmpty() || no.isEmpty()){
            hataMesaji.setText("Herhangi bir alan boş olamaz");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }
        if(!isInt(no)){
            hataMesaji.setText("Kullanıcı numarası tam sayı olmalı");
            hataMesaji.setVisibility(View.VISIBLE);
            return;
        }
        myAuth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(task-> {
                    if(task.isSuccessful())
                    {
                        Map<String,Object> userData=new HashMap<>();
                        userData.put("Email",email);

                        firestore.collection("Kullanicilar").document(kullaniciNo.getText().toString().trim()).set(userData).addOnSuccessListener(e->{
                            Intent sayfa = new Intent(this, LoginActivity.class);
                            startActivity(sayfa);
                                }).addOnFailureListener(e->{
                            hataMesaji.setText("HATA: " + e.getMessage());
                            hataMesaji.setVisibility(View.VISIBLE);
                        });
                    }
                    else{
                        hataMesaji.setVisibility(View.VISIBLE);
                        hataMesaji.setText("HATA: " + task.getException().getMessage());

                    }
                });
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