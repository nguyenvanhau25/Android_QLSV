package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.model.TaiLieu;
import java.util.List;
import android.widget.Toast;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private final List<TaiLieu> list;

    public DocumentAdapter(List<TaiLieu> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiLieu doc = list.get(position);
        holder.tvFileType.setText(doc.getLoaiFile());
        holder.tvDocName.setText(doc.getTenFile());
        holder.tvDocMeta.setText(doc.getKichThuoc() + " · Đăng ngày " + doc.getNgayDang());
        
        holder.btnDownload.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Đang tải xuống " + doc.getTenFile(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileType, tvDocName, tvDocMeta;
        View btnDownload;

        ViewHolder(View v) {
            super(v);
            tvFileType = v.findViewById(R.id.tvFileType);
            tvDocName = v.findViewById(R.id.tvDocName);
            tvDocMeta = v.findViewById(R.id.tvDocMeta);
            btnDownload = v.findViewById(R.id.btnDownload);
        }
    }
}
