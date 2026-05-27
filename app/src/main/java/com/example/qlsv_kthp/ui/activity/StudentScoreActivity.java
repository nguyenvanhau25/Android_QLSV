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
import com.google.android.material.chip.Chip;
import java.util.List;

public class StudentScoreActivity extends AppCompatActivity {
    private ActivityStudentScoreBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private String hocKyCurrent = "HK1-2025";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        binding.btnExportTranscript.setOnClickListener(v -> {
            Toast.makeText(this, "Đang tải học bạ PDF...", Toast.LENGTH_SHORT).show();
        });
        
        binding.chipGroupSemester.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int checkedId = checkedIds.get(0);
                Chip chip = findViewById(checkedId);
                if (chip != null) {
                    if (chip.getText().toString().contains("HK1")) {
                        hocKyCurrent = "HK1-2025";
                    } else if (chip.getText().toString().contains("HK2")) {
                        hocKyCurrent = "HK2-2025";
                    }
                    loadScores();
                }
            }
        });

        loadScores();
    }
    
    private void loadScores() {
        List<Diem> danhSachDiem = dbHelper.getDiemBySinhVien(session.getMaSV(), hocKyCurrent);
        
        if (danhSachDiem.isEmpty()) {
            binding.rvScores.setVisibility(View.GONE);
            binding.tvGpaSem.setText("0.0");
            binding.tvCreditsSem.setText("0 / 0");
        } else {
            binding.rvScores.setVisibility(View.VISIBLE);
            binding.rvScores.setLayoutManager(new LinearLayoutManager(this));
            binding.rvScores.setAdapter(new ScoreAdapter(danhSachDiem));

            float gpaHocKy = dbHelper.getGPAHocKy(session.getMaSV(), hocKyCurrent);
            binding.tvGpaSem.setText(String.format("%.2f", gpaHocKy));
            
            int totalCredits = 0;
            for (Diem d : danhSachDiem) {
                totalCredits += d.getSoTinChi();
            }
            binding.tvCreditsSem.setText(totalCredits + " / " + totalCredits);
        }
    }
}
