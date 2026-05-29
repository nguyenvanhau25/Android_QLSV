package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.MainActivity;
import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.util.SessionManager;

/**
 * Màn hình chào (Splash Screen) - Xử lý điều hướng ban đầu
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Đợi 2 giây để hiển thị logo trước khi điều hướng
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager session = new SessionManager(SplashActivity.this);
                
                // Kiểm tra trạng thái đăng nhập để điều hướng phù hợp
                if (session.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
