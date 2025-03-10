package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.Adapter.MachineAdapter;
import com.example.staj_deneme.Models.MachineModel;
import com.example.staj_deneme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView machineListView;
    MachineAdapter adapter;
    List<MachineModel> machineList;
    FirebaseFirestore firestore;
    String inputID;
    EditText arizaTuru;
    EditText arizaAciklama;
    FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        arizaTuru=findViewById(R.id.arizaTuru_Edt);
        arizaAciklama=findViewById(R.id.errorDesc_edt);
        machineListView = findViewById(R.id.machineInfo_Lst);
        machineList = new ArrayList<>();
        adapter = new MachineAdapter(this,machineList);
        machineListView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();
        inputID= getIntent().getStringExtra("QR");
        myAuth = FirebaseAuth.getInstance();
        fetchMachine();

    }
    private void fetchMachine(){
        FirebaseUser currentUser= myAuth.getCurrentUser();
        if(currentUser != null){
            
        }
        firestore.collection("Makineler").document(inputID).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                MachineModel machineModel = documentSnapshot.toObject(MachineModel.class);
                machineList.add(machineModel);
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(this,"Makine bulunamadı",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void addError(View view){
        String a_t = arizaTuru.getText().toString();
        String a_d = arizaAciklama.getText().toString();
        if(a_t.isEmpty()){
            Toast.makeText(this,"Arıza türü boş olamaz.",Toast.LENGTH_LONG).show();
            return;
        }
        firestore.collection("Makineler").document(inputID).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String formattedDate = sdf.format(new Date());
                HashMap<String, Object> arizaData = new HashMap<>();
                arizaData.put("ArizaTuru", a_t);
                arizaData.put("ArizaZamani", formattedDate );
                arizaData.put("ArizaAciklama", a_d);
                firestore.collection("Makineler").document(inputID)
                        .collection("ArizaKayitlari")
                        .add(arizaData)
                        .addOnSuccessListener(documentReference ->
                                Toast.makeText(this, "Arıza kaydı eklendi", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Hata oluştu: " + e.getMessage(), Toast.LENGTH_LONG).show()
                        );
            } else {
                Toast.makeText(this, "Makine bulunamadı", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void PastErrors(View view){
        firestore.collection("Makineler").document(inputID).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Intent sayfa = new Intent(this, PastErrorsActivity.class);
                sayfa.putExtra("inputID",inputID);
                startActivity(sayfa);
            }
            else{
                Toast.makeText(this,"Makine bulunamadı.",Toast.LENGTH_LONG).show();
            }
                });

    }
    public void geri(View view){
        Intent sayfa=new Intent(this, QrScanActivity.class);
        startActivity(sayfa);
    }
}