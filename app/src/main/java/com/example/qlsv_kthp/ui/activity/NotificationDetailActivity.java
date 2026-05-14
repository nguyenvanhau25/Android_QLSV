package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.db.DatabaseHelper;

public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        int maThongBao = getIntent().getIntExtra("maThongBao", -1);
        String tieuDe = getIntent().getStringExtra("tieuDe");
        String noiDung = getIntent().getStringExtra("noiDung");
        String ngayTao = getIntent().getStringExtra("ngayTao");

        if (maThongBao != -1) {
            dbHelper.markThongBaoAsRead(maThongBao);
        }

        TextView tvTitle = findViewById(R.id.tvNotificationTitle);
        TextView tvDate = findViewById(R.id.tvNotificationDate);
        TextView tvContent = findViewById(R.id.tvNotificationContent);

        tvTitle.setText(tieuDe != null ? tieuDe : "");
        tvDate.setText(ngayTao != null ? ngayTao : "");
        tvContent.setText(noiDung != null ? noiDung : "");
    }
}
