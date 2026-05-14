package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.ScoreAdapter;
import com.example.qlsv_kthp.databinding.ActivityScoreBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.Diem;
import com.example.qlsv_kthp.util.RoleAccessManager;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;

/**
 * Màn hình Quản lý Điểm số
 * Hiển thị danh sách điểm từng môn của sinh viên theo học kỳ
 */
public class ScoreActivity extends AppCompatActivity {

    private ActivityScoreBinding binding;
    private DatabaseHelper dbHelper;
    private ScoreAdapter adapter;
    private int maSV;
    private String hocKyCurrent = "HK1-2024";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // Lấy maSV từ intent hoặc session (sinh viên xem điểm của mình)
        maSV = getIntent().getIntExtra("maSV", -1);
        if (maSV == -1) {
            SessionManager session = new SessionManager(this);
            maSV = session.getMaSV();
        }

        if (!RoleAccessManager.requireStudentRecordAccess(this, maSV)) {
            return;
        }

        // Nút quay lại
        binding.btnBack.setOnClickListener(v -> finish());

        // Tên sinh viên trong summary
        loadProfileSummary();

        // Tải điểm học kỳ hiện tại
        loadScores(hocKyCurrent);
    }

    private void loadProfileSummary() {
        var sv = dbHelper.getSinhVienById(maSV);
        if (sv != null) {
            binding.tvStudentName.setText(sv.getHoTen());
            binding.tvStudentMeta.setText(
                    "SV" + String.format("%03d", sv.getMaSV()) + " · " +
                            (sv.getTenLop() != null ? sv.getTenLop() : "Chưa xếp lớp"));
        }
    }

    /**
     * Tải danh sách điểm theo học kỳ
     */
    private void loadScores(String hocKy) {
        List<Diem> danhSachDiem = dbHelper.getDiemBySinhVien(maSV, hocKy);

        if (danhSachDiem.isEmpty()) {
            binding.rvScores.setVisibility(View.GONE);
            binding.tvAverage.setText("0.0");
        } else {
            binding.rvScores.setVisibility(View.VISIBLE);
            adapter = new ScoreAdapter(danhSachDiem);
            binding.rvScores.setLayoutManager(new LinearLayoutManager(this));
            binding.rvScores.setAdapter(adapter);

            // Tính và hiển thị điểm trung bình học kỳ
            float gpaHocKy = dbHelper.getGPAHocKy(maSV, hocKy);
            binding.tvAverage.setText(String.format("%.2f", gpaHocKy));
        }
    }
}
