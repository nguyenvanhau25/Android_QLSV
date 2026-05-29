package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.StudentAdapter;
import com.example.qlsv_kthp.databinding.ActivitySubjectDetailBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;

/**
 * Màn hình chi tiết môn học - Senior Refactor
 */
public class SubjectDetailActivity extends AppCompatActivity {

    private ActivitySubjectDetailBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private int maMH;
    private MonHoc monHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);
        maMH = getIntent().getIntExtra("maMH", -1);

        if (maMH == -1) {
            Toast.makeText(this, "Không tìm thấy môn học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadData();
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> finish());
        
        // Chỉ Admin/Teacher mới có nút sửa và xem danh sách sinh viên
        if (session.isAdmin()) {
            binding.btnEdit.setVisibility(View.VISIBLE);
            binding.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(this, SubjectFormActivity.class);
                intent.putExtra("maMH", maMH);
                startActivityForResult(intent, 100);
            });
            binding.layoutStudentList.setVisibility(View.VISIBLE);
            binding.rvStudentList.setLayoutManager(new LinearLayoutManager(this));
        } else {
            binding.btnEdit.setVisibility(View.GONE);
            binding.layoutStudentList.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        monHoc = dbHelper.getMonHocById(maMH);
        if (monHoc == null) {
            Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.tvSubjectName.setText(monHoc.getTenMH());
        binding.tvSubjectCode.setText("Mã môn: MH" + String.format("%03d", monHoc.getMaMH()));
        binding.tvInstructor.setText(monHoc.getGiangVien());
        binding.tvCredits.setText(monHoc.getSoTinChi() + " tín chỉ");
        binding.tvSchedule.setText(monHoc.getLichHoc());
        
        int current = dbHelper.countStudentsInSubject(maMH);
        binding.tvCapacity.setText(current + " / " + monHoc.getSoLuongMax() + " sinh viên");
        
        if (monHoc.getTaiLieu() != null && !monHoc.getTaiLieu().isEmpty()) {
            binding.tvMaterials.setText(monHoc.getTaiLieu());
        }

        if (session.isAdmin()) {
            loadStudentList();
        }
    }

    private void loadStudentList() {
        // Lấy danh sách sinh viên đã đăng ký môn học này
        // Cần bổ sung method getSinhVienByMonHoc trong DatabaseHelper
        List<SinhVien> students = dbHelper.getSinhVienByMonHoc(maMH);
        StudentAdapter adapter = new StudentAdapter(students, sv -> {
            Intent intent = new Intent(this, StudentDetailActivity.class);
            intent.putExtra("maSV", sv.getMaSV());
            startActivity(intent);
        });
        binding.rvStudentList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();
        }
    }
}
