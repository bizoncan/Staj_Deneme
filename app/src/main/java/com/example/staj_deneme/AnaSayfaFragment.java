package com.example.staj_deneme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.staj_deneme.Activities.MainActivity;
import com.example.staj_deneme.InterFaces.RecieveNotificationInterface;
import com.example.staj_deneme.Models.NotificationModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnaSayfaFragment extends Fragment {
    boolean opened=false;
    TextView notificationText;
    BaseAdapter adapter;
    ListView notificationsListView;
    List<String> titleList,machineIdList,descList,idList;
    Button notification_btn;
    private View rootView;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_qr_scan, container, false);

        // Initialize Handler with main looper to ensure it runs on UI thread
        handler = new Handler(Looper.getMainLooper());

        // Initialize views
        notificationsListView = rootView.findViewById(R.id.notificationsbase_listview);
        notificationText = rootView.findViewById(R.id.notification_textview);

        // Set up QR scan button
        rootView.findViewById(R.id.QRTara_btn).setOnClickListener(v -> {
            IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
            integrator.initiateScan();
        });

        // Initialize data lists
        titleList = new ArrayList<>();
        machineIdList = new ArrayList<>();
        descList = new ArrayList<>();
        idList = new ArrayList<>();

        // Set up adapter
        setupAdapter();
        notificationsListView.setAdapter(adapter);

        // Set initial visibility for notifications list
        notificationsListView.setVisibility(View.GONE);

        // Set up notification toggle click listener
        // Using proper View.OnClickListener implementation
        notificationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNotificationList();
            }
        });

        return rootView;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchNotifications();
        startDatabasePolling();

    }
    private void toggleNotificationList() {
        if (isAdded()) {
            checkForNewNotifications();

            if (opened) {
                notificationsListView.setVisibility(View.GONE);
                opened = false;
            } else {
                notificationsListView.setVisibility(View.VISIBLE);
                opened = true;
            }
        }
    }

    private void setupAdapter() {
        adapter = new BaseAdapter() {
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
                if (convertView == null) {
                    convertView = LayoutInflater.from(requireContext()).inflate(R.layout.notification_layout, parent, false);
                }
                TextView title_text = convertView.findViewById(R.id.title_textview);
                TextView machineId_text = convertView.findViewById(R.id.machineId_textview);
                TextView desc_text = convertView.findViewById(R.id.description_textview);

                title_text.setText(titleList.get(position));
                machineId_text.setText(machineIdList.get(position));
                desc_text.setText(descList.get(position));
                return convertView;
            }
        };
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(requireContext(), "QR kod okunamadı", Toast.LENGTH_LONG).show();
            } else {
                String qrContent = result.getContents();
                Toast.makeText(requireContext(), qrContent, Toast.LENGTH_SHORT).show();

                // Navigate to MainActivity with QR data
                if (isAdded() && getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("QR", qrContent);
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void startDatabasePolling() {
        final int POLL_INTERVAL = 2500;

        Runnable pollRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    checkForNewNotifications();
                    handler.postDelayed(this, POLL_INTERVAL);
                }
            }
        };

        handler.post(pollRunnable);
    }

    private void fetchNotifications() {
        if (!isAdded()) return;

        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (isAdded() && response.body() != null && response.isSuccessful()) {
                    notificationText.setText(Integer.toString(response.body().size()));
                } else if (isAdded()) {
                    try {
                        Log.e("API ERROR", "Response Code: " + response.code() +
                                " | Message: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                if (isAdded()) {
                    Log.e("API ERROR", "Call failed: " + t.getMessage());
                }
            }
        });
    }

    public void checkForNewNotifications() {
        if (!isAdded()) return;

        RecieveNotificationInterface recieveNotification = RetrofitClient.getApiServiceNotification();
        recieveNotification.getNotification().enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(Call<List<NotificationModel>> call, Response<List<NotificationModel>> response) {
                if (isAdded() && response.body() != null && response.isSuccessful()) {
                    List<NotificationModel> notifications = response.body();
                    for (NotificationModel n : notifications) {
                        if (!idList.contains(Integer.toString(n.getId()))) {
                            idList.add(Integer.toString(n.getId()));
                            titleList.add(n.getTitle());
                            machineIdList.add(Integer.toString(n.getMachineId()));
                            descList.add(n.getDescription());
                        }
                    }

                    if (isAdded()) {
                        notificationText.setText(Integer.toString(notifications.size()));
                        adapter.notifyDataSetChanged();
                    }
                } else if (isAdded()) {
                    try {
                        Log.e("API ERROR", "Response Code: " + response.code() +
                                " | Message: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NotificationModel>> call, Throwable t) {
                if (isAdded()) {
                    Log.e("API ERROR", "Call failed: " + t.getMessage());
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        // Remove callbacks to prevent memory leaks
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restart polling when fragment resumes
        startDatabasePolling();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up resources
        handler.removeCallbacksAndMessages(null);
    }
}
