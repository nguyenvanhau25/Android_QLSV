package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsv_kthp.model.DiemDanh;
import com.example.qlsv_kthp.R;
import com.google.android.material.chip.Chip;
import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder> {
    private final List<DiemDanh> list;

    public AttendanceHistoryAdapter(List<DiemDanh> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiemDanh dd = list.get(position);
        holder.tvDate.setText(dd.getNgay());
        
        if (dd.getTrangThai() == 1) {
            holder.chipStatus.setText("Có mặt");
            holder.chipStatus.setChipBackgroundColorResource(R.color.score_pass_bg);
            holder.chipStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.score_pass));
        } else {
            holder.chipStatus.setText("Vắng");
            holder.chipStatus.setChipBackgroundColorResource(R.color.score_fail_bg);
            holder.chipStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.score_fail));
        }
        
        holder.tvSession.setText("Môn: " + dd.getTenMH());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chipStatus;
        TextView tvDate, tvSession, tvNote;

        ViewHolder(View v) {
            super(v);
            chipStatus = v.findViewById(R.id.chipStatus);
            tvDate = v.findViewById(R.id.tvDate);
            tvSession = v.findViewById(R.id.tvSession);
            tvNote = v.findViewById(R.id.tvNote);
        }
    }
}
