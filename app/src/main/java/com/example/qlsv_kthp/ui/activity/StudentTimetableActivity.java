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
    private List<String> dayNames;
    private List<String> dateStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentTimetableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.btnExportPdf.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng Xuất PDF đang được phát triển", Toast.LENGTH_SHORT).show()
        );

        setupTimetable();
    }

    private void setupTimetable() {
        calculateCurrentWeekDates();

        headerAdapter = new DayHeaderAdapter(dayNames, dateStrings, position ->
                binding.vpDays.setCurrentItem(position)
        );

        binding.rvDayHeaders.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        binding.rvDayHeaders.setAdapter(headerAdapter);
        binding.vpDays.setAdapter(new SchedulePagerAdapter());

        // Tự động cuộn đến ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Monday = 2...
        int currentPos = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - 2;
        binding.vpDays.setCurrentItem(currentPos, false);
        headerAdapter.setSelectedPosition(currentPos);

        binding.vpDays.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerAdapter.setSelectedPosition(position);
                binding.rvDayHeaders.smoothScrollToPosition(position);
            }
        });
    }

    private void calculateCurrentWeekDates() {
        dayNames = new ArrayList<>();
        dateStrings = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String[] shortDays = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};

        for (int i = 0; i < 7; i++) {
            dayNames.add(shortDays[i]);
            dateStrings.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private class SchedulePagerAdapter extends RecyclerView.Adapter<SchedulePagerAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView recyclerView = new RecyclerView(parent.getContext());
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            recyclerView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
            recyclerView.setPadding(0, 16, 0, 80);
            recyclerView.setClipToPadding(false);
            return new ViewHolder(recyclerView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // DB lưu Thứ 2 là 2, Thứ 3 là 3... Chủ Nhật là 8
            int thu = position + 2;
            List<ThoiKhoaBieu> schedule = dbHelper.getTKBBySinhVienAndThu(session.getMaSV(), thu);
            
            if (schedule == null || schedule.isEmpty()) {
                // Có thể thêm view "Trống" ở đây nếu muốn
            }
            holder.rv.setAdapter(new TimetableSlotAdapter(schedule));
        }

        @Override
        public int getItemCount() {
            return 7;
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