package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

public class AddTask extends AppCompatActivity {
    private EditText taskDescriptionEditText;
    private EditText dueDateEditText;
    private Spinner prioritySpinner;
    private Button saveTaskButton;
    private EditText dueTimeEditText;
    private ImageView datePickerIcon;
    private ImageView timePickerIcon;
    private ImageView backImage;

    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedTime = Calendar.getInstance();
    private DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_task);
        dbHelper = new DbHelper(this);

        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        dueTimeEditText= findViewById(R.id.dueTimeEditText);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        saveTaskButton = findViewById(R.id.saveTaskButton);
        datePickerIcon = findViewById(R.id.datePickerIcon);
        timePickerIcon = findViewById(R.id.timePickerIcon);

        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        timePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // Call createNotificationChannel to create the notification channel
        createNotificationChannel();

        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = taskDescriptionEditText.getText().toString();
                String dueDate = dueDateEditText.getText().toString();
                String list = prioritySpinner.getSelectedItem().toString();
                String dueTime=dueTimeEditText.getText().toString();

                if (task.isEmpty() || dueDate.isEmpty() || dueTime.isEmpty()) {
                    // Display a Toast message indicating that all fields are required.
                    Toast.makeText(AddTask.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    long result = dbHelper.insertTask(task, dueDate, dueTime, list);
                    if (result != -1) {
                        // Task inserted successfully
                        Toast.makeText(AddTask.this, "task inserted ,due time:"+dueTime, Toast.LENGTH_SHORT).show();
                        // Schedule a notification for the task
                        scheduleNotification(task, dueDate, dueTime);
                        Intent i = new Intent(AddTask.this, MainTabActivity.class);
                        startActivity(i);
                    } else {
                        // Error inserting task
                        // Handle error case here
                        Toast.makeText(AddTask.this, "Error inserting task", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //BACK TO MAIN
        backImage=findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDueDateEditText();
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        updateDueTimeEditText();
                    }
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false // 24-hour format
        );

        timePickerDialog.show();
    }


    private void updateDueDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedDate = dateFormat.format(selectedDate.getTime());
        dueDateEditText.setText(formattedDate);
    }

    private void updateDueTimeEditText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.US);
        String formattedTime = timeFormat.format(selectedTime.getTime());
        dueTimeEditText.setText(formattedTime);
    }


    //Create a Notification Channel
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TaskReminderChannel";
            String description = "Channel for task reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("task_reminder", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //scheduleNotification
    private void scheduleNotification(String task, String dueDate, String dueTime) {
        try {
            // Combine dueDate and dueTime into a single datetime string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            String dueDateTimeString = dueDate + " " + dueTime;

            // Parse the combined datetime string
            Date dueDateTime = dateFormat.parse(dueDateTimeString);

            // Get the time in milliseconds for the scheduled datetime
            long scheduledTimeInMillis = dueDateTime.getTime();

            // Create an Intent that will be triggered when the alarm goes off
            Intent notificationIntent = new Intent(this, TaskNotificationReceiver.class);
            notificationIntent.putExtra("task", task);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            // Schedule the notification using AlarmManager
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, scheduledTimeInMillis, pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing error (invalid date/time format)
        }
    }
}