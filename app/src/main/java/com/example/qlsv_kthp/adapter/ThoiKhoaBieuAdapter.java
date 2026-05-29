package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemTimetableBinding;
import com.example.qlsv_kthp.model.ThoiKhoaBieu;

import java.util.List;

/**
 * Adapter hiển thị Thời khóa biểu theo ngày - Senior Refactor
 */
public class ThoiKhoaBieuAdapter extends RecyclerView.Adapter<ThoiKhoaBieuAdapter.ViewHolder> {

    private final List<ThoiKhoaBieu> list;

    public ThoiKhoaBieuAdapter(List<ThoiKhoaBieu> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimetableBinding binding = ItemTimetableBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThoiKhoaBieu tkb = list.get(position);
        
        holder.binding.tvSubjectName.setText(tkb.getTenMH());
        holder.binding.tvRoom.setText("Phòng: " + tkb.getPhongHoc());
        holder.binding.tvInstructor.setText("GV: " + tkb.getTenGiangVien());
        holder.binding.tvDuration.setText(String.format("Tiết %d - %d · Tuần %s", 
                tkb.getTietBatDau(), tkb.getTietKetThuc(), tkb.getTuan()));

        // Map tiết học sang thời gian thực tế (Giả định)
        holder.binding.tvStartTime.setText(getTimeForTiet(tkb.getTietBatDau(), true));
        holder.binding.tvEndTime.setText(getTimeForTiet(tkb.getTietKetThuc(), false));
    }

    private String getTimeForTiet(int tiet, boolean isStart) {
        switch (tiet) {
            case 1: return isStart ? "07:00" : "07:45";
            case 2: return isStart ? "07:50" : "08:35";
            case 3: return isStart ? "08:40" : "09:25";
            case 4: return isStart ? "09:35" : "10:20";
            case 5: return isStart ? "10:25" : "11:10";
            case 6: return isStart ? "11:15" : "12:00";
            case 7: return isStart ? "13:00" : "13:45";
            case 8: return isStart ? "13:50" : "14:35";
            case 9: return isStart ? "14:40" : "15:25";
            case 10: return isStart ? "15:35" : "16:20";
            case 11: return isStart ? "16:25" : "17:10";
            case 12: return isStart ? "17:15" : "18:00";
            default: return "--:--";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTimetableBinding binding;
        ViewHolder(ItemTimetableBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
