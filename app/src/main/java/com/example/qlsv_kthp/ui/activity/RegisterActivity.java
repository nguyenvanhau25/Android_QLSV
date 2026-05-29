package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityRegisterBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.Lop;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.util.SecurityUtils;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnRegister.setOnClickListener(v -> registerAccount());

        loadClasses();
    }

    private void loadClasses() {
        List<Lop> classes = dbHelper.getAllLop();
        ArrayAdapter<Lop> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClass.setAdapter(adapter);
    }

    private void registerAccount() {
        String fullName = textOf(binding.etFullName);
        String username = textOf(binding.etUsername);
        String email = textOf(binding.etEmail);
        String phone = textOf(binding.etPhone);
        String dob = textOf(binding.etDob);
        String address = textOf(binding.etAddress);
        String password = textOf(binding.etPassword);
        String confirmPassword = textOf(binding.etConfirmPassword);
        Lop selectedClass = (Lop) binding.spinnerClass.getSelectedItem();
        String gender = binding.rbMale.isChecked() ? "Nam" : "Nu";

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập các thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedClass == null) {
            Toast.makeText(this, "Vui lòng chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        SinhVien sinhVien = new SinhVien();
        sinhVien.setHoTen(fullName);
        sinhVien.setNgaySinh(dob);
        sinhVien.setGioiTinh(gender);
        sinhVien.setEmail(email);
        sinhVien.setSoDienThoai(phone);
        sinhVien.setDiaChi(address);
        sinhVien.setMaLop(selectedClass.getMaLop());

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setUsername(username);
        taiKhoan.setPassword(SecurityUtils.sha256(password));
        taiKhoan.setHoTen(fullName);
        taiKhoan.setEmail(email);
        taiKhoan.setRole("student");

        long result = -1;
        try {
            result = dbHelper.registerStudentAccount(taiKhoan, sinhVien);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi hệ thống: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        
        if (result <= 0) {
            Toast.makeText(this, "Tạo tài khoản thất bại, vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đăng ký thành công, bạn có thể đăng nhập ngay", Toast.LENGTH_LONG).show();
        finish();
    }

    private String textOf(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
