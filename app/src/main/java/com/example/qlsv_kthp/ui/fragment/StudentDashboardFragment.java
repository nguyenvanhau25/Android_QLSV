package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.NotificationAdapter;
import com.example.qlsv_kthp.adapter.TimetableSlotAdapter;
import com.example.qlsv_kthp.databinding.FragmentStudentDashboardBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.model.ThongBao;
import com.example.qlsv_kthp.ui.activity.ProfileActivity;
import com.example.qlsv_kthp.ui.activity.StudentScoreActivity;
import com.example.qlsv_kthp.ui.activity.StudentSubjectRegisterActivity;
import com.example.qlsv_kthp.ui.activity.StudentTimetableActivity;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.Calendar;
import java.util.List;

public class StudentDashboardFragment extends Fragment {

    private FragmentStudentDashboardBinding binding;
    private SessionManager session;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        session = new SessionManager(requireContext());
        dbHelper = new DatabaseHelper(requireContext());

        setupUserInfo();
        setupTodaySchedule();
        setupNotifications();

        // GPA card -> màn hình điểm
        binding.cardGpa.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), StudentScoreActivity.class)));

        // Chuông / hồ sơ
        binding.ivNotif.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ProfileActivity.class)));

        // Nhãn "HÔM NAY" -> mở thời khóa biểu
        binding.tvTodayLabel.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), StudentTimetableActivity.class)));

        // Mini stats -> đăng ký môn học
        binding.layoutMiniStats.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), StudentSubjectRegisterActivity.class)));

        // Avatar -> chọn ảnh từ URL
        binding.ivAvatar.setOnClickListener(v -> showAvatarInputDialog());

        // Load avatar đã lưu (nếu có)
        String savedAvatar = session.getAvatarPath();
        if (!TextUtils.isEmpty(savedAvatar)) {
            loadAvatar(savedAvatar);
        }
    }

    // ─── Avatar ──────────────────────────────────────────────────────────────

    private void showAvatarInputDialog() {
        com.google.android.material.textfield.TextInputEditText etUrl =
                new com.google.android.material.textfield.TextInputEditText(requireContext());
        etUrl.setHint("Nhập URL ảnh (https://...)");
        String saved = session.getAvatarPath();
        if (!TextUtils.isEmpty(saved)) etUrl.setText(saved);

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cập nhật ảnh đại diện")
                .setView(etUrl)
                .setPositiveButton("Áp dụng", (d, w) -> {
                    String url = etUrl.getText() != null ? etUrl.getText().toString().trim() : "";
                    if (!url.isEmpty()) {
                        session.setAvatarPath(url);
                        loadAvatar(url);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadAvatar(String url) {
        try {
            // Dùng Glide nếu có, nếu không thì ẩn chữ tắt
            Class.forName("com.bumptech.glide.Glide");
            com.bumptech.glide.Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .placeholder(android.R.color.transparent)
                    .into(binding.ivAvatar);
            binding.tvAvatarInitials.setVisibility(View.GONE);
        } catch (ClassNotFoundException e) {
            // Glide không có sẵn — hiển thị URL đầu tiên như placeholder chữ
            binding.tvAvatarInitials.setVisibility(View.VISIBLE);
        }
    }

    // ─── Data setup ──────────────────────────────────────────────────────────

    private void setupUserInfo() {
        SinhVien sv = dbHelper.getSinhVienById(session.getMaSV());
        if (sv != null) {
            String fullName = sv.getHoTen();
            String firstName = fullName.contains(" ")
                    ? fullName.substring(fullName.lastIndexOf(" ") + 1)
                    : fullName;
            binding.tvGreeting.setText("Xin chào, " + firstName + "!");
            binding.tvStudentId.setText("SV" + String.format("%03d", sv.getMaSV())
                    + " · " + (sv.getTenLop() != null ? sv.getTenLop() : "Chưa xếp lớp"));
            binding.tvAvatarInitials.setText(firstName.substring(0, 1).toUpperCase());
        }

        float gpa = dbHelper.getGPA(session.getMaSV());
        binding.tvGpaValue.setText(String.format("%.2f", gpa));

        List<com.example.qlsv_kthp.model.MonHoc> registeredSubjects =
                dbHelper.getRegisteredSubjects(session.getMaSV());
        binding.tvSubjectCount.setText(registeredSubjects.size() + " môn học kỳ này");
    }

    private void setupTodaySchedule() {
        Calendar cal = Calendar.getInstance();
        // Calendar.DAY_OF_WEEK: 1=CN, 2=T2, ..., 7=T7
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // Chuyển sang hệ thứ trong tuần (2=T2 ... 7=T7, 8=CN)
        int thu = (dayOfWeek == Calendar.SUNDAY) ? 8 : dayOfWeek;

        List<com.example.qlsv_kthp.model.ThoiKhoaBieu> schedule =
                dbHelper.getTKBBySinhVienAndThu(session.getMaSV(), thu);

        if (schedule.isEmpty()) {
            binding.rvTodaySchedule.setVisibility(View.GONE);
        } else {
            binding.rvTodaySchedule.setVisibility(View.VISIBLE);
            binding.rvTodaySchedule.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.rvTodaySchedule.setAdapter(new TimetableSlotAdapter(schedule));
        }
    }

    private void setupNotifications() {
        List<ThongBao> allNotifs = dbHelper.getAllThongBao();
        List<ThongBao> recent = allNotifs.subList(0, Math.min(allNotifs.size(), 3));
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvNotifications.setAdapter(new NotificationAdapter(recent));

        binding.btnSeeAllNotif.setOnClickListener(v ->
                startActivity(new Intent(requireContext(),
                        com.example.qlsv_kthp.ui.activity.NotificationDetailActivity.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
