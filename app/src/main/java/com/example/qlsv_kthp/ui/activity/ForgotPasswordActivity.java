package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityForgotPasswordBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.util.SecurityUtils;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnResetPassword.setOnClickListener(v -> handleResetPassword());
    }

    private void handleResetPassword() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String newPass = binding.etNewPassword.getText().toString().trim();
        String confirmPass = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập Username và Email đã đăng ký", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPass)) {
            binding.etNewPassword.setError("Vui lòng nhập mật khẩu mới");
            return;
        }

        if (newPass.length() < 6) {
            binding.etNewPassword.setError("Mật khẩu mới phải từ 6 ký tự");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            binding.etConfirmPassword.setError("Mật khẩu xác nhận không trùng khớp");
            return;
        }

        try {
            String hashedNewPass = SecurityUtils.sha256(newPass);
            boolean success = dbHelper.resetPassword(username, email, hashedNewPass);

            if (success) {
                Toast.makeText(this, "Đặt lại mật khẩu thành công! Hãy đăng nhập lại.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Thông tin không chính xác hoặc tài khoản không tồn tại", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}