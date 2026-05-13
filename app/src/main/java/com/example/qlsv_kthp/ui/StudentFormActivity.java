package com.example.qlsv_kthp.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityStudentFormBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.Lop;
import com.example.qlsv_kthp.model.SinhVien;

import java.util.List;

public class StudentFormActivity extends AppCompatActivity {

    private ActivityStudentFormBinding binding;
    private DatabaseHelper dbHelper;
    private int maSV = -1; // -1 means adding new

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // Load classes for Spinner
        loadClasses();

        // Check if editing
        if (getIntent().hasExtra("maSV")) {
            maSV = getIntent().getIntExtra("maSV", -1);
            binding.tvTitle.setText("Sửa sinh viên");
            // Load student data...
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> {
            saveStudent();
        });
    }

    private void loadClasses() {
        List<Lop> classes = dbHelper.getAllLop();
        ArrayAdapter<Lop> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClass.setAdapter(adapter);
    }

    private void saveStudent() {
        String name = binding.etFullName.getText().toString().trim();
        String dob = binding.etDob.getText().toString().trim();
        String gender = binding.rbMale.isChecked() ? "Nam" : "Nữ";
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        Lop selectedLop = (Lop) binding.spinnerClass.getSelectedItem();

        if (name.isEmpty() || selectedLop == null) {
            Toast.makeText(this, "Vui lòng nhập tên và chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SinhVien sv = new SinhVien();
        sv.setHoTen(name);
        sv.setNgaySinh(dob);
        sv.setGioiTinh(gender);
        sv.setEmail(email);
        sv.setSoDienThoai(phone);
        sv.setDiaChi(address);
        sv.setMaLop(selectedLop.getMaLop());

        if (maSV == -1) {
            long id = dbHelper.insertSinhVien(sv);
            if (id > 0) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            sv.setMaSV(maSV);
            int rows = dbHelper.updateSinhVien(sv);
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
