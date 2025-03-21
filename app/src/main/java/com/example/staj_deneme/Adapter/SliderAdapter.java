package com.example.staj_deneme.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.staj_deneme.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private Context context;
    private ArrayList<Object> imageList; // URI, Bitmap ve String URL'leri destekler

    public SliderAdapter(Context context, ArrayList<Object> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Object image = imageList.get(position);

        if (image instanceof String) { // URL türündeyse
            Glide.with(holder.itemView.getContext()).load((String) image).into(holder.imageView);
        } else if (image instanceof Uri) { // URI türündeyse
            Glide.with(holder.itemView.getContext()).load((Uri) image).into(holder.imageView);
        } else if (image instanceof Bitmap) { // Bitmap türündeyse
            holder.imageView.setImageBitmap((Bitmap) image);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    private void showImageDialog(Object image) {
        Dialog imageDialog = new Dialog(context);
        imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDialog.setContentView(R.layout.dialog_image);

        ImageView dialogImageView = imageDialog.findViewById(R.id.dialogImageView);

        if (image instanceof String) { // URL türündeyse
            Glide.with(context).load((String) image).into(dialogImageView);
        } else if (image instanceof Uri) { // URI türündeyse
            Glide.with(context).load((Uri) image).into(dialogImageView);
        } else if (image instanceof Bitmap) { // Bitmap türündeyse
            Bitmap resizedBitmap = resizeBitmap((Bitmap) image, 1080, 1920);
            dialogImageView.setImageBitmap((Bitmap) resizedBitmap);
        }

        imageDialog.show();
    }



    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
        }
    }
    public void setImageList(ArrayList<Object> newImageList) {
        notifyDataSetChanged(); // **Adapter'e verilerin değiştiğini bildir**
    }
    private Bitmap resizeBitmap(Bitmap original, int maxWidth, int maxHeight) {
        int width = original.getWidth();
        int height = original.getHeight();

        // Boyut oranlarını koruyarak yeni boyutları hesapla
        float aspectRatio = (float) width / (float) height;
        if (width > height) {
            width = maxWidth;
            height = (int) (width / aspectRatio);
        } else {
            height = maxHeight;
            width = (int) (height * aspectRatio);
        }

        // Yeni boyutta bitmap'i oluştur
        return Bitmap.createScaledBitmap(original, width, height, false);
    }
}
