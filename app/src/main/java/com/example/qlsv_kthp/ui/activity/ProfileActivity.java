package com.example.qlsv_kthp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityProfileBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.model.TaiKhoan;
import com.example.qlsv_kthp.util.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager session;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        bindProfile();

        binding.btnAccountInfo.setOnClickListener(v -> showAccountInfo());
        binding.btnChangePassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        binding.btnCreateNotification.setVisibility(session.isAdmin() ? View.VISIBLE : View.GONE);
        binding.btnCreateNotification.setOnClickListener(v -> {
            android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            int padding = (int) (20 * getResources().getDisplayMetrics().density);
            layout.setPadding(padding, padding / 2, padding, 0);

            com.google.android.material.textfield.TextInputEditText etTitle = new com.google.android.material.textfield.TextInputEditText(this);
            etTitle.setHint("Tiêu đề thông báo");
            com.google.android.material.textfield.TextInputEditText etContent = new com.google.android.material.textfield.TextInputEditText(this);
            etContent.setHint("Nội dung");
            
            layout.addView(etTitle);
            layout.addView(etContent);

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Tạo thông báo mới")
                    .setView(layout)
                    .setPositiveButton("Tạo", (dialog, which) -> {
                        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
                        String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";
                        if (title.isEmpty() || content.isEmpty()) {
                            android.widget.Toast.makeText(this, "Vui lòng nhập đầy đủ", android.widget.Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
                        com.example.qlsv_kthp.model.ThongBao tb = new com.example.qlsv_kthp.model.ThongBao(0, title, content, date, 0, "general");
                        dbHelper.insertThongBao(tb);
                        android.widget.Toast.makeText(this, "Tạo thông báo thành công", android.widget.Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        binding.btnDemoAccounts.setVisibility(View.GONE);
        binding.btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void bindProfile() {
        String name = session.getFullName();
        binding.tvFullName.setText(name);
        
        if (session.isAdmin()) {
            binding.tvUserRole.setText("Giảng viên / Quản trị");
            binding.tvUserMeta.setText("Tài khoản hệ thống");
        } else {
            binding.tvUserRole.setText("Sinh viên");
            // Hiển thị tên lớp thay vì văn bản tĩnh
            SinhVien sv = dbHelper.getSinhVienById(session.getMaSV());
            if (sv != null && sv.getTenLop() != null) {
                binding.tvUserMeta.setText("Lớp: " + sv.getTenLop());
                // Thiết lập click để xem tài liệu
                binding.tvUserMeta.setOnClickListener(v -> 
                    startActivity(new Intent(this, StudentDocumentActivity.class)));
                // Làm cho text trông như có thể nhấn được
                binding.tvUserMeta.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                binding.tvUserMeta.setText("Chưa xếp lớp");
            }
        }

        if (!TextUtils.isEmpty(name)) {
            String[] parts = name.trim().split("\\s+");
            binding.tvAvatarInitials.setText(parts[parts.length - 1].substring(0, 1).toUpperCase());
        }
    }

    private void showAccountInfo() {
        TaiKhoan taiKhoan = dbHelper.getTaiKhoanById(session.getUserId());
        if (taiKhoan == null) {
            return;
        }

        StringBuilder message = new StringBuilder()
                .append("Họ tên: ").append(taiKhoan.getHoTen())
                .append("\nEmail: ").append(taiKhoan.getEmail())
                .append("\nVai trò: ").append(session.isAdmin() ? "Giảng viên / Quản trị" : "Sinh viên");

        if (taiKhoan.getMaSV() > 0) {
            message.append("\nMã sinh viên: ").append(taiKhoan.getMaSV());
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle("Thông tin cá nhân")
                .setMessage(message.toString())
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (d, w) -> {
                    session.logoutUser();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
