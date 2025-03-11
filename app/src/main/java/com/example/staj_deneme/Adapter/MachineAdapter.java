package com.example.staj_deneme.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.staj_deneme.Models.MachineModel;
import com.example.staj_deneme.R;

import java.util.List;

public class MachineAdapter extends BaseAdapter {
    List<MachineModel> machines;
    Context context;

    public MachineAdapter() {
    }

    public MachineAdapter(Context context ,List<MachineModel> machines) {
        this.machines = machines;
        this.context = context;
    }

    @Override
    public int getCount() {
        return machines.size();
    }

    @Override
    public Object getItem(int position) {

        return machines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.machine_layout, parent, false);
        }

        MachineModel curr = machines.get(position);

        TextView name= convertView.findViewById(R.id.Name_textview);
        TextView number= convertView.findViewById(R.id.Number_textview);
        TextView desc= convertView.findViewById(R.id.Desc_textview);
        ImageView imgUrl= convertView.findViewById(R.id.imgURL);

        name.setText("Adı: "+curr.getName());
        number.setText("Sayısı: "+String.valueOf(curr.getNumber()));
        desc.setText("Açıklama: "+curr.getDesc());
        Log.d("FirebaseData", "Image URL: " + curr.getImgURL());
        Glide.with(context).load(curr.getImgURL()).into(imgUrl);


        return convertView;
    }
}
