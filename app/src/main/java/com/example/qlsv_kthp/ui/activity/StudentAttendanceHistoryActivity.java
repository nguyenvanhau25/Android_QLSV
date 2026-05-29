package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.AttendanceAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentAttendanceHistoryBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DiemDanh;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentAttendanceHistoryActivity extends AppCompatActivity {

    private ActivityStudentAttendanceHistoryBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private int maSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentAttendanceHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);
        maSV = session.getMaSV();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.rvAttendance.setLayoutManager(new LinearLayoutManager(this));

        loadAttendanceStats();
    }

    private void loadAttendanceStats() {
        List<MonHoc> registeredSubjects = dbHelper.getRegisteredSubjects(maSV);
        List<DiemDanh> allAttendance = new ArrayList<>();

        int totalPresent = 0;
        int totalAbsent = 0;

        if (registeredSubjects != null) {
            for (MonHoc mh : registeredSubjects) {
                List<DiemDanh> subjectAttendance =
                        dbHelper.getDiemDanhBySinhVien(maSV, mh.getMaMH());

                if (subjectAttendance == null) {
                    continue;
                }

                for (DiemDanh dd : subjectAttendance) {
                    if (dd.getTrangThai() == 1) {
                        totalPresent++;
                    } else {
                        totalAbsent++;
                    }
                }

                allAttendance.addAll(subjectAttendance);
            }
        }

        binding.tvTotalPresent.setText(String.valueOf(totalPresent));
        binding.tvTotalAbsent.setText(String.valueOf(totalAbsent));

        int total = totalPresent + totalAbsent;
        float rate = total == 0 ? 100f : (float) totalPresent * 100f / total;

        binding.tvAttendanceRate.setText(String.format(Locale.getDefault(), "%.1f%%", rate));
        binding.progressRate.setProgress((int) rate);

        if (allAttendance.isEmpty()) {
            binding.rvAttendance.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rvAttendance.setVisibility(View.VISIBLE);
            binding.layoutEmpty.setVisibility(View.GONE);

            AttendanceAdapter adapter = new AttendanceAdapter(allAttendance);
            binding.rvAttendance.setAdapter(adapter);
        }
    }
}