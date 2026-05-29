package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.model.ThoiKhoaBieu;
import java.util.List;

public class TimetableSlotAdapter extends RecyclerView.Adapter<TimetableSlotAdapter.ViewHolder> {
    private final List<ThoiKhoaBieu> list;

    public TimetableSlotAdapter(List<ThoiKhoaBieu> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetable_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThoiKhoaBieu tkb = list.get(position);
        holder.tvTime.setText(getTimeString(tkb.getTietBatDau()));
        holder.tvSubjectName.setText(tkb.getTenMH());
        holder.tvRoom.setText(tkb.getPhongHoc());
        holder.tvTimeRange.setText(getTimeString(tkb.getTietBatDau()) + " - " + getTimeStringEnd(tkb.getTietKetThuc()));
        holder.tvTeacher.setText("GV: " + tkb.getTenGiangVien());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String getTimeString(int tiet) {
        // Mock time mapping based on period (tiet)
        switch (tiet) {
            case 1: return "07:30";
            case 2: return "08:20";
            case 3: return "09:10";
            case 4: return "10:00";
            case 5: return "10:50";
            case 6: return "13:00";
            case 7: return "13:50";
            case 8: return "14:40";
            case 9: return "15:30";
            case 10: return "16:20";
            default: return "00:00";
        }
    }

    private String getTimeStringEnd(int tiet) {
        switch (tiet) {
            case 1: return "08:20";
            case 2: return "09:10";
            case 3: return "10:00";
            case 4: return "10:50";
            case 5: return "11:40";
            case 6: return "13:50";
            case 7: return "14:40";
            case 8: return "15:30";
            case 9: return "16:20";
            case 10: return "17:10";
            default: return "00:00";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvSubjectName, tvRoom, tvTimeRange, tvTeacher;

        ViewHolder(View v) {
            super(v);
            tvTime = v.findViewById(R.id.tvTime);
            tvSubjectName = v.findViewById(R.id.tvSubjectName);
            tvRoom = v.findViewById(R.id.tvRoom);
            tvTimeRange = v.findViewById(R.id.tvTimeRange);
            tvTeacher = v.findViewById(R.id.tvTeacher);
        }
    }
}
