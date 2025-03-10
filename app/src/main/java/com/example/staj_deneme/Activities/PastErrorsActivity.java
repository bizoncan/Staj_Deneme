package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PastErrorsActivity extends AppCompatActivity {
    private String documentId;
    private ListView listAriza;
    private List<String> errorList;
    private List<String> zamanList;
    private List<String> errorDescList;
    BaseAdapter adapter;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_past_errors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firestore = FirebaseFirestore.getInstance();
        documentId= getIntent().getStringExtra("inputID");
        listAriza = findViewById(R.id.listAriza);
        errorList = new ArrayList<>();
        zamanList = new ArrayList<>();
        errorDescList = new ArrayList<>();
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return errorList.size();
            }

            @Override
            public Object getItem(int position) {
                return errorList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null){
                    convertView= LayoutInflater.from(PastErrorsActivity.this).inflate(R.layout.past_errors_layout, parent, false);
                }
                TextView arizaTuru= convertView.findViewById(R.id.arizaT_textview);
                TextView arizaZamani = convertView.findViewById(R.id.arizaZaman_textview);
                TextView arizaAciklama = convertView.findViewById(R.id.arizaAcikla_textview);

                arizaTuru.setText(errorList.get(position));
                arizaZamani.setText(zamanList.get(position));
                arizaAciklama.setText(errorDescList.get(position));
                return convertView ;
            }
        };
        firestore = FirebaseFirestore.getInstance();
        listAriza.setAdapter(adapter);
        fetchSubCollection();
    }
    private void fetchSubCollection() {
        CollectionReference collection = firestore.collection("Makineler/"+documentId+"/ArizaKayitlari");
        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            errorList.clear();
            zamanList.clear();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String a_t = document.getString("ArizaTuru");
                String a_z = document.getString("ArizaZamani");
                String a_a = document.getString("ArizaAciklama");

                if(a_t != null && a_z!= null){
                    errorList.add("Arıza türü: "+a_t);
                    zamanList.add("Arıza zamanı: "+a_z);
                    errorDescList.add("Arıza açıklaması: "+a_a);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
    public void geri(View view){
        Intent sayfa=new Intent(this, MainActivity.class);
        sayfa.putExtra("QR",documentId);
        startActivity(sayfa);
    }
}