package com.example.staj_deneme.Adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.staj_deneme.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private ArrayList<Object> imageList; // URI, Bitmap ve String URL'leri destekler

    public SliderAdapter(ArrayList<Object> imageList) {
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
    }

    @Override
    public int getItemCount() {
        return imageList.size();
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
}
