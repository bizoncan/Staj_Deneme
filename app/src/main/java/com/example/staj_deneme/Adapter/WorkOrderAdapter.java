package com.example.staj_deneme.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.staj_deneme.Models.MachineModel;
import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderViewHolder> {
    private List<WorkOrderModel> workOrders;
    private List<String> machineNames;
    private List<MachineModel> machineModels;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public WorkOrderAdapter(List<WorkOrderModel> workOrders, List<String> machineNames, List<MachineModel> machineModels) {
        this.workOrders = workOrders;
        this.machineNames = machineNames;
        this.machineModels = machineModels;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_work_order, parent, false);
        return new WorkOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderViewHolder holder, int position) {
        WorkOrderModel workOrder = workOrders.get(position);
        String machineName = "";
        String imageUrl = "";
        if(machineModels.get(position) != null){
            machineName = machineModels.get(position).getName() ;
            imageUrl = machineModels.get(position).getImgURL();
        }


        if (machineName ==""){
            machineName = "Makine bilgisi bulunamadı";
        }
        //String machineName = machineNames.get(position);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(workOrder.getWorkOrderStartDate());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String formattedDate = outputFormat.format(date);
        holder.titleTextView.setText(workOrder.getTitle());
        holder.machineNameTextView.setText("Makine: " + machineName);
        holder.startDateTextView.setText("Başlangıç: " + formattedDate);
       // holder.statusTextView.setText(workOrder.isClosed() ? "İş Bitti" : "İş Bitmedi");
        holder.statusLight.setBackgroundResource(workOrder.isClosed() ? R.color.green : R.color.redred);
        Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_no_photography_24)
                        .error(R.drawable.baseline_no_photography_24)
                        .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    class WorkOrderViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView machineNameTextView;
        TextView startDateTextView;
        TextView statusTextView;
        View statusLight;
        ImageView imageView;
        WorkOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.workOrderTitle_txt);
            machineNameTextView = itemView.findViewById(R.id.workMachineName_txt);
            startDateTextView = itemView.findViewById(R.id.workOrderStartDate_txt);
            statusTextView = itemView.findViewById(R.id.isDurum_txt);
            statusLight = itemView.findViewById(R.id.light2);
            imageView = itemView.findViewById(R.id.workOrderImage_img);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, v);
                    }
                }
            });
        }
    }
}
