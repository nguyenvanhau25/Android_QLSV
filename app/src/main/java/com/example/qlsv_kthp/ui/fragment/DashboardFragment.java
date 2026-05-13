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
import com.example.qlsv_kthp.util.SessionManager;

public class DashboardFragment extends Fragment {

    private ActivityDashboardBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityDashboardBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());

        // Setup stats
        updateStats();

        // Hide navigation bar inside fragment if it exists in layout (to avoid double nav)
        if (binding.layoutNavBar != null) {
            binding.layoutNavBar.setVisibility(View.GONE);
        }

        return binding.getRoot();
    }

    private void updateStats() {
        SessionManager session = new SessionManager(getContext());
        binding.tvGreeting.setText(getString(R.string.xin_ch_o_c_u_ch, session.getFullName()));

        int students = dbHelper.getCount(DatabaseHelper.TABLE_SINH_VIEN);
        int classes = dbHelper.getCount(DatabaseHelper.TABLE_LOP);
        int subjects = dbHelper.getCount(DatabaseHelper.TABLE_MON_HOC);

        binding.tvStatStudents.setText(String.valueOf(students));
        binding.tvStatClasses.setText(String.valueOf(classes));
        binding.tvStatSubjects.setText(String.valueOf(subjects));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
