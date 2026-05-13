package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.StudentAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentListBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.ui.StudentFormActivity;
import com.example.qlsv_kthp.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentListFragment extends Fragment {

    private ActivityStudentListBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityStudentListBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());

        binding.rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Setup Add Button
        binding.btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), StudentFormActivity.class));
        });

        // Setup Export Button
        binding.btnExport.setOnClickListener(v -> {
            exportStudents();
        });

        // Hide navigation bar inside fragment
        if (binding.layoutNavBar != null) {
            binding.layoutNavBar.setVisibility(View.GONE);
        }
        
        // Hide back button in fragment mode
        binding.btnBack.setVisibility(View.GONE);

        loadStudents();

        return binding.getRoot();
    }

    private void loadStudents() {
        List<SinhVien> students = dbHelper.getAllSinhVien();
        StudentAdapter adapter = new StudentAdapter(students, student -> {
            Intent intent = new Intent(getActivity(), StudentFormActivity.class);
            intent.putExtra("maSV", student.getMaSV());
            startActivity(intent);
        });
        binding.rvStudents.setAdapter(adapter);

        if (students.isEmpty()) {
            // Logic to show empty state if needed
        }
    }

    private void exportStudents() {
        List<SinhVien> students = dbHelper.getAllSinhVien();
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"MaSV", "HoTen", "Lop", "Email", "SDT"});
        for (SinhVien s : students) {
            data.add(new String[]{
                    String.valueOf(s.getMaSV()),
                    s.getHoTen(),
                    s.getTenLop(),
                    s.getEmail(),
                    s.getSoDienThoai()
            });
        }
        FileUtils.exportToCSV(getContext(), "DanhSachSinhVien", data);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStudents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
