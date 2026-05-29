package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.MainActivity;
import com.example.qlsv_kthp.databinding.ActivityLoginBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.util.SecurityUtils;
import com.example.qlsv_kthp.util.SessionManager;

/**
 * Màn hình Đăng nhập - Senior Refactor
 * Xử lý Authentication, Validation và Phân quyền điều hướng.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        // Nếu đã đăng nhập thì vào thẳng Main
        if (session.isLoggedIn()) {
            navigateToMain();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> performLogin());
        binding.tvRegister.setOnClickListener(v -> 
            startActivity(new Intent(this, RegisterActivity.class)));
        binding.tvForgotPassword.setOnClickListener(v -> 
            startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void performLogin() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // 1. Validate Input
        if (TextUtils.isEmpty(username)) {
            binding.etUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        // 2. Kiểm tra thông tin trong DB
        // Sử dụng SHA-256 để kiểm tra mật khẩu (Security Task 5)
        String hashedPass = SecurityUtils.sha256(password);
        TaiKhoan user = dbHelper.checkLogin(username, hashedPass);

        if (user != null) {
            // 3. Tạo session và điều hướng
            session.createLoginSession(user);
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            navigateToMain();
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        // Clear backstack để không quay lại màn hình login được
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
