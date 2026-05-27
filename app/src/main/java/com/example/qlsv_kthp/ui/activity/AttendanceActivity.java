package com.example.qlsv_kthp.ui.activity;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.AttendanceAdapter;
import com.example.qlsv_kthp.databinding.ActivityAttendanceBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DiemDanh;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.util.RoleAccessManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình Điểm danh
 * Giảng viên điểm danh tất cả sinh viên trong một buổi học
 */
public class AttendanceActivity extends AppCompatActivity {

    private ActivityAttendanceBinding binding;
    private DatabaseHelper dbHelper;
    private AttendanceAdapter adapter;
    private List<DiemDanh> danhSachDiemDanh = new ArrayList<>();
    private int maMH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!RoleAccessManager.requireAdmin(this)) {
            return;
        }
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        maMH = getIntent().getIntExtra("maMH", -1);

        if (maMH == -1) {
            Toast.makeText(this, "Không xác định được môn học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());

        // Cập nhật thống kê tóm tắt
        loadSummary();

        // Tải danh sách sinh viên để điểm danh
        loadStudentsForAttendance();

        // Nút lưu điểm danh
        binding.btnSave.setOnClickListener(v -> saveAttendance());
    }

    private void loadSummary() {
        updateSummaryCount();
    }

    /**
     * Tải danh sách sinh viên theo lớp/môn học để điểm danh hôm nay
     */
    private void loadStudentsForAttendance() {
        List<SinhVien> students = dbHelper.getAllSinhVien();
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        // Tạo DiemDanh mặc định = có mặt cho mỗi sinh viên
        danhSachDiemDanh.clear();
        for (SinhVien sv : students) {
            DiemDanh dd = new DiemDanh();
            dd.setMaSV(sv.getMaSV());
            dd.setMaMH(maMH);
            dd.setNgay(today);
            dd.setTrangThai(1); // mặc định có mặt
            dd.setTenSV(sv.getHoTen());
            danhSachDiemDanh.add(dd);
        }

        adapter = new AttendanceAdapter(danhSachDiemDanh);
        binding.rvAttendance.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAttendance.setAdapter(adapter);

        // Cập nhật thống kê ngay sau khi load
        updateSummaryCount();
    }

    /**
     * Cập nhật số liệu có mặt / vắng theo danh sách hiện tại
     */
    private void updateSummaryCount() {
        int present = 0, absent = 0;
        for (DiemDanh dd : danhSachDiemDanh) {
            if (dd.getTrangThai() == 1) present++;
            else absent++;
        }
        binding.tvPresentCount.setText(String.valueOf(present));
        binding.tvAbsentCount.setText(String.valueOf(absent));
    }

    /**
     * Lưu toàn bộ điểm danh vào database
     */
    private void saveAttendance() {
        int saved = 0;
        for (DiemDanh dd : danhSachDiemDanh) {
            long result = dbHelper.insertDiemDanh(dd);
            if (result > 0) saved++;
        }
        Toast.makeText(this,
                "Đã lưu điểm danh " + saved + "/" + danhSachDiemDanh.size() + " sinh viên",
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
