package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.qlsv_kthp.adapter.AttendanceHistoryAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentAttendanceHistoryBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.DiemDanh;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.util.SessionManager;
import java.util.List;

public class StudentAttendanceHistoryActivity extends AppCompatActivity {
    private ActivityStudentAttendanceHistoryBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private List<MonHoc> subjectList;
    private int selectedMaMH = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentAttendanceHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        setupSubjectDropdown();
    }
    
    private void setupSubjectDropdown() {
        subjectList = dbHelper.getRegisteredSubjects(session.getMaSV());
        if (subjectList.isEmpty()) {
            binding.rvAttendanceHistory.setVisibility(android.view.View.GONE);
            return;
        }
        
        String[] subjectNames = new String[subjectList.size()];
        for (int i = 0; i < subjectList.size(); i++) {
            subjectNames[i] = subjectList.get(i).getTenMH();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, subjectNames);
        binding.actvSubject.setAdapter(adapter);
        
        binding.actvSubject.setOnItemClickListener((parent, view, position, id) -> {
            selectedMaMH = subjectList.get(position).getMaMH();
            loadAttendanceHistory();
        });
        
        // Select first item by default
        binding.actvSubject.setText(subjectNames[0], false);
        selectedMaMH = subjectList.get(0).getMaMH();
        loadAttendanceHistory();
    }
    
    private void loadAttendanceHistory() {
        if (selectedMaMH == -1) return;
        
        List<DiemDanh> history = dbHelper.getDiemDanhBySinhVien(session.getMaSV(), selectedMaMH);
        
        int present = 0;
        int absent = 0;
        for (DiemDanh dd : history) {
            if (dd.getTrangThai() == 1) present++;
            else absent++;
        }
        
        binding.tvPresentCount.setText(String.valueOf(present));
        binding.tvAbsentCount.setText(String.valueOf(absent));
        binding.tvLateCount.setText("0"); // Logic cho muộn nếu có
        
        int total = present + absent;
        int rate = total > 0 ? (present * 100 / total) : 100;
        
        binding.tvRateValue.setText(rate + "%");
        binding.progressRate.setProgress(rate);
        
        binding.rvAttendanceHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAttendanceHistory.setAdapter(new AttendanceHistoryAdapter(history));
    }
}
