package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.model.MonHoc;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MonHoc mh);
        void onDeleteClick(MonHoc mh);
    }

    private final List<MonHoc> list;
    private final boolean allowManage;
    private final OnItemClickListener listener;

    public SubjectAdapter(List<MonHoc> list, boolean allowManage, OnItemClickListener listener) {
        this.list = list;
        this.allowManage = allowManage;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonHoc mh = list.get(position);
        holder.tvText.setText(mh.getTenMH() + " (" + mh.getSoTinChi() + " TC)");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(mh);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (allowManage && listener != null) {
                listener.onDeleteClick(mh);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        ViewHolder(View v) {
            super(v);
            tvText = v.findViewById(R.id.textView);
        }
    }
}
