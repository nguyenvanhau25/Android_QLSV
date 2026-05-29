package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ItemDocumentBinding;
import com.example.qlsv_kthp.model.TaiLieu;

import java.util.List;

/**
 * Adapter hiển thị tài liệu môn học - Senior Refactor
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    public interface OnDocumentClickListener {
        void onDownloadClick(TaiLieu tl);
        void onItemClick(TaiLieu tl);
    }

    private final List<TaiLieu> list;
    private final OnDocumentClickListener listener;

    public DocumentAdapter(List<TaiLieu> list, OnDocumentClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDocumentBinding binding = ItemDocumentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiLieu tl = list.get(position);

        holder.binding.tvFileName.setText(tl.getTenFile());
        holder.binding.tvFileInfo.setText(String.format("%s · Đăng ngày %s",
                tl.getKichThuoc(), tl.getNgayDang()));

        // Set icon dựa trên loại file
        int iconRes = R.drawable.ic_grade; // Default
        if (tl.getLoaiFile() != null) {
            String type = tl.getLoaiFile().toLowerCase();
            if (type.contains("pdf")) iconRes = R.drawable.ic_grade; // Thay bằng ic_pdf nếu có
            else if (type.contains("video")) iconRes = R.drawable.ic_home; // Thay bằng ic_video
        }
        holder.binding.ivFileIcon.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(tl));
        holder.binding.btnDownload.setOnClickListener(v -> listener.onDownloadClick(tl));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDocumentBinding binding;
        ViewHolder(ItemDocumentBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
