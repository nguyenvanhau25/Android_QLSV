package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ItemAttendanceBinding;
import com.example.qlsv_kthp.model.DiemDanh;

import java.util.List;

/**
 * Adapter cho danh sách điểm danh – cho phép toggle trạng thái có mặt / vắng
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private final List<DiemDanh> list;

    public AttendanceAdapter(List<DiemDanh> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendanceBinding binding = ItemAttendanceBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiemDanh dd = list.get(position);

        // Tên và mã SV
        holder.binding.tvStudentName.setText(dd.getTenSV());
        holder.binding.tvStudentId.setText("SV" + String.format("%03d", dd.getMaSV()));

        // Avatar initials
        String name = dd.getTenSV() != null ? dd.getTenSV() : "?";
        String[] parts = name.split(" ");
        String initial = parts[parts.length - 1].substring(0, 1).toUpperCase();
        holder.binding.tvAvatarInitials.setText(initial);

        // Badge trạng thái
        updateStatusBadge(holder, dd.getTrangThai());

        // Toggle khi click vào card
        holder.itemView.setOnClickListener(v -> {
            int newStatus = dd.getTrangThai() == 1 ? 0 : 1;
            dd.setTrangThai(newStatus);
            updateStatusBadge(holder, newStatus);
        });
    }

    private void updateStatusBadge(ViewHolder holder, int status) {
        android.content.Context ctx = holder.itemView.getContext();
        if (status == 1) {
            holder.binding.tvStatusLabel.setText(ctx.getString(R.string.status_present));
            holder.binding.tvStatusLabel.setTextColor(ctx.getColor(R.color.green_primary));
            holder.binding.cvStatusBadge.setCardBackgroundColor(ctx.getColor(R.color.green_light));
        } else {
            holder.binding.tvStatusLabel.setText(ctx.getString(R.string.status_absent));
            holder.binding.tvStatusLabel.setTextColor(ctx.getColor(R.color.red_primary));
            holder.binding.cvStatusBadge.setCardBackgroundColor(ctx.getColor(R.color.red_light));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAttendanceBinding binding;
        ViewHolder(ItemAttendanceBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
