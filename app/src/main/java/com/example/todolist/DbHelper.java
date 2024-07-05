package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_DUE_TIME = "due_time"; // New time column
    public static final String COLUMN_LIST = "list";

    // Create table query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASK + " TEXT NOT NULL, " +
                    COLUMN_DUE_DATE + " TEXT, " +
                    COLUMN_DUE_TIME + " TEXT, " +
                    COLUMN_LIST + " TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long insertTask(String task, String dueDate, String dueTime, String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_DUE_TIME, dueTime); // Insert due time
        values.put(COLUMN_LIST, list);
        return db.insert(TABLE_TASKS, null, values);
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to exclude tasks with list="finished"
        String selection = COLUMN_LIST + " != ?";
        String[] selectionArgs = new String[]{"finished"};

        Cursor cursor = db.query(TABLE_TASKS, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int taskIdIndex = cursor.getColumnIndex(COLUMN_ID);
            int taskIndex = cursor.getColumnIndex(COLUMN_TASK);
            int dueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
            int dueTimeIndex = cursor.getColumnIndex(COLUMN_DUE_TIME);
            int listIndex = cursor.getColumnIndex(COLUMN_LIST);

            // Check if the columns exist in the result set
            if (taskIdIndex != -1 && taskIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1 && listIndex != -1) {
                int taskId = cursor.getInt(taskIdIndex);
                String task = cursor.getString(taskIndex);
                String dueDate = cursor.getString(dueDateIndex);
                String dueTime = cursor.getString(dueTimeIndex);
                String list = cursor.getString(listIndex);

                Task taskObj = new Task(taskId, task, dueDate, dueTime, list);
                taskList.add(taskObj);
            } else {
                // Handle the case where one or more columns are missing in the result set
                // You can log an error or take appropriate action here
            }
        }

        cursor.close();

        return taskList;
    }

    public List<Task> getTasksByList(String selectedList) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_LIST + " = ?";
        String[] selectionArgs = { selectedList };

        Cursor cursor = db.query(TABLE_TASKS, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int taskIdIndex = cursor.getColumnIndex(COLUMN_ID);
            int taskIndex = cursor.getColumnIndex(COLUMN_TASK);
            int dueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
            int dueTimeIndex = cursor.getColumnIndex(COLUMN_DUE_TIME);
            int listIndex = cursor.getColumnIndex(COLUMN_LIST);

            // Check if the columns exist in the result set
            if (taskIdIndex != -1 && taskIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1 && listIndex != -1) {
                int taskId = cursor.getInt(taskIdIndex);
                String task = cursor.getString(taskIndex);
                String dueDate = cursor.getString(dueDateIndex);
                String dueTime = cursor.getString(dueTimeIndex);
                String list = cursor.getString(listIndex);

                Task taskObj = new Task(taskId, task, dueDate, dueTime, list);
                taskList.add(taskObj);
            } else {
                // Handle the case where one or more columns are missing in the result set
                // You can log an error or take appropriate action here
            }
        }

        cursor.close();

        return taskList;
    }

    //update to finished list
    public void updateTaskToListFinished(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST, "finished"); // Update "list" to "finished"

        // Define a WHERE clause to specify which task to update based on its ID
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(taskId)};

        // Update the task in the database
        db.update(TABLE_TASKS, values, whereClause, whereArgs);
    }


    // Add this method to delete records with 'finished' list
    public void deleteTasksWithFinishedList() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define a WHERE clause to specify the condition for deletion
        String whereClause = COLUMN_LIST + "=?";
        String[] whereArgs = { "finished" }; // Specify the value to match

        // Perform the delete operation
        db.delete(TABLE_TASKS, whereClause, whereArgs);
    }

    //DELETE PARTICULAR TASK
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define a WHERE clause to specify which task to delete based on its ID
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(taskId)};

        // Perform the delete operation
        db.delete(TABLE_TASKS, whereClause, whereArgs);
    }

    // Update task details
    public void updateTask(int taskId, String task, String dueDate, String dueTime,String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK, task);
        values.put(COLUMN_DUE_DATE, dueDate);
        values.put(COLUMN_DUE_TIME, dueTime);
        values.put(COLUMN_LIST, list);

        // Define a WHERE clause to specify which task to update based on its ID
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = { String.valueOf(taskId) };

        // Update the task in the database
        db.update(TABLE_TASKS, values, whereClause, whereArgs);
    }

    public List<Task> getTasksByDate(String selectedDate) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_DUE_DATE + " = ?";
        String[] selectionArgs = { selectedDate };

        Cursor cursor = db.query(TABLE_TASKS, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int taskIdIndex = cursor.getColumnIndex(COLUMN_ID);
            int taskIndex = cursor.getColumnIndex(COLUMN_TASK);
            int dueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
            int dueTimeIndex = cursor.getColumnIndex(COLUMN_DUE_TIME);
            int listIndex = cursor.getColumnIndex(COLUMN_LIST);

            // Check if the columns exist in the result set
            if (taskIdIndex != -1 && taskIndex != -1 && dueDateIndex != -1 && dueTimeIndex != -1 && listIndex != -1) {
                int taskId = cursor.getInt(taskIdIndex);
                String task = cursor.getString(taskIndex);
                String dueDate = cursor.getString(dueDateIndex);
                String dueTime = cursor.getString(dueTimeIndex);
                String list = cursor.getString(listIndex);

                Task taskObj = new Task(taskId, task, dueDate, dueTime, list);
                taskList.add(taskObj);
            } else {
                // Handle the case where one or more columns are missing in the result set
                // You can log an error or take appropriate action here
            }
        }

        cursor.close();

        return taskList;
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, null, null);
    }
}

