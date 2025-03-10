package com.example.staj_deneme.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staj_deneme.R;

public class SendNotificationActivity extends AppCompatActivity {
    public static final String CHANNEL_ID ="my_channel_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void bildirimYolla(View view){
        createNotificationChanel(this);
        showNotifications(this, "pooooop","poooooooooooooooooooooooooooooooooooooooooooop");

    }
    public void createNotificationChanel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            CharSequence name = "hah";
            String desc= "sadad";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    public void showNotifications(Context context, String title, String message){
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title).setContentText(message).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true);
        if(notificationManager !=null){
            notificationManager.notify(1, builder.build());
        }

    }
}