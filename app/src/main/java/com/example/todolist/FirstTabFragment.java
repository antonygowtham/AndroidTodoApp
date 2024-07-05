package com.example.todolist;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FirstTabFragment extends Fragment {

    public FirstTabFragment() {
        // Required empty public constructor
    }

    FloatingActionButton addList;
    private RecyclerView taskRecyclerView;
    private ReAdapter reAdapter;
    private List<Task> taskList;
    private DbHelper dbHelper;
    private Spinner categorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_first_tab_fragment, container, false);

        //navigation to Add Task page
        addList = view.findViewById(R.id.addTaskFab);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddTask.class);
                startActivity(i);
            }
        });

        //RecyclerView Adapter and DB
        taskList = new ArrayList<>();
        dbHelper = new DbHelper(getActivity());
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView);
        reAdapter = new ReAdapter(taskList, dbHelper, getActivity());
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecyclerView.setAdapter(reAdapter);
        fetchDataFromDatabase();

        //Category Spinner
        List<String> dataList = new ArrayList<>();
        dataList.add("All List");
        dataList.add("finished");
        dataList.add("Personal");
        dataList.add("Shopping");
        dataList.add("Wishlist");
        dataList.add("Work");
        dataList.add("Others");
        categorySpinner = view.findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_layout, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set a listener to the spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedList = parentView.getItemAtPosition(position).toString();
                if (!selectedList.equals("All List")) {
                    // Fetch tasks based on the selected list
                    List<Task> filteredTasks = dbHelper.getTasksByList(selectedList);

                    // Update the RecyclerView adapter with the filtered tasks
                    reAdapter.updateTaskList(filteredTasks);
                } else {
                    fetchDataFromDatabase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void fetchDataFromDatabase() {
        taskList.clear(); // Clear existing data
        taskList.addAll(dbHelper.getAllTasks()); // Replace with your fetching logic
        reAdapter.notifyDataSetChanged(); // Notify the adapter of data change
    }


}
