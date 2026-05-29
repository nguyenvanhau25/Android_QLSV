package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.ScoreAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentScoreBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.Diem;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;
import java.util.Locale;

/**
 * Màn hình xem bảng điểm sinh viên - Senior Refactor
 * Hiển thị GPA tổng quát và chi tiết điểm từng môn.
 */
public class StudentScoreActivity extends AppCompatActivity {

    private ActivityStudentScoreBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private int maSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);
        maSV = session.getMaSV();

        if (maSV == -1) {
            Toast.makeText(this, "Không xác định được sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        loadScoreData();
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.rvScores.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadScoreData() {
        // 1. Tính toán GPA tổng
        float totalGpa = dbHelper.getGPA(maSV);
        binding.tvTotalGpa.setText(String.format(Locale.getDefault(), "%.2f", totalGpa));
        
        // 2. Xếp loại
        String ranking;
        if (totalGpa >= 9.0) ranking = "Xuất sắc";
        else if (totalGpa >= 8.0) ranking = "Giỏi";
        else if (totalGpa >= 7.0) ranking = "Khá";
        else if (totalGpa >= 5.0) ranking = "Trung bình";
        else ranking = "Yếu/Kém";
        
        binding.tvRanking.setText("Xếp loại: " + ranking);

        // 3. Load danh sách điểm chi tiết (Lấy HK gần nhất hoặc tất cả)
        // Trong DatabaseHelper hiện tại getDiemBySinhVien yêu cầu hocKy. 
        // Ta sẽ lấy tất cả các môn đã đăng ký để hiển thị điểm.
        List<Diem> scores = dbHelper.getDiemBySinhVien(maSV, "HK1-2025");
        
        if (scores.isEmpty()) {
            binding.rvScores.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rvScores.setVisibility(View.VISIBLE);
            binding.layoutEmpty.setVisibility(View.GONE);
            ScoreAdapter adapter = new ScoreAdapter(scores);
            binding.rvScores.setAdapter(adapter);
        }
    }
}
