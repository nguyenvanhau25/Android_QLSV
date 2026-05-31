package com.example.qlsv_kthp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.NotificationAdapter;
import com.example.qlsv_kthp.databinding.ActivityNotificationBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.ThongBao;
import com.example.qlsv_kthp.ui.activity.NotificationDetailActivity;
import com.example.qlsv_kthp.ui.activity.NotificationFormActivity;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;

public class NotificationFragment extends Fragment {

    private ActivityNotificationBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private static final int REQUEST_CODE_FORM = 101;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = ActivityNotificationBinding.inflate(inflater, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        session = new SessionManager(requireContext());

        setupUI();
        loadNotifications();

        return binding.getRoot();
    }

    private void setupUI() {
        binding.tvTitle.setText("Thông báo");

        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnMarkAllRead.setOnClickListener(v -> markAllNotificationsAsRead());

        // Hiển thị nút thêm thông báo nếu là Admin
        if (session.isAdmin()) {
            binding.btnAddNotification.setVisibility(View.VISIBLE);
            binding.btnAddNotification.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), NotificationFormActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FORM);
            });
        } else {
            binding.btnAddNotification.setVisibility(View.GONE);
        }
    }

    private void loadNotifications() {
        if (binding == null) return;

        List<ThongBao> list = dbHelper.getAllThongBao();

        NotificationAdapter adapter = new NotificationAdapter(
                list,
                new NotificationAdapter.OnNotificationClickListener() {
                    @Override
                    public void onNotificationClick(ThongBao tb) {
                        dbHelper.markThongBaoAsRead(tb.getMaThongBao());

                        Intent intent = new Intent(requireContext(), NotificationDetailActivity.class);
                        intent.putExtra("maThongBao", tb.getMaThongBao());
                        intent.putExtra("tieuDe", tb.getTieuDe());
                        intent.putExtra("noiDung", tb.getNoiDung());
                        intent.putExtra("ngayTao", tb.getNgayTao());
                        startActivity(intent);

                        loadNotifications();
                    }

                    @Override
                    public void onLongClick(ThongBao tb) {
                        if (session.isAdmin()) {
                            showAdminActions(tb);
                        }
                    }
                }
        );

        binding.rvNotifications.setAdapter(adapter);

        if (list == null || list.isEmpty()) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvNotifications.setVisibility(View.GONE);
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
            binding.rvNotifications.setVisibility(View.VISIBLE);
        }

        updateUnreadCount(list);
    }

    private void updateUnreadCount(List<ThongBao> list) {
        int unreadCount = 0;

        if (list != null) {
            for (ThongBao tb : list) {
                if (tb.getDaDoc() == 0) {
                    unreadCount++;
                }
            }
        }

        binding.tvUnreadCount.setText(unreadCount + " thông báo chưa đọc");
    }

    private void markAllNotificationsAsRead() {
        List<ThongBao> list = dbHelper.getAllThongBao();

        if (list == null || list.isEmpty()) {
            Toast.makeText(requireContext(), "Không có thông báo nào", Toast.LENGTH_SHORT).show();
            return;
        }

        for (ThongBao tb : list) {
            if (tb.getDaDoc() == 0) {
                dbHelper.markThongBaoAsRead(tb.getMaThongBao());
            }
        }

        Toast.makeText(requireContext(), "Đã đánh dấu tất cả là đã đọc", Toast.LENGTH_SHORT).show();
        loadNotifications();
    }

    private void showAdminActions(ThongBao tb) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Quản lý thông báo")
                .setItems(new String[]{"Sửa thông báo", "Xóa thông báo"}, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(requireContext(), NotificationFormActivity.class);
                        intent.putExtra("maThongBao", tb.getMaThongBao());
                        intent.putExtra("tieuDe", tb.getTieuDe());
                        intent.putExtra("noiDung", tb.getNoiDung());
                        intent.putExtra("loai", tb.getLoai());
                        startActivityForResult(intent, REQUEST_CODE_FORM);
                    } else {
                        confirmDelete(tb);
                    }
                })
                .show();
    }

    private void confirmDelete(ThongBao tb) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa thông báo này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int rows = dbHelper.deleteThongBao(tb.getMaThongBao());
                    if (rows > 0) {
                        Toast.makeText(requireContext(), "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                        loadNotifications();
                    } else {
                        Toast.makeText(requireContext(), "Không thể xóa thông báo", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FORM && resultCode == Activity.RESULT_OK) {
            loadNotifications();
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