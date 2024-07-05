package com.example.todolist;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SecondTabFragment extends Fragment {

    private RecyclerView taskRecyclerView;
    private List<Task> taskList;
    private ReAdapter reAdapter;
    private DbHelper dbHelper;

    private CalendarView taskFilterCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_second_tab_fragment, container, false);
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView);
        taskFilterCalendar = view.findViewById(R.id.taskFilterCalendar);

        taskList = new ArrayList<>();
        dbHelper = new DbHelper(getActivity());
        reAdapter = new ReAdapter(taskList, dbHelper, getActivity());
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecyclerView.setAdapter(reAdapter);

        // Get today's date
        Calendar todayCalendar = Calendar.getInstance();
        int year = todayCalendar.get(Calendar.YEAR);
        int month = todayCalendar.get(Calendar.MONTH);
        int day = todayCalendar.get(Calendar.DAY_OF_MONTH);

        // Set the taskFilterCalendar to today's date
        taskFilterCalendar.setDate(todayCalendar.getTimeInMillis());

        // Format today's date
        String selectedDate = formatDate(year, month, day);

        // Fetch tasks for today
        fetchTasksForDate(selectedDate);

        // Set up a listener for the CalendarView to filter tasks by date
        taskFilterCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                // Convert the selected date to a string in your desired format (e.g., "yyyy-MM-dd")
                String selectedDate = formatDate(year, month, day);

                // Call a method to fetch tasks for the selected date
                fetchTasksForDate(selectedDate);
            }
        });

        return view;
    }

    private String formatDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private void fetchTasksForDate(String selectedDate) {
        taskList.clear(); // Clear existing data
        taskList.addAll(dbHelper.getTasksByDate(selectedDate)); // Replace with your fetching logic
        reAdapter.notifyDataSetChanged(); // Notify the adapter of data change
    }
}
