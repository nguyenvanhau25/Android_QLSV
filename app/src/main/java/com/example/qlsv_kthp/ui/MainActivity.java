package com.example.qlsv_kthp.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qlsv_kthp.R;
import com.example.qlsv_kthp.databinding.ActivityMainBinding;
import com.example.qlsv_kthp.ui.fragment.DashboardFragment;
import com.example.qlsv_kthp.ui.fragment.MoreFragment;
import com.example.qlsv_kthp.ui.fragment.NotificationFragment;
import com.example.qlsv_kthp.ui.fragment.StudentListFragment;
import com.example.qlsv_kthp.ui.fragment.SubjectFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load default fragment
        loadFragment(new DashboardFragment());

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    fragment = new DashboardFragment();
                } else if (id == R.id.nav_students) {
                    fragment = new StudentListFragment();
                } else if (id == R.id.nav_subjects) {
                    fragment = new SubjectFragment();
                } else if (id == R.id.nav_notifications) {
                    fragment = new NotificationFragment();
                } else if (id == R.id.nav_more) {
                    fragment = new MoreFragment();
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
