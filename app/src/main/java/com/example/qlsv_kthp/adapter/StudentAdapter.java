package com.example.qlsv_kthp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlsv_kthp.databinding.ItemStudentBinding;
import com.example.qlsv_kthp.model.SinhVien;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<SinhVien> studentList;
    private OnItemClickListener listener;

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
        holder.binding.tvStudentName.setText(student.getHoTen());
        holder.binding.tvStudentMeta.setText(student.getMaSV() + " • " + (student.getTenLop() != null ? student.getTenLop() : "N/A"));
        holder.binding.tvAvatarInitials.setText(student.getInitial());
        
        // Example score, logic can be updated later
        holder.binding.tvStudentScore.setText("GPA: 0.0");

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemStudentBinding binding;

        public ViewHolder(ItemStudentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
