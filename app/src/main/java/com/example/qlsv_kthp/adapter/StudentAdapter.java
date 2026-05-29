package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemStudentBinding;
import com.example.qlsv_kthp.model.SinhVien;

import java.util.List;

/**
 * Bộ nạp dữ liệu (Adapter) cho danh sách Sinh viên
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<SinhVien> studentList;
    private OnItemClickListener listener;

    /**
     * Giao diện xử lý sự kiện khi nhấn vào một sinh viên
     */
    public interface OnItemClickListener {
        void onItemClick(SinhVien student);
    }

    public StudentAdapter(List<SinhVien> studentList, OnItemClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudentBinding binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien student = studentList.get(position);
        
        // Hiển thị thông tin cơ bản
        holder.binding.tvStudentName.setText(student.getHoTen());
        holder.binding.tvStudentMeta.setText(student.getMaSV() + " • " + (student.getTenLop() != null ? student.getTenLop() : "Chưa xếp lớp"));
        
        // Hiển thị ký tự viết tắt làm ảnh đại diện tạm thời
        holder.binding.tvAvatarInitials.setText(student.getInitial());
        
        // Mặc định hiển thị điểm (logic thật sẽ lấy từ Database)
        com.example.qlsv_kthp.db.DatabaseHelper dbHelper = new com.example.qlsv_kthp.db.DatabaseHelper(holder.itemView.getContext());
        float gpa = dbHelper.getGPA(student.getMaSV());
        holder.binding.tvStudentScore.setText(String.format("GPA: %.2f", gpa));

        // Sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    /**
     * Lớp giữ các view trong một dòng dữ liệu
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemStudentBinding binding;

        public ViewHolder(ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
