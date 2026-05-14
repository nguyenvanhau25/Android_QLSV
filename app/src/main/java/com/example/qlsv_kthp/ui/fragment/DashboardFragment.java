package com.example.qlsv_kthp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ActivityDashboardBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.SinhVien;
import com.example.qlsv_kthp.util.SessionManager;

public class DashboardFragment extends Fragment {

    private ActivityDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityDashboardBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());
        session = new SessionManager(requireContext());

        updateStats();

        if (binding.layoutNavBar != null) {
            binding.layoutNavBar.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    private void updateStats() {
        binding.tvGreeting.setText(getString(R.string.xin_ch_o_c_u_ch, session.getFullName()));

        if (session.isAdmin()) {
            int students = dbHelper.getCount(DatabaseHelper.TABLE_SINH_VIEN);
            int classes = dbHelper.getCount(DatabaseHelper.TABLE_LOP);
            int subjects = dbHelper.getCount(DatabaseHelper.TABLE_MON_HOC);

            binding.tvStatStudents.setText(String.valueOf(students));
            binding.tvStatClasses.setText(String.valueOf(classes));
            binding.tvStatSubjects.setText(String.valueOf(subjects));
            return;
        }

        SinhVien currentStudent = dbHelper.getSinhVienById(session.getMaSV());
        binding.tvStatStudents.setText("1");
        binding.tvStatClasses.setText(currentStudent != null && currentStudent.getTenLop() != null ? "1" : "0");
        binding.tvStatSubjects.setText(String.valueOf(dbHelper.getCount(DatabaseHelper.TABLE_MON_HOC)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
