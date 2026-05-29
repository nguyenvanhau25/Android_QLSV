package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivitySubjectFormBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.util.RoleAccessManager;

public class SubjectFormActivity extends AppCompatActivity {

    private ActivitySubjectFormBinding binding;
    private DatabaseHelper dbHelper;
    private int maMH = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!RoleAccessManager.requireAdmin(this)) {
            return;
        }
        binding = ActivitySubjectFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        maMH = getIntent().getIntExtra("maMH", -1);

        if (maMH != -1) {
            binding.tvTitle.setText("Sửa môn học");
        }

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveSubject());
    }

    private void saveSubject() {
        String name = binding.etSubjectName.getText().toString().trim();
        String creditsStr = binding.etCredits.getText().toString().trim();

        if (name.isEmpty() || creditsStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            MonHoc mh = new MonHoc();
            mh.setTenMH(name);
            mh.setSoTinChi(credits);

            if (maMH == -1) {
                dbHelper.insertMonHoc(mh);
                Toast.makeText(this, "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
            } else {
                mh.setMaMH(maMH);
                dbHelper.updateMonHoc(mh);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }

            setResult(RESULT_OK);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tín chỉ phải là số nguyên", Toast.LENGTH_SHORT).show();
        }
    }
}
