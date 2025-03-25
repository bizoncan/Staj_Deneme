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

import com.example.staj_deneme.InterFaces.ErrorInterface;
import com.example.staj_deneme.Models.ErrorInfoModel;
import com.example.staj_deneme.Models.ErrorModel;
import com.example.staj_deneme.R;
import com.example.staj_deneme.RetrofitClient;
import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorAdapter extends BaseAdapter {
    List<ErrorModel> errors;
    List<ErrorInfoModel> errorInfos;
    Context context;

    public ErrorAdapter() {
    }

    public ErrorAdapter(List<ErrorModel> errors, List<ErrorInfoModel> errorInfos, Context context) {
        this.errors = errors;
        this.errorInfos = errorInfos;
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

        String durs = formatErrorDuration(curr);

        TextView machindeId = convertView.findViewById(R.id.errorMachineId_textview);
        TextView machindePartId = convertView.findViewById(R.id.errorMachinePartId_textview);
        TextView errorType = convertView.findViewById(R.id.errorType_textview);
        TextView errorDesc = convertView.findViewById(R.id.errorDesc_textview);
        TextView errorStartDate = convertView.findViewById(R.id.errorStartDate_textview);
        TextView errorEndDate = convertView.findViewById(R.id.errorEndDate_textview);
        ImageView imgURL = convertView.findViewById(R.id.errorImage_imageview);
        for(ErrorInfoModel e : errorInfos){
            if (e.getMachineId()==curr.getMachineId()){
                machindeId.setText("Makine: "+e.getMachineName());
            }
            if(curr.getMachinePartId() != null){
                if(e.getMachineId()==curr.getMachineId()){
                    machindePartId.setText("Makine parçası: "+e.getMachinePartName());
                }
            }
            else machindePartId.setText("Makine Parçası Id'si yok");
        }
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
    private String formatErrorDuration(ErrorModel error) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(error.getErrorDate(),dateFormat);
        LocalDateTime end = LocalDateTime.parse(error.getErrorEndDate(),dateFormat);
        Duration durr = Duration.between(start, end);
        Long hours= durr.toHours();
        Long minutes= durr.toMinutes()%60;
        Long seconds= durr.getSeconds()%60;
        String durs = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours,minutes, seconds);
        return durs;
    }

}
