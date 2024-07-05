package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton addList;
    private RecyclerView taskRecyclerView;
    private ReAdapter reAdapter;
    private List<Task> taskList;
    private DbHelper dbHelper;
    private ImageView moreMenu;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        onSpinnerRefresh();
        setContentView(R.layout.activity_main);

        //navigation to Add Task page
        addList = findViewById(R.id.addTaskFab);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddTask.class);
                startActivity(i);
            }
        });

        //RecyclerView Adapter and DB
        taskList = new ArrayList<>();
        dbHelper = new DbHelper(this);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        reAdapter = new ReAdapter(taskList, dbHelper,this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        categorySpinner = findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set a listener to the spinner
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedList = parentView.getItemAtPosition(position).toString();
                if(selectedList!="All List") {
                    // Fetch tasks based on the selected list
                    List<Task> filteredTasks = dbHelper.getTasksByList(selectedList);

                    // Update the RecyclerView adapter with the filtered tasks
                    reAdapter.updateTaskList(filteredTasks);
                }
                else{
                    fetchDataFromDatabase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // Set a click listener for the ImageView to show the menu
        moreMenu = findViewById(R.id.moreMenu);
        moreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


    }

    private void fetchDataFromDatabase() {
        // Fetch data from database using dbHelper
        taskList.clear(); // Clear existing data
        taskList.addAll(dbHelper.getAllTasks()); // Replace with your fetching logic
        reAdapter.notifyDataSetChanged(); // Notify the adapter of data change
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.more_menu_resource); // Replace with your actual menu resource
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here using if-else statements
                if (item.getItemId() == R.id.menu_item_1) {
                    // Handle the first menu item click
                    dbHelper.deleteTasksWithFinishedList();
                    taskList.clear(); // Clear existing data
                    taskList.addAll(dbHelper.getAllTasks()); // Replace with your fetching logic
                    reAdapter.notifyDataSetChanged(); // Notify the adapter of data change
                    return true;
                } else if (item.getItemId() == R.id.menu_item_2) {
                    // Handle the second menu item click
                    return true;
                }


                // Add more conditions for other menu items as needed
                return false;
            }
        });
        popupMenu.show();
    }


}