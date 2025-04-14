package com.example.staj_deneme.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErrorAdapter extends BaseAdapter {
    List<ErrorModel> errors;
    List<ErrorInfoModel> errorInfos;
    Context context;
    private Map<Integer, ErrorInfoModel> errorInfoMap;

    public ErrorAdapter() {
    }

    public ErrorAdapter(List<ErrorModel> errors, List<ErrorInfoModel> errorInfos, Context context) {
        this.errors = errors;
        this.errorInfos = errorInfos;
        this.context = context;


    }
    private static class ViewHolder {
        TextView machindeId;
        TextView machindePartId;
        TextView errorType;
        TextView errorDesc;
        TextView errorStartDate;
        TextView errorEndDate;
        ImageView imgURL;
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
        ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.error_detail_layout, parent, false);

            holder = new ViewHolder();
            holder.machindeId = convertView.findViewById(R.id.errorMachineId_textview);
            holder.machindePartId = convertView.findViewById(R.id.errorMachinePartId_textview);
            holder.errorType = convertView.findViewById(R.id.errorType_textview);
            holder.errorDesc = convertView.findViewById(R.id.errorDesc_textview);
            holder.errorStartDate = convertView.findViewById(R.id.errorStartDate_textview);
            holder.errorEndDate = convertView.findViewById(R.id.errorEndDate_textview);
            holder.imgURL = convertView.findViewById(R.id.errorImage_imageview);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ErrorModel curr = errors.get(position);





        for (ErrorInfoModel e : errorInfos) {
            if (e.getMachineId() == curr.getMachineId()) {
                holder.machindeId.setText("Makine adı: " + e.getMachineName());
                break;
            }
        }

// Makine parça adı
        boolean foundPart = false;
        if (curr.getMachinePartId() != null) {
            for (ErrorInfoModel e : errorInfos) {
                if (e.getMachinePartId() != null && e.getMachinePartId().equals(curr.getMachinePartId())) {
                    holder.machindePartId.setText("Makine parça adı: " + e.getMachinePartName());
                    foundPart = true;
                    break;
                }
            }
        }
        if (!foundPart) {
            holder.machindePartId.setText("Makine parçası bilgisi bulunamadı.");
        }
        holder.errorType.setText("Hata tipi: "+curr.getErrorType());
        holder.errorDesc.setText("Hata açıklama: "+curr.getErrorDesc());
        holder.errorStartDate.setText("Arıza kaydı giriş tarihi: "+curr.getErrorDate());
        try{
            String durs = formatErrorDuration(curr);
            holder.errorEndDate.setText("Arıza kaydı için geçen süre: "+durs);
        }catch (Exception e){
            holder.errorEndDate.setText("Arıza kaydı için geçen süre: HESAPLANAMADI");
        }
        loadImage(holder.imgURL,curr.getErrorImage());

        return convertView;
    }
    private void loadImage(ImageView imageView, String base64Image) {
        // Consider using a background thread or image loading library
        if (base64Image != null) {
            try {
                byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Glide.with(context)
                        .asBitmap()
                        .load(decodedBytes)
                        .placeholder(R.drawable.baseline_no_photography_24)
                        .error(R.drawable.baseline_no_photography_24)
                        .into(imageView);
            } catch (Exception e) {
                Log.e("ImageDecodeError", "Error decoding image: " + e.getMessage());
                Glide.with(context)
                        .load(R.drawable.baseline_no_photography_24)
                        .into(imageView);
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.baseline_no_photography_24)
                    .into(imageView);
        }
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
