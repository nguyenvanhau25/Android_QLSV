package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityRegisterBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.util.SecurityUtils;

/**
 * Màn hình Đăng ký tài khoản - Senior Refactor
 * Bao gồm Validation dữ liệu đầu vào và xử lý nghiệp vụ tạo tài khoản.
 */
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
        binding.btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String username = binding.etUsername.getText().toString().trim();
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPass = binding.etConfirmPassword.getText().toString().trim();

        // 1. Validation Logic (Task 5)
        if (TextUtils.isEmpty(username) || username.length() < 4) {
            binding.etUsername.setError("Tên đăng nhập ít nhất 4 ký tự");
            return;
        }
        if (TextUtils.isEmpty(fullName)) {
            binding.etFullName.setError("Vui lòng nhập họ tên");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Định dạng Email không hợp lệ");
            return;
        }
        if (!phone.matches("(\\+84|0)\\d{9,10}")) {
            binding.etPhone.setError("Số điện thoại không hợp lệ");
            return;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Mật khẩu phải từ 6 ký tự");
            return;
        }
        if (!password.equals(confirmPass)) {
            binding.etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        // 2. Check tồn tại
        if (dbHelper.isUsernameExists(username)) {
            Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Thực hiện đăng ký
        TaiKhoan tk = new TaiKhoan();
        tk.setUsername(username);
        tk.setPassword(SecurityUtils.sha256(password)); // Hash password
        tk.setHoTen(fullName);
        tk.setEmail(email);
        tk.setRole("student");

        SinhVien sv = new SinhVien();
        sv.setHoTen(fullName);
        sv.setEmail(email);
        sv.setSoDienThoai(phone);
        sv.setMaLop(1); // Mặc định lớp mẫu, SV sẽ cập nhật sau

        long result = dbHelper.registerStudentAccount(tk, sv);

        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công! Hãy đăng nhập.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi hệ thống khi đăng ký", Toast.LENGTH_SHORT).show();
        }
    }
}
