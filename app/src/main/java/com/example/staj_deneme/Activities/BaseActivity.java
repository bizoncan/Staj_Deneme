package com.example.staj_deneme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.staj_deneme.InterFaces.MachineApiInterface;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.Models.MachineAndMachinePartModel;
import com.example.staj_deneme.Models.NotificationModel;
import com.example.staj_deneme.Models.NotificationResponseModel;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.skydoves.transformationlayout.TransformationLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    boolean opened = false;
    TransformationLayout transformationLayout;
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    TextView notificationText,usernameTextView;
    MenuItem menuItem;
    List<String> idList,titleList,machineIdList,machinePartIdList,descList,typeList,workOrderIdList,userIdList;
    BaseAdapter adadpter;
    int userId;
    ListView notificationListView;
    int not_size = 0;

    private Handler pollingHandler;
    private Runnable pollingRunnable;
    private boolean isPollingRunning = false;
    private static final int POLL_INTERVAL = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_base);
        typeList = new ArrayList<>();
        idList = new ArrayList<>();
        titleList = new ArrayList<>();
        machineIdList = new ArrayList<>();
        machinePartIdList = new ArrayList<>();
        descList = new ArrayList<>();
        workOrderIdList = new ArrayList<>();
        userIdList = new ArrayList<>();
        adadpter = new BaseAdapter() {
            @Override
            public int getCount() {
                return titleList.size();
            }

            @Override
            public Object getItem(int position) {
                return titleList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(BaseActivity.this).inflate(R.layout.notification_layout,parent,false);
                }

                TextView title_text = convertView.findViewById(R.id.title_textview);
                TextView machineId_text = convertView.findViewById(R.id.machineId_textview);
                TextView machinePartId_text= convertView.findViewById(R.id.machinePartId_textview);
                TextView desc_text = convertView.findViewById(R.id.description_textview);
                TextView type_text = convertView.findViewById(R.id.type_txt);
                Log.e("TAG", "getView: "+titleList.get(position));
                Log.e("TAG", "getView: "+machineIdList.get(position));
                Log.e("TAG", "getView: "+machinePartIdList.get(position));
                Log.e("TAG", "getView: "+descList.get(position));
                Log.e("TAG", "getView: "+typeList.get(position));
                title_text.setText(titleList.get(position));
                machineId_text.setText(machineIdList.get(position));
                machinePartId_text.setText(machinePartIdList.get(position));
                desc_text.setText(descList.get(position));
                type_text.setText(typeList.get(position));
                return convertView;
            }
        };
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        userId = sharedPreferences.getInt("UserId",0);
        initializePolling();
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


        //startDatabasePolling();

    }


    private void setupNavigationElements() {
        navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        materialToolbar = findViewById(R.id.materialToolbar);
        transformationLayout = findViewById(R.id.transition_layout);
        usernameTextView = headerView.findViewById(R.id.textViewUsername);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        usernameTextView.setText(sharedPreferences.getString("Username",""));
        notificationListView = findViewById(R.id.notificationsbase_listview);
        notificationListView.setAdapter(adadpter);
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (typeList.get(position)== "Arıza"){
                    Intent sayfa = new Intent(BaseActivity.this, NotificationAddErrorActivity.class);
                    sayfa.putExtra("machineID",machineIdList.get(position));
                    sayfa.putExtra("machinePartID",machinePartIdList.get(position));
                    sayfa.putExtra("notificationId",idList.get(position));
                    startActivity(sayfa);

                }
                else {
                    if(Integer.parseInt(userIdList.get(position)) != 0){
                        Intent sayfa = new Intent(BaseActivity.this, AddWorkActivity.class);
                        sayfa.putExtra("workOrderId",Integer.parseInt(workOrderIdList.get(position)));
                        startActivity(sayfa);
                    }
                    else{
                        Intent sayfa = new Intent(BaseActivity.this, WorkOrderDetailActivity.class);
                        sayfa.putExtra("workOrderId",Integer.parseInt(workOrderIdList.get(position)));
                        startActivity(sayfa);
                    }
                }
            }
        });

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

            /*if (item.getItemId() == R.id.home) {
                sayfa = new Intent(getApplicationContext(), TestActivity.class);
            } else if (item.getItemId() == R.id.person) {
                sayfa = new Intent(getApplicationContext(), ErrorDetailsActivity.class);
            } else if (item.getItemId() == R.id.nufus) {
                sayfa = new Intent(getApplicationContext(), AddErrorManuelActivity.class);
            } else*/ if (item.getItemId() == R.id.tarihi) {
                sayfa = new Intent(getApplicationContext(), TestLoginActivity.class);
            }
            else if (item.getItemId() == R.id.workorders) {
                sayfa = new Intent(getApplicationContext(), WorkOrdersActivity.class);
            }
            /*else if (item.getItemId() == R.id.myworks) {
                sayfa = new Intent(getApplicationContext(), TakenWorksActivity.class);
            }*/
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
        if(opened){
            transformationLayout.startTransform();

            opened=false;
        }
        else{
            transformationLayout.finishTransform();
            opened=true;

        }
    }

    public void checkForUpdates() {
        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if(response.body()!= null && response.isSuccessful()){
                    int ss = 0;
                    List<NotificationModel> notifications = response.body().getNotificationList();
                    List<WorkOrderModel> works = response.body().getWorkNotificationList();
                    for(NotificationModel n: notifications){
                        if(!idList.contains(Integer.toString(n.getId()))){
                            typeList.add("Arıza");
                            idList.add(Integer.toString(n.getId()));
                            titleList.add(n.getTitle());
                            workOrderIdList.add("0");
                            userIdList.add("0");
                            if(n.getMachineId()== null)machineIdList.add(null);
                            else machineIdList.add(Integer.toString(n.getMachineId()));
                            if(n.getMachinePartId()==null)machinePartIdList.add(null) ;
                            else machinePartIdList.add(Integer.toString(n.getMachinePartId()));
                            descList.add(n.getDescription());
                            not_size = response.body().getNotificationList().size() +
                                    response.body().getWorkNotificationList().size();
                            notificationText.setText(Integer.toString(not_size));
                            checkMachineNames();
                        }

                    }
                    for(WorkOrderModel w: works){
                        if (!w.isOpened()){
                            if(!idList.contains(Integer.toString(w.getId()))){
                                typeList.add("İş Emri");
                                idList.add(Integer.toString(w.getId()));
                                titleList.add("Başlık: "+w.getTitle());
                                workOrderIdList.add(Integer.toString(w.getId()));
                                userIdList.add("0");
                                if(w.getMachineId()== null)machineIdList.add(null);
                                else machineIdList.add(Integer.toString(w.getMachineId()));
                                if(w.getMachinePartId()==null)machinePartIdList.add(null) ;
                                else machinePartIdList.add(Integer.toString(w.getMachinePartId()));
                                descList.add("Açıklama: "+w.getDesc());
                                checkMachineNames();
                            }
                        }
                        else {
                            if (w.userId!=null &&w.userId == userId){
                                if(!idList.contains(Integer.toString(w.getId()))){
                                    typeList.add("İş Emri");
                                    idList.add(Integer.toString(w.getId()));
                                    titleList.add("Başlık: "+w.getTitle());
                                    workOrderIdList.add(Integer.toString(w.getId()));
                                    userIdList.add(Integer.toString(w.getUserId()));
                                    if(w.getMachineId()== null)machineIdList.add(null);
                                    else machineIdList.add(Integer.toString(w.getMachineId()));
                                    if(w.getMachinePartId()==null)machinePartIdList.add(null) ;
                                    else machinePartIdList.add(Integer.toString(w.getMachinePartId()));
                                    descList.add("Açıklama: "+w.getDesc());
                                    checkMachineNames();
                                }

                            }
                            else {
                                ss=ss+1;
                            }
                        }


                    }
                    not_size = response.body().getNotificationList().size() +
                            response.body().getWorkNotificationList().size()-ss;
                    notificationText.setText(Integer.toString(not_size));
                    adadpter.notifyDataSetChanged();
                }
                else{
                    try {
                        Log.e("API ERROR", "Response Code: " + response.code() +
                                " | Message: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

            }
        });

    }

    private void checkMachineNames() {
        MachineApiInterface machineApiInterface = RetrofitClient.getApiServiceMachine();
        machineApiInterface.getMachineWithParts().enqueue(new Callback<MachineAndMachinePartModel>() {
            @Override
            public void onResponse(Call<MachineAndMachinePartModel> call, Response<MachineAndMachinePartModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    MachineAndMachinePartModel machineAndMachinePartModels = response.body();

                        for(int i = 0; i< machineIdList.size(); i++){
                            if(machineIdList.get(i) != null && machineIdList.get(i).equals(Integer.toString(machineAndMachinePartModels.getMachineModels().get(0).getId()))){
                                machineIdList.set(i,machineAndMachinePartModels.getMachineModels().get(0).getName());
                            }
                            if(machinePartIdList.get(i) != null && machinePartIdList.get(i).equals(Integer.toString(machineAndMachinePartModels.getMachinePartModels().get(0).getId()))){
                                machinePartIdList.set(i,machineAndMachinePartModels.getMachinePartModels().get(0).getName());
                            }
                        }

                    adadpter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MachineAndMachinePartModel> call, Throwable t) {
                Log.e("API ERROR", t.getMessage());
            }
        });


    }

    private void initializePolling() {
        // Handler'ı Main Looper ile oluşturduğundan emin ol
        pollingHandler = new Handler(Looper.getMainLooper());
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                // Activity hala aktif mi kontrol et
                if (!isFinishing() && !isDestroyed() && isPollingRunning) {
                    Log.d("PollingDebug", "checkForUpdates çağrılıyor: " + BaseActivity.this.getClass().getSimpleName() + " Instance: " + BaseActivity.this.hashCode());
                    checkForUpdates();

                    // Sadece polling hala aktifse bir sonrakini zamanla
                    pollingHandler.postDelayed(this, POLL_INTERVAL);
                } else {
                    Log.d("PollingDebug", "Polling durdu veya Activity sonlanıyor: " + BaseActivity.this.getClass().getSimpleName());
                    isPollingRunning = false; // Bayrağı güncelle
                }
            }
        };
    }
    private void startDatabasePolling() {
        if (pollingHandler == null || pollingRunnable == null) {
            initializePolling();
        }

        // Zaten çalışmıyorsa ve başlatılabiliyorsa başlat
        if (!isPollingRunning && pollingHandler != null && pollingRunnable != null) {
            Log.d("PollingDebug", "Polling başlatılıyor: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
            isPollingRunning = true;
            // Önceki olası callback'leri temizle (garanti olsun)
            pollingHandler.removeCallbacks(pollingRunnable);
            // Runnable'ı hemen çalıştır ve döngüyü başlat
            pollingHandler.post(pollingRunnable);
        } else if (isPollingRunning) {
            Log.d("PollingDebug", "Polling zaten çalışıyor: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
        }
    }
    private void stopDatabasePolling() {
        if (pollingHandler != null && pollingRunnable != null) {
            Log.d("PollingDebug", "Polling durduruluyor: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
            isPollingRunning = false;
            pollingHandler.removeCallbacks(pollingRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ActivityLifecycle", "onResume: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
        // Activity ön plana geldiğinde polling'i başlat
        startDatabasePolling();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ActivityLifecycle", "onPause: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
        // Activity arka plana gittiğinde polling'i durdur
        stopDatabasePolling();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "onDestroy: " + this.getClass().getSimpleName() + " Instance: " + this.hashCode());
        // Activity yok edildiğinde polling'i kesin olarak durdur ve referansları temizle
        stopDatabasePolling();
        pollingHandler = null; // Garbage Collector'a yardımcı ol
        pollingRunnable = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        //new Handler().postDelayed(this::updateNotificationBadgePosition, 100);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);

        MenuItem notificationItem = menu.findItem(R.id.notification);
        if (notificationItem != null && notificationText != null) {
            // Wait until everything is laid out
            new Handler().postDelayed(()->{
                View notificationView = materialToolbar.findViewById(R.id.notification);
                if(notificationView != null){
                    int[] location = new int[2];
                    notificationView.getLocationOnScreen(location);

                    notificationText.setX(location[0]+(notificationView.getWidth()*0.05f));
                    notificationText.setY(location[1]-(notificationText.getHeight()*0.55f));
                    Log.e("Badge", "Badge positioned at: " + notificationText.getX() + ", " + notificationText.getY());

                }
                else {
                    Log.e("Badge", "Notification view not found directly");

                    // Try an alternative method - get the overflow menu button
                    View overflowButton = null;
                    for (int i = 0; i < materialToolbar.getChildCount(); i++) {
                        View child = materialToolbar.getChildAt(i);
                        if (child.getClass().getSimpleName().contains("OverflowMenuButton") ||
                                child.getClass().getSimpleName().contains("ActionMenuView")) {
                            overflowButton = child;
                            break;
                        }
                    }

                    if (overflowButton != null) {
                        int[] location = new int[2];
                        overflowButton.getLocationOnScreen(location);

                        // Position relative to the overflow button (slightly to the left)
                        notificationText.setX(location[0] - notificationText.getWidth() - 10);
                        notificationText.setY(location[1] - (notificationText.getHeight() / 2));
                        notificationText.setVisibility(View.VISIBLE);

                        Log.d("Badge", "Badge positioned relative to overflow button");
                    } else {
                        Log.e("Badge", "Could not find overflow button either");
                    }
                }
            }, 300); // Delay to ensure views are laid out
        }

        return result;
    }



   /* private void updateNotificationBadgePosition() {
        if (notificationText != null && materialToolbar != null) {
            final MenuItem notificationItem = materialToolbar.getMenu().findItem(R.id.notification);
            if (notificationItem != null) {
                materialToolbar.post(() -> {
                    // Try to find the action view in the toolbar
                    for (int i = 0; i < materialToolbar.getChildCount(); i++) {
                        View child = materialToolbar.getChildAt(i);
                        // Look for the overflow menu or action items
                        if (child instanceof ActionMenuView) {
                            ActionMenuView menuView = (ActionMenuView) child;
                            for (int j = 0; j < menuView.getChildCount(); j++) {
                                View menuItem = menuView.getChildAt(j);
                                if (menuItem.getId() == R.id.notification) {
                                    // Found it, now position the badge
                                    int[] location = new int[2];
                                    menuItem.getLocationInWindow(location);

                                    // Calculate center point of the notification icon
                                    int centerX = location[0] + (menuItem.getWidth() / 2);
                                    int centerY = location[1] + (menuItem.getHeight() / 2);

                                    // Adjust badge position to be centered on icon
                                    // Offset by half the badge width/height to center it properly
                                    notificationText.setX(centerX - (notificationText.getWidth() / 2));
                                    notificationText.setY(centerY - (notificationText.getHeight() / 2));

                                    // Make sure it's visible
                                    notificationText.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        }
    }*/
}