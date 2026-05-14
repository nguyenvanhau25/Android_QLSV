package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlsv_kthp.databinding.ActivityStudentFormBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.Lop;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.util.RoleAccessManager;

import java.util.List;

/**
 * Màn hình Thêm/Sửa Sinh viên - Xử lý nhập liệu và lưu trữ
 */
public class StudentFormActivity extends AppCompatActivity {

    private ActivityStudentFormBinding binding;
    private DatabaseHelper dbHelper;
    private int maSV = -1; // -1 nghĩa là đang thêm mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!RoleAccessManager.requireAdmin(this)) {
            return;
        }
        binding = ActivityStudentFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // Tải danh sách lớp vào Spinner
        loadClasses();

        // Kiểm tra xem là chế độ Chỉnh sửa hay Thêm mới
        if (getIntent().hasExtra("maSV")) {
            maSV = getIntent().getIntExtra("maSV", -1);
            binding.tvTitle.setText("Sửa sinh viên");
            loadStudentDataToForm();
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSave.setOnClickListener(v -> {
            saveStudent();
        });
    }

    /**
     * Load dữ liệu sinh viên cũ lên form để chỉnh sửa
     */
    private void loadStudentDataToForm() {
        SinhVien sv = dbHelper.getSinhVienById(maSV);
        if (sv != null) {
            binding.etFullName.setText(sv.getHoTen());
            binding.etDob.setText(sv.getNgaySinh());
            if ("Nam".equals(sv.getGioiTinh())) {
                binding.rbMale.setChecked(true);
            } else {
                binding.rbFemale.setChecked(true);
            }
            binding.etEmail.setText(sv.getEmail());
            binding.etPhone.setText(sv.getSoDienThoai());
            binding.etAddress.setText(sv.getDiaChi());
            
            // Chọn lớp tương ứng trong Spinner
            List<Lop> classes = dbHelper.getAllLop();
            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).getMaLop() == sv.getMaLop()) {
                    binding.spinnerClass.setSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * Tải danh sách lớp từ database
     */
    private void loadClasses() {
        List<Lop> classes = dbHelper.getAllLop();
        ArrayAdapter<Lop> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClass.setAdapter(adapter);
    }

    /**
     * Xử lý lưu thông tin sinh viên
     */
    private void saveStudent() {
        String name = binding.etFullName.getText().toString().trim();
        String dob = binding.etDob.getText().toString().trim();
        String gender = binding.rbMale.isChecked() ? "Nam" : "Nữ";
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        Lop selectedLop = (Lop) binding.spinnerClass.getSelectedItem();

        // Validate dữ liệu cơ bản
        if (name.isEmpty() || selectedLop == null) {
            Toast.makeText(this, "Vui lòng nhập tên và chọn lớp", Toast.LENGTH_SHORT).show();
            return;
        }

        SinhVien sv = new SinhVien();
        sv.setHoTen(name);
        sv.setNgaySinh(dob);
        sv.setGioiTinh(gender);
        sv.setEmail(email);
        sv.setSoDienThoai(phone);
        sv.setDiaChi(address);
        sv.setMaLop(selectedLop.getMaLop());

        if (maSV == -1) {
            // Thêm mới
            long id = dbHelper.insertSinhVien(sv);
            if (id > 0) {
                Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Cập nhật
            sv.setMaSV(maSV);
            int rows = dbHelper.updateSinhVien(sv);
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
