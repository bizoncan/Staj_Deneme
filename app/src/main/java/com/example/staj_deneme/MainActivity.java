package com.example.staj_deneme;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView machineListView;
    MachineAdapter adapter;
    List<MachineModel> machineList;
    FirebaseFirestore firestore;
    String inputID;
    TextView arizaTuru;
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
        arizaTuru=findViewById(R.id.arizaTuru);
        machineListView = findViewById(R.id.machineList);
        machineList = new ArrayList<>();
        adapter = new MachineAdapter(this,machineList);
        machineListView.setAdapter(adapter);
        firestore = FirebaseFirestore.getInstance();
        inputID= getIntent().getStringExtra("QR");

        fetchMachine();
    }
    private void fetchMachine(){
        CollectionReference collection = firestore.collection("Makineler"); // Koleksiyon adı
        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            machineList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String documentId = document.getId();
                Log.d("Fireeee",documentId);
                if(documentId.equals(inputID)){
                    MachineModel model = document.toObject(MachineModel.class);

                    machineList.add(model);
                    fetchSubCollection(documentId);
                }

            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
    private void fetchSubCollection(String documentId) {
        CollectionReference collection = firestore.collection("Makineler/"+documentId+"/ArizaKayitlari");
        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                 arizaTuru.setText(document.getString("arizaTuru"));
                }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}