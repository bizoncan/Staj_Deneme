package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.Models.NotificationModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.skydoves.transformationlayout.TransformationLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    TextView notificationText;
    MenuItem menuItem;

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
        notificationText = findViewById(R.id.notificationCount_textview);


        startDatabasePolling();

    }


    private void setupNavigationElements() {
        navigationView = findViewById(R.id.navigationView);
        materialToolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(materialToolbar);
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
            if (item.getItemId() == R.id.notification) {

                onNotificationButtonClick(null);
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

    public interface NotificationButtonListener {
        void onNotificationButtonClicked();
    }

    private NotificationButtonListener notificationListener;

    public void setNotificationButtonListener(NotificationButtonListener listener) {
        this.notificationListener = listener;
    }

    // When the button is clicked in BaseActivity
    public void onNotificationButtonClick(View view) {
        if (notificationListener != null) {
            notificationListener.onNotificationButtonClicked();
        }
    }

    public void checkForUpdates() {
        RecieveNotificationInterface recieveNotificationInterface = RetrofitClient.getApiServiceNotification();
        recieveNotificationInterface.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (response.body() != null && response.isSuccessful()) {

                    notificationText.setText(Integer.toString(response.body().size()));
                }

            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {

            }
        });
    }

    private void startDatabasePolling() {
        final int POLL_INTERVAL = 2500;

        Handler handler = new Handler();
        Runnable pollRunnable = new Runnable() {
            @Override
            public void run() {
                checkForUpdates();
                handler.postDelayed(this, POLL_INTERVAL); // Tekrar çalıştır
            }
        };

        handler.post(pollRunnable); // Başlat
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        new Handler().postDelayed(this::updateNotificationBadgePosition, 100);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        updateNotificationBadgePosition();
        // Position notification count text after menu is prepared
        final MenuItem notificationItem = menu.findItem(R.id.notification);
        if (notificationItem != null && notificationText != null) {
            notificationText.post(() -> {
                // Get action view
                View actionView = materialToolbar.findViewById(notificationItem.getItemId());
                if (actionView == null) {
                    // Try finding the notification view in the overflow
                    actionView = materialToolbar.findViewById(
                            androidx.appcompat.R.id.action_mode_bar_stub);
                }

                if (actionView != null) {
                    // Get location
                    int[] location = new int[2];
                    actionView.getLocationInWindow(location);

                    // Position the badge
                    ConstraintLayout.LayoutParams params =
                            (ConstraintLayout.LayoutParams) notificationText.getLayoutParams();
                    params.leftMargin=0;

                    params.topMargin = 0;

                    // Set explicit x,y coordinates
                    notificationText.setX(location[0]+73 - notificationText.getWidth() / 2);
                    notificationText.setY(location[1]-7 - notificationText.getHeight() / 2);

                    // Make visible
                    notificationText.setVisibility(View.VISIBLE);
                }
            });
        }

        return result;
    }
    private void updateNotificationBadgePosition() {
        if (notificationText != null && materialToolbar != null) {
            MenuItem notificationItem = materialToolbar.getMenu().findItem(R.id.notification);
            if (notificationItem != null) {
                materialToolbar.post(() -> {
                    // Try to find the action view in the toolbar
                    for (int i = 0; i < materialToolbar.getChildCount(); i++) {
                        View child = materialToolbar.getChildAt(i);
                        // Look for the overflow menu or action item
                        if (child instanceof ActionMenuView) {
                            ActionMenuView menuView = (ActionMenuView) child;
                            for (int j = 0; j < menuView.getChildCount(); j++) {
                                View menuItem = menuView.getChildAt(j);
                                if (menuItem.getId() == R.id.notification) {
                                    // Found it, now position the badge
                                    int[] location = new int[2];
                                    menuItem.getLocationInWindow(location);

                                    // Position badge directly using X and Y
                                    notificationText.setX(location[0] - 5);
                                    notificationText.setY(location[1] - 5);
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        }
    }


}