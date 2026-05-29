package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ActivityStudentDetailBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.util.RoleAccessManager;

import java.util.Locale;

public class StudentDetailActivity extends AppCompatActivity {

    private ActivityStudentDetailBinding binding;
    private DatabaseHelper dbHelper;
    private SinhVien sinhVien;
    private int maSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        maSV = getIntent().getIntExtra("maSV", -1);

        if (maSV == -1) {
            Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!RoleAccessManager.requireStudentRecordAccess(this, maSV)) {
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSettings.setVisibility(RoleAccessManager.isAdmin(this) ? View.VISIBLE : View.GONE);
        binding.btnSettings.setOnClickListener(v -> showActionDialog());
        loadStudentData();
    }

    // Inside StudentDetailActivity.java

    private void loadStudentData() {
        sinhVien = dbHelper.getSinhVienById(maSV);
        if (sinhVien == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Header
        binding.tvAvatarInitials.setText(sinhVien.getInitial());
        binding.tvStudentName.setText(sinhVien.getHoTen());
        String classInfo = (sinhVien.getTenLop() != null ? sinhVien.getTenLop() : "Chưa xếp lớp");
        binding.tvStudentIdAndClass.setText(
                "SV" + String.format(Locale.getDefault(), "%03d", sinhVien.getMaSV()) + " · " + classInfo);

        // Academic Info
        float gpa = dbHelper.getGPA(maSV);
        binding.tvGpa.setText(String.format(Locale.getDefault(), "%.2f / 10.0", gpa));
        binding.tvRegisteredClass.setText(classInfo);

        // Fetch Current Subjects (For demonstration, using latest semester data)
        loadCurrentSubjects();

        // Personal Info
        binding.tvDob.setText(sinhVien.getNgaySinh() != null ? sinhVien.getNgaySinh() : "--");
        binding.tvGender.setText(sinhVien.getGioiTinh() != null ? sinhVien.getGioiTinh() : "--");
        binding.tvEmail.setText(sinhVien.getEmail() != null ? sinhVien.getEmail() : "--");
        binding.tvPhone.setText(sinhVien.getSoDienThoai() != null ? sinhVien.getSoDienThoai() : "--");
        binding.tvAddress.setText(sinhVien.getDiaChi() != null ? sinhVien.getDiaChi() : "--");
    }

    private void loadCurrentSubjects() {
        // Assume latest semester is 2025.2 or simply get all recent subjects
        java.util.List<com.example.qlsv_kthp.model.Diem> scores = dbHelper.getDiemBySinhVien(maSV, "2025.1");
        if (scores.isEmpty()) {
            scores = dbHelper.getDiemBySinhVien(maSV, "2025.2");
        }

        if (scores.isEmpty()) {
            binding.tvCurrentSubjects.setText("Chưa đăng ký môn học");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < scores.size(); i++) {
                sb.append("• ").append(scores.get(i).getTenMH());
                if (i < scores.size() - 1) sb.append("\n");
            }
            binding.tvCurrentSubjects.setText(sb.toString());
        }
    }

    public void showActionDialog() {
        if (!RoleAccessManager.isAdmin(this)) {
            Toast.makeText(this, "Bạn không có quyền quản trị sinh viên", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(sinhVien.getHoTen())
                .setItems(new String[]{"Sửa thông tin", "Xóa sinh viên"},
                        (dialog, which) -> {
                            if (which == 0) {
                                editStudent();
                            } else {
                                confirmDelete();
                            }
                        })
                .show();
    }

    private void editStudent() {
        Intent intent = new Intent(this, StudentFormActivity.class);
        intent.putExtra("maSV", maSV);
        startActivityForResult(intent, 100);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_delete_student))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    int rows = dbHelper.deleteSinhVien(maSV);
                    if (rows > 0) {
                        Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadStudentData();
        }
    }
}
