package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlsv_kthp.databinding.FragmentStudentDashboardBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.ui.activity.ProfileActivity;
import com.example.qlsv_kthp.ui.activity.StudentDocumentActivity;
import com.example.qlsv_kthp.ui.activity.StudentScoreActivity;
import com.example.qlsv_kthp.ui.activity.StudentSubjectRegisterActivity;
import com.example.qlsv_kthp.ui.activity.StudentTimetableActivity;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.Locale;

/**
 * Dashboard dành riêng cho Sinh viên - Refactored by Senior Developer
 */
public class StudentDashboardFragment extends Fragment {

    private FragmentStudentDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStudentDashboardBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        session = new SessionManager(requireContext());

        setupDashboard();
        setupClickListeners();
        return binding.getRoot();
    }

    private void setupDashboard() {
        int maSV = session.getMaSV();
        binding.tvWelcome.setText("Chào mừng, " + session.getFullName());
        
        // Hiển thị GPA
        float gpa = dbHelper.getGPA(maSV);
        binding.tvGpaValue.setText(String.format(Locale.getDefault(), "%.2f", gpa));
        
        // Hiển thị số môn đã đăng ký
        int subjectCount = dbHelper.getRegisteredSubjects(maSV).size();
        binding.tvSubjectCount.setText(String.valueOf(subjectCount));
        
        // Thông báo mới (giả lập hoặc lấy từ DB)
        int unreadNotif = dbHelper.countUnreadThongBao();
        binding.tvUnreadNotif.setText(unreadNotif + " thông báo mới");
    }

    private void setupClickListeners() {
        binding.cardRegister.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), StudentSubjectRegisterActivity.class)));
        
        binding.cardScore.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), StudentScoreActivity.class)));
            
        binding.cardTimetable.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), StudentTimetableActivity.class)));
            
        binding.cardProfile.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), ProfileActivity.class)));

        // Mở tài liệu môn học khi nhấn vào thống kê môn học
        binding.cardSubjectStats.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), StudentDocumentActivity.class)));
            
        // Mở bảng điểm khi nhấn vào GPA
        binding.cardGpa.setOnClickListener(v -> 
            startActivity(new Intent(getActivity(), StudentScoreActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        setupDashboard(); // Refresh data when returning to dashboard
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
