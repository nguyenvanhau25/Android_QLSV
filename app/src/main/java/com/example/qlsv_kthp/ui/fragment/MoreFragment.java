package com.example.qlsv_kthp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qlsv_kthp.databinding.ActivityProfileBinding;
import com.example.qlsv_kthp.ui.activity.LoginActivity;
import com.example.qlsv_kthp.util.SessionManager;

/**
 * Fragment hiển thị thông tin cá nhân và cài đặt (Logout)
 */
public class MoreFragment extends Fragment {

    private ActivityProfileBinding binding;
    private SessionManager session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityProfileBinding.inflate(inflater, container, false);
        session = new SessionManager(getContext());

        // Hiển thị thông tin từ Session
        binding.tvFullName.setText(session.getFullName());
        binding.tvUserRole.setText(session.getUserRole());

        // Xử lý đăng xuất
        binding.btnLogout.setOnClickListener(v -> {
            session.logoutUser();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // Xóa stack và chuyển về màn hình Login
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
