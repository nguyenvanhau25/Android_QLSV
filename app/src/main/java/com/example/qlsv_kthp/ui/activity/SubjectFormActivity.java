package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivitySubjectFormBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.util.RoleAccessManager;

/**
 * Màn hình thêm/sửa môn học - Refactored by Senior Developer
 */
public class SubjectFormActivity extends AppCompatActivity {

    private ActivitySubjectFormBinding binding;
    private DatabaseHelper dbHelper;
    private int maMH = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Kiểm tra quyền Admin/Teacher trước khi truy cập
        if (!RoleAccessManager.requireAdmin(this)) {
            finish();
            return;
        }
        
        binding = ActivitySubjectFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        maMH = getIntent().getIntExtra("maMH", -1);

        if (maMH != -1) {
            binding.tvTitle.setText("Sửa môn học");
            loadSubjectData();
        } else {
            binding.tvTitle.setText("Thêm môn học");
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveSubject());
    }

    private void loadSubjectData() {
        MonHoc mh = dbHelper.getMonHocById(maMH);
        if (mh != null) {
            binding.etSubjectName.setText(mh.getTenMH());
            binding.etCredits.setText(String.valueOf(mh.getSoTinChi()));
            binding.etInstructor.setText(mh.getGiangVien());
            binding.etSchedule.setText(mh.getLichHoc());
            binding.etMaterials.setText(mh.getTaiLieu());
            binding.etMaxStudents.setText(String.valueOf(mh.getSoLuongMax()));
        }
    }

    private void saveSubject() {
        String name = binding.etSubjectName.getText().toString().trim();
        String creditsStr = binding.etCredits.getText().toString().trim();
        String instructor = binding.etInstructor.getText().toString().trim();
        String schedule = binding.etSchedule.getText().toString().trim();
        String materials = binding.etMaterials.getText().toString().trim();
        String maxStudentsStr = binding.etMaxStudents.getText().toString().trim();

        // Validate đầu vào
        if (name.isEmpty() || creditsStr.isEmpty()) {
            Toast.makeText(this, "Tên môn và số tín chỉ là bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            int maxStudents = maxStudentsStr.isEmpty() ? 65 : Integer.parseInt(maxStudentsStr);

            MonHoc mh = new MonHoc();
            mh.setTenMH(name);
            mh.setSoTinChi(credits);
            mh.setGiangVien(instructor);
            mh.setLichHoc(schedule);
            mh.setTaiLieu(materials);
            mh.setSoLuongMax(maxStudents);

            if (maMH == -1) {
                long id = dbHelper.insertMonHoc(mh);
                if (id != -1) {
                    Toast.makeText(this, "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm môn học", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                mh.setMaMH(maMH);
                int rows = dbHelper.updateMonHoc(mh);
                if (rows > 0) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            setResult(RESULT_OK);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Dữ liệu số (tín chỉ/số lượng) không hợp lệ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
