package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlsv_kthp.adapter.DayHeaderAdapter;
import com.example.qlsv_kthp.adapter.TimetableSlotAdapter;
import com.example.qlsv_kthp.databinding.ActivityStudentTimetableBinding;
import com.example.qlsv_kthp.db.DatabaseHelper;
import com.example.qlsv_kthp.model.ThoiKhoaBieu;
import com.example.qlsv_kthp.util.SessionManager;

import java.util.Arrays;
import java.util.List;

public class StudentTimetableActivity extends AppCompatActivity {

    private ActivityStudentTimetableBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private DayHeaderAdapter headerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentTimetableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnExportPdf.setOnClickListener(v ->
                Toast.makeText(this, "Tải PDF thành công", Toast.LENGTH_SHORT).show()
        );

        binding.btnExportIcs.setOnClickListener(v ->
                Toast.makeText(this, "Đã xuất ra lịch máy", Toast.LENGTH_SHORT).show()
        );

        setupTimetable();
    }

    private void setupTimetable() {
        List<String> days = Arrays.asList("T2", "T3", "T4", "T5", "T6", "T7");
        List<String> dates = Arrays.asList("14/04", "15/04", "16/04", "17/04", "18/04", "19/04");

        headerAdapter = new DayHeaderAdapter(days, dates, position ->
                binding.vpDays.setCurrentItem(position)
        );

        binding.rvDayHeaders.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        binding.rvDayHeaders.setAdapter(headerAdapter);

        binding.vpDays.setAdapter(new SchedulePagerAdapter(days));

        binding.vpDays.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (headerAdapter != null) {
                    headerAdapter.setSelectedPosition(position);
                }
            }
        });
    }

    private class SchedulePagerAdapter extends RecyclerView.Adapter<SchedulePagerAdapter.ViewHolder> {

        private final List<String> days;

        public SchedulePagerAdapter(List<String> days) {
            this.days = days;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView recyclerView = new RecyclerView(parent.getContext());

            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));

            return new ViewHolder(recyclerView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int thu = position + 2;

            List<ThoiKhoaBieu> schedule =
                    dbHelper.getTKBBySinhVienAndThu(session.getMaSV(), thu);

            holder.rv.setAdapter(new TimetableSlotAdapter(schedule));
        }

        @Override
        public int getItemCount() {
            return days == null ? 0 : days.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView rv;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                rv = (RecyclerView) itemView;
            }
        }
    }
}