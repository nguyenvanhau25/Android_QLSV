package com.example.qlsv_kthp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayHeaderAdapter extends RecyclerView.Adapter<DayHeaderAdapter.ViewHolder> {
    
    public interface OnDayClickListener {
        void onDayClick(int position);
    }

    private final List<String> days;
    private final List<String> dates;
    private int selectedPosition = 0;
    private final OnDayClickListener listener;

    public DayHeaderAdapter(List<String> days, List<String> dates, OnDayClickListener listener) {
        this.days = days;
        this.dates = dates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // We can just create a simple text view dynamically or inflate a small layout
        // For simplicity, dynamically create a TextView
        TextView tv = new TextView(parent.getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setPadding(32, 16, 32, 16);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setTextSize(14);
        return new ViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(days.get(position) + "\n" + dates.get(position));
        if (selectedPosition == position) {
            holder.tv.setTextColor(Color.parseColor("#1976D2")); // primary color roughly
            holder.tv.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            holder.tv.setTextColor(Color.parseColor("#757575")); // gray
            holder.tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
        
        holder.itemView.setOnClickListener(v -> {
            int old = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPosition);
            if (listener != null) listener.onDayClick(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
    
    public void setSelectedPosition(int position) {
        int old = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(old);
        notifyItemChanged(selectedPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ViewHolder(View v) {
            super(v);
            tv = (TextView) v;
        }
    }
}
