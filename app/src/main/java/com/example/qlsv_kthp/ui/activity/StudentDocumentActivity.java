package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

        binding.btnBack.setOnClickListener(v -> finish());

        binding.rvDocuments.setLayoutManager(new LinearLayoutManager(this));

        setupSubjectDropdown();
        setupChipFilter();
    }

    private void setupChipFilter() {
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

        if (subjectList == null || subjectList.isEmpty()) {
            binding.rvDocuments.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Bạn chưa đăng ký môn học nào", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] subjectNames = new String[subjectList.size()];

        for (int i = 0; i < subjectList.size(); i++) {
            subjectNames[i] = subjectList.get(i).getTenMH();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                subjectNames
        );

        binding.actvSubject.setAdapter(adapter);

        binding.actvSubject.setOnItemClickListener((parent, view, position, id) -> {
            selectedMaMH = subjectList.get(position).getMaMH();
            loadDocuments();
        });

        binding.actvSubject.setText(subjectNames[0], false);
        selectedMaMH = subjectList.get(0).getMaMH();

        loadDocuments();
    }

    private void loadDocuments() {
        if (selectedMaMH == -1) {
            return;
        }

        List<TaiLieu> documents = dbHelper.getTaiLieuByMonHoc(selectedMaMH, selectedType);

        if (documents == null || documents.isEmpty()) {
            binding.rvDocuments.setVisibility(View.GONE);
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            return;
        }

        binding.rvDocuments.setVisibility(View.VISIBLE);
        binding.layoutEmpty.setVisibility(View.GONE);

        DocumentAdapter adapter = new DocumentAdapter(
                documents,
                new DocumentAdapter.OnDocumentClickListener() {
                    @Override
                    public void onDownloadClick(TaiLieu tl) {
                        openDocument(tl);
                    }

                    @Override
                    public void onItemClick(TaiLieu tl) {
                        openDocument(tl);
                    }
                }
        );

        binding.rvDocuments.setAdapter(adapter);
    }

    private void openDocument(TaiLieu taiLieu) {
        if (taiLieu == null) {
            Toast.makeText(this, "Tài liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = taiLieu.getUrlOrPath();

        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy đường dẫn tài liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở tài liệu", Toast.LENGTH_SHORT).show();
        }
    }
}