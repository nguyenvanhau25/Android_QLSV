package com.example.qlsv_kthp.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemScoreBinding;
import com.example.qlsv_kthp.model.Diem;

import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị chi tiết điểm số môn học - Senior Refactor
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private final List<Diem> list;

    public ScoreAdapter(List<Diem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScoreBinding binding = ItemScoreBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Diem diem = list.get(position);
        
        holder.binding.tvSubjectName.setText(diem.getTenMH());
        holder.binding.tvSubjectInfo.setText(String.format(Locale.getDefault(), 
                "%d tín chỉ · %s", diem.getSoTinChi(), diem.getHocKy()));
        
        holder.binding.tvDiemBT.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiemBaiTap()));
        holder.binding.tvDiemGK.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiemGiuaKy()));
        holder.binding.tvDiemCK.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiemCuoiKy()));

        float dtb = diem.getDiemTrungBinh();
        holder.binding.tvFinalGrade.setText(String.format(Locale.getDefault(), "%.1f", dtb));

        // Cập nhật màu sắc Tag theo điểm số
        GradientDrawable bg = (GradientDrawable) holder.binding.tvFinalGrade.getBackground();
        if (dtb >= 8.5) {
            bg.setColor(Color.parseColor("#4CAF50")); // Giỏi - Xanh lá
        } else if (dtb >= 7.0) {
            bg.setColor(Color.parseColor("#2196F3")); // Khá - Xanh dương
        } else if (dtb >= 5.0) {
            bg.setColor(Color.parseColor("#FF9800")); // TB - Cam
        } else {
            bg.setColor(Color.parseColor("#F44336")); // Yếu - Đỏ
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemScoreBinding binding;
        ViewHolder(ItemScoreBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
