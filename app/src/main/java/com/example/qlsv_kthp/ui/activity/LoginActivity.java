package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.MainActivity;
import com.example.qlsv_kthp.databinding.ActivityLoginBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.util.SecurityUtils;
import com.example.qlsv_kthp.util.SessionManager;

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

        binding.btnLogin.setOnClickListener(v -> login());
        binding.tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

    }

    private void login() {
        String user = binding.etUsername.getText() != null
                ? binding.etUsername.getText().toString().trim() : "";
        String pass = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        TaiKhoan account = dbHelper.checkLogin(user, SecurityUtils.sha256(pass));
        if (account == null) {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        session.createLoginSession(account);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
