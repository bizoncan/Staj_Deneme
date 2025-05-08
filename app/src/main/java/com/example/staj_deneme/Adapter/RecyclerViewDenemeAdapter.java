package com.example.staj_deneme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.staj_deneme.Models.WorkOrderModel;
import com.example.staj_deneme.R;

import java.util.List;

public class RecyclerViewDenemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_WORK_ORDER = 1;
    private static final int TYPE_EMPTY = 2;

    private List<WorkOrderModel> workOrders;
    private Context context;

    public RecyclerViewDenemeAdapter(Context context, List<WorkOrderModel> workOrders) {
        this.context = context;
        this.workOrders = workOrders;
    }

    @Override
    public int getItemViewType(int position) {
        WorkOrderModel w = workOrders.get(position);
        if (w != null && !w.isClosed()) {
            return TYPE_WORK_ORDER;
        } else {
            return TYPE_EMPTY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_WORK_ORDER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_work_order, parent, false);
            return new WorkOrderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.empty_son_of_empty, parent, false);
            return new EmptyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkOrderViewHolder) {
            WorkOrderModel w = workOrders.get(position);
            WorkOrderViewHolder h = (WorkOrderViewHolder) holder;

            h.title.setText(w.getTitle());
            h.desc.setText(w.getDesc());
            h.startDate.setText(w.getWorkOrderStartDate());

            if (w.isClosed()) {
                setGreen(h.light2); // Tanımladığın method
                h.endDate.setText(w.getWorkOrderEndDate());
            } else {
                h.endDate.setText("İş kaydı daha sonlanmadı.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    // ViewHolder'lar
    public class WorkOrderViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, startDate, endDate;
        View light2;

        public WorkOrderViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.workOrderTitle_txt);
            desc = itemView.findViewById(R.id.workOrderDesc_txt);
            startDate = itemView.findViewById(R.id.workOrderStartDate_txt);
            endDate = itemView.findViewById(R.id.workOrderEndDate_txt);
            light2 = itemView.findViewById(R.id.lightView2);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }

    // Bu metodu Activity'den de taşıyabilirsin
    private void setGreen(View view) {
        view.setBackgroundResource(R.drawable.light_circle); // örnek
    }
}
