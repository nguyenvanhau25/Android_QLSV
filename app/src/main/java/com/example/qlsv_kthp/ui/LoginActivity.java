package com.example.qlsv_kthp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.R;
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

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = binding.etUsername.getText().toString().trim();
                String pass = binding.etPassword.getText().toString().trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hash input password
                String hashedPass = SecurityUtils.sha256(pass);
                TaiKhoan account = dbHelper.checkLogin(user, hashedPass);
                
                if (account != null) {
                    session.createLoginSession(account);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
