package com.example.staj_deneme.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;

import java.util.List;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderViewHolder> {
    private List<WorkOrderModel> workOrders;
    private List<String> machineNames;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public WorkOrderAdapter(List<WorkOrderModel> workOrders, List<String> machineNames) {
        this.workOrders = workOrders;
        this.machineNames = machineNames;
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
        String machineName = machineNames.get(position);

        holder.titleTextView.setText(workOrder.getTitle());
        holder.machineNameTextView.setText("Makine: " + machineName);
        holder.startDateTextView.setText("Başlangıç: " + workOrder.getWorkOrderStartDate());
        holder.statusTextView.setText(workOrder.isClosed() ? "İş Bitti" : "İş Bitmedi");
        holder.statusLight.setBackgroundResource(workOrder.isClosed() ? R.color.green : R.color.redred);
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

        WorkOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.workOrderTitle_txt);
            machineNameTextView = itemView.findViewById(R.id.workMachineName_txt);
            startDateTextView = itemView.findViewById(R.id.workOrderStartDate_txt);
            statusTextView = itemView.findViewById(R.id.isDurum_txt);
            statusLight = itemView.findViewById(R.id.light2);

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
