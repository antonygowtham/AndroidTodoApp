package com.example.todolist.model;

public class Task {
    private int taskId; // Add taskId field
    private String task;
    private String dueDate;
    private String dueTime;
    private String list;



    public Task(int taskId, String task, String dueDate, String dueTime, String list) {
        this.taskId = taskId;
        this.task = task;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.list = list;
    }

    public int getTaskId() {return taskId;}

    public String getTask() {
        return task;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public String getList() {
        return list;
    }
}
