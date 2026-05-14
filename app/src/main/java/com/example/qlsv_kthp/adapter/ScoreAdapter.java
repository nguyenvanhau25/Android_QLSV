package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ItemScoreBinding;
import com.example.qlsv_kthp.model.Diem;

import java.util.List;

/**
 * Adapter hiển thị danh sách điểm từng môn học
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
        holder.binding.tvCredits.setText(diem.getSoTinChi() + " tín chỉ");

        float dtb = diem.getDiemTrungBinh();
        holder.binding.tvScore.setText(String.format("%.1f", dtb));

        // Progress bar (0–10 → 0–100%)
        holder.binding.progressScore.setProgress((int) (dtb * 10));

        // Màu sắc theo điểm
        int colorRes;
        if (dtb >= 8.5) colorRes = R.color.score_pass;
        else if (dtb >= 5.0) colorRes = R.color.score_warn;
        else colorRes = R.color.score_fail;

        holder.binding.tvScore.setTextColor(
                holder.itemView.getContext().getColor(colorRes));
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
