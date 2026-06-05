package com.example.qlsv_kthp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StudentTimetableActivity extends AppCompatActivity {

    private ActivityStudentTimetableBinding binding;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private DayHeaderAdapter headerAdapter;
    private Calendar currentWeekCalendar;
    private List<String> dayLabels;
    private List<String> dateStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentTimetableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);
        currentWeekCalendar = Calendar.getInstance();
        currentWeekCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        setupWeekNavigation();
        setupTimetable();
    }

    private void setupWeekNavigation() {
        binding.btnPrevWeek.setOnClickListener(v -> {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            updateWeekUI();
        });

        binding.btnNextWeek.setOnClickListener(v -> {
            currentWeekCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            updateWeekUI();
        });
    }

    private void setupTimetable() {
        dayLabels = new ArrayList<>();
        dateStrings = new ArrayList<>();
        
        headerAdapter = new DayHeaderAdapter(dayLabels, dateStrings, position ->
                binding.vpDays.setCurrentItem(position)
        );

        binding.rvDayHeaders.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvDayHeaders.setAdapter(headerAdapter);
        binding.vpDays.setAdapter(new SchedulePagerAdapter());

        binding.vpDays.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerAdapter.setSelectedPosition(position);
                binding.rvDayHeaders.smoothScrollToPosition(position);
            }
        });

        updateWeekUI();
        
        // Mặc định nhảy tới ngày hiện tại nếu đang ở tuần hiện tại
        Calendar today = Calendar.getInstance();
        if (isSameWeek(today, currentWeekCalendar)) {
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
            int pos = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - 2;
            binding.vpDays.setCurrentItem(pos, false);
        }
    }

    private void updateWeekUI() {
        dayLabels.clear();
        dateStrings.clear();

        Calendar cal = (Calendar) currentWeekCalendar.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String weekStart = sdfDate.format(cal.getTime());

        String[] labels = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (int i = 0; i < 7; i++) {
            dayLabels.add(labels[i]);
            dateStrings.add(sdfDate.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        cal.add(Calendar.DAY_OF_MONTH, -1);
        String weekEnd = sdfDate.format(cal.getTime());
        binding.tvWeekRange.setText(weekStart + " - " + weekEnd);

        headerAdapter.notifyDataSetChanged();
        // Refresh pager content
        binding.vpDays.getAdapter().notifyDataSetChanged();
    }

    private boolean isSameWeek(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }

    private class SchedulePagerAdapter extends RecyclerView.Adapter<SchedulePagerAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView rv = new RecyclerView(parent.getContext());
            rv.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            rv.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            rv.setPadding(0, 16, 0, 100);
            rv.setClipToPadding(false);
            return new ViewHolder(rv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            int thu = position + 2; // T2=2...CN=8
            List<ThoiKhoaBieu> schedule = dbHelper.getTKBBySinhVienAndThu(session.getMaSV(), thu);
            holder.rv.setAdapter(new TimetableSlotAdapter(schedule));
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView rv;
            ViewHolder(View v) { super(v); rv = (RecyclerView) v; }
        }
    }
}