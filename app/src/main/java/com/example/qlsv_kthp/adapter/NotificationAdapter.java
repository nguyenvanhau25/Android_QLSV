package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemNotificationBinding;
import com.example.qlsv_kthp.model.ThongBao;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

  private final List<ThongBao> list;
  private final OnNotificationClickListener listener;

  public interface OnNotificationClickListener {
    void onNotificationClick(ThongBao tb);
    void onLongClick(ThongBao tb);
  }

  public NotificationAdapter(List<ThongBao> list, OnNotificationClickListener listener) {
    this.list = list;
    this.listener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemNotificationBinding binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent,
            false
    );

    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ThongBao tb = list.get(position);

    holder.binding.tvTitle.setText(tb.getTieuDe());
    holder.binding.tvBody.setText(tb.getNoiDung());
    holder.binding.tvTime.setText(tb.getNgayTao());

    holder.itemView.setAlpha(tb.isRead() ? 0.65f : 1.0f);

    holder.itemView.setOnClickListener(v -> {
      if (listener != null) {
        listener.onNotificationClick(tb);
      }

      tb.setDaDoc(1);
      holder.itemView.setAlpha(0.65f);
    });

    holder.itemView.setOnLongClickListener(v -> {
      if (listener != null) {
        listener.onLongClick(tb);
      }
      return true;
    });
  }

  @Override
  public int getItemCount() {
    return list == null ? 0 : list.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    final ItemNotificationBinding binding;

    ViewHolder(ItemNotificationBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}