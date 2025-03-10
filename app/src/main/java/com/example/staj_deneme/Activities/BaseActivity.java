package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.staj_deneme.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_base);


    }
    @Override
    public void setContentView(int layoutResID) {
        // 1. Ana layout'u inflate et
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        // 2. İçerik container'ını bul
        FrameLayout contentContainer = drawerLayout.findViewById(R.id.content_container);

        // 3. Alt sınıf layout'unu içerik container'a ekle
        getLayoutInflater().inflate(layoutResID, contentContainer, true);

        // 4. Temel layout'u ayarla
        super.setContentView(drawerLayout);

        // 5. Navigation elemanlarını bul ve ayarla
        setupNavigationElements();
    }
    private void setupNavigationElements() {
        navigationView = findViewById(R.id.navigationView);
        materialToolbar = findViewById(R.id.materialToolbar);

        // ActionBarDrawerToggle kurulumu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                materialToolbar,
                R.string.drawer_close,
                R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Toolbar click listener
        materialToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.share) {
                Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        // Navigation item listener
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent sayfa = null;

            if (item.getItemId() == R.id.home) {
                sayfa = new Intent(getApplicationContext(), TestActivity.class);
            } else if (item.getItemId() == R.id.person) {
                sayfa = new Intent(getApplicationContext(), ErrorDetailsActivity.class);
            } else if (item.getItemId() == R.id.nufus) {
                sayfa = new Intent(getApplicationContext(), MainActivity.class);
            } else if (item.getItemId() == R.id.tarihi) {
                sayfa = new Intent(getApplicationContext(), MainActivity.class);
            }

            if (sayfa != null) {
                startActivity(sayfa);
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }
}