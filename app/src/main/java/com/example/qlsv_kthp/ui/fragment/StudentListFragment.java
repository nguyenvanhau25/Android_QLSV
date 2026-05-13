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
import com.example.qlsv_kthp.ui.activity.StudentFormActivity;
import com.example.qlsv_kthp.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment hiển thị danh sách sinh viên
 */
public class StudentListFragment extends Fragment {

    private ActivityStudentListBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityStudentListBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());

        binding.rvStudents.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Thiết lập nút thêm sinh viên
        binding.btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), StudentFormActivity.class));
        });

        // Thiết lập nút xuất dữ liệu CSV
        binding.btnExport.setOnClickListener(v -> {
            exportStudents();
        });

        // Ẩn thanh điều hướng bên trong fragment (đã có ở MainActivity)
        if (binding.layoutNavBar != null) {
            binding.layoutNavBar.setVisibility(View.GONE);
        }
        
        // Ẩn nút quay lại trong chế độ Fragment
        binding.btnBack.setVisibility(View.GONE);

        loadStudents();

        return binding.getRoot();
    }

    /**
     * Tải danh sách sinh viên từ Database và hiển thị lên RecyclerView
     */
    private void loadStudents() {
        List<SinhVien> students = dbHelper.getAllSinhVien();
        StudentAdapter adapter = new StudentAdapter(students, student -> {
            // Khi nhấn vào sinh viên, mở màn hình chỉnh sửa
            Intent intent = new Intent(getActivity(), StudentFormActivity.class);
            intent.putExtra("maSV", student.getMaSV());
            startActivity(intent);
        });
        binding.rvStudents.setAdapter(adapter);

        if (students.isEmpty()) {
            // Xử lý khi danh sách trống (có thể hiện view thông báo)
        }
    }

    /**
     * Xuất danh sách sinh viên ra file CSV
     */
    private void exportStudents() {
        List<SinhVien> students = dbHelper.getAllSinhVien();
        List<String[]> data = new ArrayList<>();
        // Tiêu đề cột
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
        // Gọi utility để lưu file
        FileUtils.exportToCSV(getContext(), "DanhSachSinhVien", data);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại danh sách mỗi khi fragment quay lại trạng thái hiển thị
        loadStudents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
