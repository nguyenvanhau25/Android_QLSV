package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.qlsv_kthp.adapter.SubjectAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentSubjectRegisterBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.util.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class StudentSubjectRegisterActivity extends AppCompatActivity {
    private ActivityStudentSubjectRegisterBinding binding;
    private DatabaseHelper dbHelper;
    private List<MonHoc> allSubjects;
    private List<MonHoc> selectedSubjects = new ArrayList<>();
    private SubjectAdapter adapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentSubjectRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        loadSubjects();
        
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSubjects(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        
        binding.btnConfirmRegister.setOnClickListener(v -> {
            if (selectedSubjects.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn môn học", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Bắt lỗi 1 lớp chỉ có tối đa 65 sinh viên
            for (MonHoc mh : selectedSubjects) {
                if (dbHelper.countStudentsInSubject(mh.getMaMH()) >= 65) {
                    Toast.makeText(this, "Môn " + mh.getTenMH() + " đã đủ 65 sinh viên!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            
            // Simulate payment
            long totalFee = selectedSubjects.size() * 500000L;
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Thanh toán học phí")
                .setMessage("Bạn đang đăng ký " + selectedSubjects.size() + " môn học.\nTổng học phí: " + totalFee + " VNĐ.\nBạn có muốn thanh toán và hoàn tất đăng ký?")
                .setPositiveButton("Thanh toán", (dialog, which) -> {
                    for (MonHoc mh : selectedSubjects) {
                        dbHelper.registerSubject(session.getMaSV(), mh.getMaMH());
                    }
                    Toast.makeText(this, "Đăng ký thành công " + selectedSubjects.size() + " môn học", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
        
        binding.btnViewSelected.setOnClickListener(v -> {
            Toast.makeText(this, "Đã chọn: " + selectedSubjects.size() + " môn", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void loadSubjects() {
        allSubjects = dbHelper.getUnregisteredSubjects(session.getMaSV());
        setupRecyclerView(allSubjects);
    }
    
    private void filterSubjects(String query) {
        if (query.trim().isEmpty()) {
            setupRecyclerView(allSubjects);
            return;
        }
        
        List<MonHoc> filtered = new ArrayList<>();
        for (MonHoc mh : allSubjects) {
            if (mh.getTenMH().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(mh);
            }
        }
        setupRecyclerView(filtered);
    }
    
    private void setupRecyclerView(List<MonHoc> list) {
        adapter = new SubjectAdapter(list, false, new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MonHoc mh) {
                if (selectedSubjects.contains(mh)) {
                    selectedSubjects.remove(mh);
                    Toast.makeText(StudentSubjectRegisterActivity.this, "Đã bỏ chọn: " + mh.getTenMH(), Toast.LENGTH_SHORT).show();
                } else {
                    selectedSubjects.add(mh);
                    Toast.makeText(StudentSubjectRegisterActivity.this, "Đã chọn: " + mh.getTenMH(), Toast.LENGTH_SHORT).show();
                }
                updateCreditTracker();
            }

            @Override
            public void onDeleteClick(MonHoc mh) {
                // Do nothing
            }
        });
        binding.rvAvailableSubjects.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAvailableSubjects.setAdapter(adapter);
    }
    
    private void updateCreditTracker() {
        int credits = 0;
        for (MonHoc mh : selectedSubjects) {
            credits += mh.getSoTinChi();
        }
        binding.tvCreditSelected.setText("Đã chọn: " + credits + " / 24 tín chỉ");
        binding.progressCredits.setProgress((int) ((credits / 24.0f) * 100));
        binding.btnViewSelected.setText("Môn đã chọn (" + selectedSubjects.size() + ")");
    }
}
