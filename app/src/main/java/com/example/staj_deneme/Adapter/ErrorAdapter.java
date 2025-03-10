package com.example.staj_deneme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ErrorAdapter extends BaseAdapter {
    List<ErrorModel> errors;
    Context context;

    public ErrorAdapter() {
    }

    public ErrorAdapter(List<ErrorModel> errors, Context context) {
        this.errors = errors;
        this.context = context;
    }

    @Override
    public int getCount() {
        return errors.size();
    }

    @Override
    public Object getItem(int position) {
        return errors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.error_detail_layout, parent, false);
        }
        ErrorModel curr = errors.get(position);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(curr.getErrorDate(),dateFormat);
        LocalDateTime end = LocalDateTime.parse(curr.getErrorEndDate(),dateFormat);
        Duration durr = Duration.between(start, end);
        Long hours= durr.toHours();
        Long minutes= durr.toMinutes()%60;
        Long seconds= durr.getSeconds()%60;
        String durs = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours,minutes, seconds);
        TextView machindeId = convertView.findViewById(R.id.errorMachineId_textview);
        TextView machindePartId = convertView.findViewById(R.id.machinePartId_textview);
        TextView errorType = convertView.findViewById(R.id.errorType_textview);
        TextView errorDesc = convertView.findViewById(R.id.errorDesc_textview);
        TextView errorStartDate = convertView.findViewById(R.id.errorStartDate_textview);
        TextView errorEndDate = convertView.findViewById(R.id.errorEndDate_textview);
        ImageView imgURL = convertView.findViewById(R.id.errorImage_imageview);

        machindeId.setText("Makine Id: "+curr.getMachineId().toString());
        if(machindePartId != null)machindePartId.setText("Makine parçasi Id: "+curr.getMachinePartId().toString());
        errorType.setText("Hata tipi: "+curr.getErrorType());
        errorDesc.setText("Hata açıklama: "+curr.getErrorDesc());
        errorStartDate.setText("Arıza kaydı giriş tarihi: "+curr.getErrorDate());
        errorEndDate.setText("Arıza kaydı için geçen süre: "+durs);
        if (curr.getErrorImage() != null) {
            try {
                byte[] decodedBytes = Base64.decode(curr.getErrorImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imgURL.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                imgURL.setImageResource(R.drawable.baseline_home_24);
            }
        }
        else{
            imgURL.setImageResource(R.drawable.baseline_home_24);
        }
        return convertView;
    }
}
