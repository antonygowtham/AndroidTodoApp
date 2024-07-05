package com.example.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private MyPagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    private DbHelper dbHelper;
    private ImageView moreMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);


        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Set tab titles based on your tab order
                    switch (position) {
                        case 0:
                            tab.setText("Tasks");


                            break;
                        case 1:
                            tab.setText("Calendar");
                            break;
                        // Add more cases for additional tabs
                    }
                }).attach();


        // Set a click listener for the ImageView to show the menu
        moreMenu = findViewById(R.id.moreMenu);
        moreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.more_menu_resource); // Replace with your actual menu resource
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dbHelper=new DbHelper(getApplicationContext());
                if (item.getItemId() == R.id.menu_item_1) {
                    // Handle the first menu item click
                    dbHelper=new DbHelper(getApplicationContext());
                    dbHelper.deleteTasksWithFinishedList();
                    recreate();
                    return true;
                } else if (item.getItemId() == R.id.menu_item_2) {
                    showDeleteConfirmationDialog();
                    return true;
                }
                // Add more conditions for other menu items as needed
                return false;
            }
        });
        popupMenu.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to Clear Entire List ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteAllTasks(); // This should delete all tasks
                recreate(); // Recreate the activity to reflect the changes
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show(); // Show the AlertDialog
    }

}
