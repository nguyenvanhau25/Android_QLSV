package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.StudentAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentListBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.ui.activity.StudentDetailActivity;
import com.example.qlsv_kthp.ui.activity.StudentFormActivity;
import com.example.qlsv_kthp.util.FileUtils;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StudentListFragment extends Fragment {

    private ActivityStudentListBinding binding;
    private DatabaseHelper dbHelper;
    private List<SinhVien> allStudents = new ArrayList<>();
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityStudentListBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());
        session = new SessionManager(requireContext());

        binding.rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));

        if (binding.layoutNavBar != null) {
            binding.layoutNavBar.setVisibility(View.GONE);
        }
        binding.btnBack.setVisibility(View.GONE);

        configureUiByRole();

        binding.btnAdd.setOnClickListener(v -> {
            if (!session.isAdmin()) {
                Toast.makeText(getContext(), "Sinh viên không được phép thêm dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(new Intent(getActivity(), StudentFormActivity.class), 200);
        });

        binding.btnExport.setOnClickListener(v -> {
            if (!session.isAdmin()) {
                Toast.makeText(getContext(), "Sinh viên không được phép xuất danh sách", Toast.LENGTH_SHORT).show();
                return;
            }
            exportStudents();
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                filterStudents(s.toString());
            }
        });

        loadStudents();
        return binding.getRoot();
    }

    private void configureUiByRole() {
        if (session.isAdmin()) {
            binding.tvTitle.setText("Danh sách sinh viên");
            binding.btnAdd.setVisibility(View.VISIBLE);
            binding.btnExport.setVisibility(View.VISIBLE);
            return;
        }

        binding.tvTitle.setText("Thông tin cá nhân");
        binding.btnAdd.setVisibility(View.GONE);
        binding.btnExport.setVisibility(View.GONE);
    }

    private void loadStudents() {
        if (session.isAdmin()) {
            allStudents = dbHelper.getAllSinhVien();
        } else {
            allStudents = new ArrayList<>();
            SinhVien currentStudent = dbHelper.getSinhVienById(session.getMaSV());
            if (currentStudent != null) {
                allStudents.add(currentStudent);
            }
        }
        showStudents(allStudents);
    }

    private void showStudents(List<SinhVien> students) {
        StudentAdapter adapter = new StudentAdapter(students, student -> {
            Intent intent = new Intent(getActivity(), StudentDetailActivity.class);
            intent.putExtra("maSV", student.getMaSV());
            startActivityForResult(intent, 201);
        });
        binding.rvStudents.setAdapter(adapter);
    }

    private void filterStudents(String query) {
        if (!session.isAdmin()) {
            showStudents(allStudents);
            return;
        }

        if (query.trim().isEmpty()) {
            showStudents(allStudents);
            return;
        }

        List<SinhVien> filtered = dbHelper.searchSinhVien(query.trim());
        showStudents(filtered);
    }

    public void showDeleteConfirm(SinhVien sv) {
        if (!session.isAdmin()) {
            Toast.makeText(getContext(), "Sinh viên không được phép xóa dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Xóa sinh viên")
                .setMessage("Bạn có chắc muốn xóa " + sv.getHoTen() + "?\nDữ liệu điểm và điểm danh liên quan cũng sẽ bị xóa.")
                .setPositiveButton("Xóa", (d, w) -> {
                    dbHelper.deleteSinhVien(sv.getMaSV());
                    loadStudents();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void exportStudents() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"MaSV", "HoTen", "NgaySinh", "GioiTinh", "Email", "SDT", "Lop"});
        for (SinhVien s : allStudents) {
            data.add(new String[]{
                    String.valueOf(s.getMaSV()),
                    s.getHoTen(),
                    s.getNgaySinh() != null ? s.getNgaySinh() : "",
                    s.getGioiTinh() != null ? s.getGioiTinh() : "",
                    s.getEmail() != null ? s.getEmail() : "",
                    s.getSoDienThoai() != null ? s.getSoDienThoai() : "",
                    s.getTenLop() != null ? s.getTenLop() : ""
            });
        }
        FileUtils.exportToCSV(getContext(), "DanhSachSinhVien", data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == android.app.Activity.RESULT_OK) {
            loadStudents();
        }
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
