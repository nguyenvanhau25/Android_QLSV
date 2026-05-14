package com.example.qlsv_kthp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.NotificationAdapter;
import com.example.qlsv_kthp.databinding.ActivityNotificationBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.ThongBao;

import java.util.List;

public class NotificationFragment extends Fragment {

    private ActivityNotificationBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityNotificationBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());

        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.btnMarkAllRead.setOnClickListener(v -> {
            dbHelper.markAllAsRead();
            loadNotifications();
        });

        loadNotifications();
        return binding.getRoot();
    }

    private void loadNotifications() {
        List<ThongBao> notifications = dbHelper.getAllThongBao();
        binding.rvNotifications.setAdapter(new NotificationAdapter(notifications));
        binding.tvEmpty.setVisibility(notifications.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rvNotifications.setVisibility(notifications.isEmpty() ? View.GONE : View.VISIBLE);

        int unread = dbHelper.countUnreadThongBao();
        if (unread == 0) {
            binding.tvUnreadCount.setText("Tất cả thông báo đã được đọc");
        } else if (unread == 1) {
            binding.tvUnreadCount.setText("Còn 1 thông báo chưa đọc");
        } else {
            binding.tvUnreadCount.setText("Còn " + unread + " thông báo chưa đọc");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            loadNotifications();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
