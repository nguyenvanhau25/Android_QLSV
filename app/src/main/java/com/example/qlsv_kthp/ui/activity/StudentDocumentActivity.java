package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.qlsv_kthp.adapter.DocumentAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentDocumentBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.model.TaiLieu;
import com.example.qlsv_kthp.util.SessionManager;
import com.google.android.material.chip.Chip;
import java.util.List;

public class StudentDocumentActivity extends AppCompatActivity {
    private ActivityStudentDocumentBinding binding;
    private DatabaseHelper dbHelper;
    private List<MonHoc> subjectList;
    private int selectedMaMH = -1;
    private String selectedType = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        
        setupSubjectDropdown();
        
        binding.chipGroupType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = findViewById(checkedIds.get(0));
                if (chip != null) {
                    selectedType = chip.getText().toString();
                    loadDocuments();
                }
            }
        });
    }
    
    private void setupSubjectDropdown() {
        SessionManager session = new SessionManager(this);
        subjectList = dbHelper.getRegisteredSubjects(session.getMaSV());
        if (subjectList.isEmpty()) {
            binding.rvDocuments.setVisibility(android.view.View.GONE);
            binding.layoutEmpty.setVisibility(android.view.View.VISIBLE);
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
            loadDocuments();
        });
        
        // Select first by default
        binding.actvSubject.setText(subjectNames[0], false);
        selectedMaMH = subjectList.get(0).getMaMH();
        loadDocuments();
    }
    
    private void loadDocuments() {
        if (selectedMaMH == -1) return;
        
        List<TaiLieu> documents = dbHelper.getTaiLieuByMonHoc(selectedMaMH, selectedType);
        
        if (documents.isEmpty()) {
            binding.rvDocuments.setVisibility(android.view.View.GONE);
            binding.layoutEmpty.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.rvDocuments.setVisibility(android.view.View.VISIBLE);
            binding.layoutEmpty.setVisibility(android.view.View.GONE);
            binding.rvDocuments.setLayoutManager(new LinearLayoutManager(this));
            binding.rvDocuments.setAdapter(new DocumentAdapter(documents));
        }
    }
}
