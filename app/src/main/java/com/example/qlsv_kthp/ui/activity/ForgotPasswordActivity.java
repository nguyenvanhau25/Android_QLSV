package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
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
        binding.btnResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String username = textOf(binding.etUsername);
        String email = textOf(binding.etEmail);
        String newPassword = textOf(binding.etNewPassword);
        String confirmPassword = textOf(binding.etConfirmPassword);

        if (username.isEmpty() || email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.resetPassword(username, email, SecurityUtils.sha256(newPassword));
        if (!success) {
            Toast.makeText(this, "Không tìm thấy tài khoản khớp với tên đăng nhập và email", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String textOf(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
