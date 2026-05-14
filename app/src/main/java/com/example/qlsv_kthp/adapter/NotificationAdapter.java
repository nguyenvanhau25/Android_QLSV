package com.example.qlsv_kthp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemNotificationBinding;
import com.example.qlsv_kthp.model.ThongBao;
import com.example.qlsv_kthp.ui.activity.NotificationDetailActivity;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

  private final List<ThongBao> list;

  public NotificationAdapter(List<ThongBao> list) {
    this.list = list;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemNotificationBinding binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ThongBao tb = list.get(position);

    holder.binding.tvTitle.setText(tb.getTieuDe());
    holder.binding.tvBody.setText(tb.getNoiDung());
    holder.binding.tvTime.setText(tb.getNgayTao());

    // Làm mờ thông báo đã đọc
    holder.itemView.setAlpha(tb.isRead() ? 0.65f : 1.0f);

    // Mở chi tiết khi click
    holder.itemView.setOnClickListener(v -> {
      Intent intent = new Intent(v.getContext(), NotificationDetailActivity.class);
      intent.putExtra("maThongBao", tb.getMaThongBao());
      intent.putExtra("tieuDe", tb.getTieuDe());
      intent.putExtra("noiDung", tb.getNoiDung());
      intent.putExtra("ngayTao", tb.getNgayTao());
      v.getContext().startActivity(intent);
      // Cập nhật UI ngay lập tức
      tb.setDaDoc(1);
      holder.itemView.setAlpha(0.65f);
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    final ItemNotificationBinding binding;
    ViewHolder(ItemNotificationBinding b) {
      super(b.getRoot());
      binding = b;
    }
  }
}
