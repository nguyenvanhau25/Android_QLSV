package com.example.qlsv_kthp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qlsv_kthp.databinding.ActivityMainBinding;
import com.example.qlsv_kthp.ui.activity.LoginActivity;
import com.example.qlsv_kthp.ui.fragment.DashboardFragment;
import com.example.qlsv_kthp.ui.fragment.MoreFragment;
import com.example.qlsv_kthp.ui.fragment.NotificationFragment;
import com.example.qlsv_kthp.ui.fragment.StudentListFragment;
import com.example.qlsv_kthp.ui.fragment.SubjectFragment;
import com.example.qlsv_kthp.util.SessionManager;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        configureNavigationForRole();
        
        if (session.isAdmin()) {
            loadFragment(new DashboardFragment());
        } else {
            loadFragment(new com.example.qlsv_kthp.ui.fragment.StudentDashboardFragment());
        }

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    if (session.isAdmin()) {
                        fragment = new DashboardFragment();
                    } else {
                        fragment = new com.example.qlsv_kthp.ui.fragment.StudentDashboardFragment();
                    }
                } else if (id == R.id.nav_students) {
                    if (session.isAdmin()) {
                        fragment = new StudentListFragment();
                    } else {
                        fragment = new SubjectFragment(); // Shows registered subjects
                    }
                } else if (id == R.id.nav_subjects) {
                    if (session.isAdmin()) {
                        fragment = new SubjectFragment();
                    } else {
                        startActivity(new Intent(MainActivity.this, com.example.qlsv_kthp.ui.activity.StudentSubjectRegisterActivity.class));
                        return false; // Do not select the tab, just open activity
                    }
                } else if (id == R.id.nav_notifications) {
                    fragment = new NotificationFragment();
                } else if (id == R.id.nav_more) {
                    fragment = new MoreFragment();
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }

                Toast.makeText(MainActivity.this, "Bạn không có quyền truy cập chức năng này", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void configureNavigationForRole() {
        if (!session.isAdmin()) {
            Menu menu = binding.bottomNavigation.getMenu();
            MenuItem studentItem = menu.findItem(R.id.nav_students);
            if (studentItem != null) {
                studentItem.setTitle("Cá nhân");
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
