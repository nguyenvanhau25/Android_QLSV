package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityNotificationFormBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.ThongBao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationFormActivity extends AppCompatActivity {

    private ActivityNotificationFormBinding binding;
    private DatabaseHelper dbHelper;
    private int maThongBao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        maThongBao = getIntent().getIntExtra("maThongBao", -1);

        setupUI();
        if (maThongBao != -1) {
            loadData();
        }
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> finish());
        
        String[] types = {"Chung", "Học tập", "Lịch thi", "Hệ thống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types);
        binding.actvType.setAdapter(adapter);

        binding.btnSave.setOnClickListener(v -> saveNotification());
        
        if (maThongBao != -1) {
            binding.tvTitle.setText("Chỉnh sửa thông báo");
        }
    }

    private void loadData() {
        String tieuDe = getIntent().getStringExtra("tieuDe");
        String noiDung = getIntent().getStringExtra("noiDung");
        String loai = getIntent().getStringExtra("loai");

        binding.etTitle.setText(tieuDe);
        binding.etContent.setText(noiDung);
        binding.actvType.setText(loai != null ? loai : "Chung", false);
    }

    private void saveNotification() {
        String title = binding.etTitle.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();
        String type = binding.actvType.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ThongBao tb = new ThongBao();
        tb.setTieuDe(title);
        tb.setNoiDung(content);
        tb.setLoai(type);
        tb.setDaDoc(0);

        if (maThongBao == -1) {
            // Thêm mới
            String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            tb.setNgayTao(currentTime);
            long id = dbHelper.insertThongBao(tb);
            if (id != -1) {
                Toast.makeText(this, "Đã thêm thông báo mới", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm thông báo", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Cập nhật
            tb.setMaThongBao(maThongBao);
            int rows = dbHelper.updateThongBao(tb);
            if (rows > 0) {
                Toast.makeText(this, "Đã cập nhật thông báo", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật thông báo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
