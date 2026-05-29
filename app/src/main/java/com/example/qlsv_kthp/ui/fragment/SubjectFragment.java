package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.qlsv_kthp.adapter.SubjectAdapter;
import com.example.qlsv_kthp.databinding.ActivitySubjectListBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.MonHoc;
import com.example.qlsv_kthp.ui.activity.SubjectDetailActivity;
import com.example.qlsv_kthp.ui.activity.SubjectFormActivity;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.List;

/**
 * Fragment hiển thị danh sách môn học
 * Admin thấy tất cả môn học, sinh viên thấy môn đã đăng ký.
 */
public class SubjectFragment extends Fragment {

    private ActivitySubjectListBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = ActivitySubjectListBinding.inflate(inflater, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        session = new SessionManager(requireContext());

        setupUI();
        loadSubjects();

        return binding.getRoot();
    }

    private void setupUI() {
        binding.rvSubjects.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.tvTitle.setText(session.isAdmin() ? "Quản lý môn học" : "Môn học của tôi");

        binding.btnAdd.setVisibility(session.isAdmin() ? View.VISIBLE : View.GONE);
        binding.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SubjectFormActivity.class);
            startActivityForResult(intent, 100);
        });
    }

    private void loadSubjects() {
        if (binding == null) return;

        List<MonHoc> subjects;

        if (session.isAdmin()) {
            subjects = dbHelper.getAllMonHoc();
        } else {
            subjects = dbHelper.getRegisteredSubjects(session.getMaSV());
        }

        if (subjects == null || subjects.isEmpty()) {
            binding.rvSubjects.setVisibility(View.GONE);
        } else {
            binding.rvSubjects.setVisibility(View.VISIBLE);
        }

        SubjectAdapter adapter = new SubjectAdapter(
                subjects,
                session.isAdmin(),
                new SubjectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MonHoc mh) {
                        Intent intent = new Intent(requireContext(), SubjectDetailActivity.class);
                        intent.putExtra("maMH", mh.getMaMH());
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(MonHoc mh) {
                        if (!session.isAdmin()) {
                            return;
                        }

                        confirmDeleteSubject(mh);
                    }
                }
        );

        binding.rvSubjects.setAdapter(adapter);
    }

    private void confirmDeleteSubject(MonHoc mh) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa môn học")
                .setMessage("Bạn có chắc muốn xóa môn: " + mh.getTenMH() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = dbHelper.deleteMonHoc(mh.getMaMH());

                    if (success) {
                        Toast.makeText(requireContext(), "Đã xóa môn học", Toast.LENGTH_SHORT).show();
                        loadSubjects();
                    } else {
                        Toast.makeText(
                                requireContext(),
                                "Không thể xóa môn học",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == android.app.Activity.RESULT_OK) {
            loadSubjects();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (binding != null) {
            loadSubjects();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}