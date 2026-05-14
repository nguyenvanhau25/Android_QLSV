package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.SubjectAdapter;
import com.example.qlsv_kthp.databinding.ActivitySubjectListBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.ui.activity.SubjectFormActivity;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;

public class SubjectFragment extends Fragment {

    private ActivitySubjectListBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySubjectListBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());
        session = new SessionManager(requireContext());

        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.btnAdd.setVisibility(session.isAdmin() ? View.VISIBLE : View.GONE);

        binding.btnAdd.setOnClickListener(v -> {
            if (!session.isAdmin()) {
                Toast.makeText(getContext(), "Sinh viên không được phép thêm môn học", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(new Intent(getActivity(), SubjectFormActivity.class), 100);
        });

        loadSubjects();
        return binding.getRoot();
    }

    private void loadSubjects() {
        List<MonHoc> subjects = dbHelper.getAllMonHoc();
        SubjectAdapter adapter = new SubjectAdapter(subjects, session.isAdmin(), new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MonHoc mh) {
            }

            @Override
            public void onDeleteClick(MonHoc mh) {
                if (!session.isAdmin()) {
                    Toast.makeText(getContext(), "Sinh viên không được phép xóa môn học", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = dbHelper.deleteMonHoc(mh.getMaMH());
                if (success) {
                    loadSubjects();
                } else {
                    Toast.makeText(getContext(), "Không thể xóa môn học đã có dữ liệu liên quan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.rvSubjects.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null && resultCode == getActivity().RESULT_OK) {
            loadSubjects();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
