package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.model.Task;

import java.util.List;

public class ReAdapter extends RecyclerView.Adapter<ReAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private DbHelper dbHelper;
    private Context context;

    public ReAdapter(List<Task> taskList, DbHelper dbHelper, Context context) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
        this.context = context;
    }



    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.re_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTextView.setText(task.getTask());
        holder.dateTextView.setText(task.getDueDate());
        holder.timeTextView.setText(task.getDueTime());
        holder.listTextView.setText(task.getList());

        //Delete Task
        holder.deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this task?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteTask(task.getTaskId());
                        taskList.remove(task);
                        notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Edit Task
        holder.editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTaskDialog(task);
            }
        });

        //Finish Task
        holder.addFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!task.getList().equals("finished")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm Finish");
                    builder.setMessage("Are you sure you want to finish this task ?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.updateTaskToListFinished(task.getTaskId());
                            taskList.remove(task);
                            notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirm UnFinish");
                    builder.setMessage("Are you sure you want to 'UnFinish' this task ?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showEditTaskDialog(task);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

        });

        // Finish Text
        if(task.getList().equals("finished")){
            holder.finishText.setText("finished");
        }
        else {
            holder.finishText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView, dateTextView, timeTextView, listTextView, finishText;
        ImageView deleteTask, editTask, addFinish;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            listTextView = itemView.findViewById(R.id.listTextView);
            finishText = itemView.findViewById(R.id.finishText);
            deleteTask = itemView.findViewById(R.id.deleteTask);
            editTask = itemView.findViewById(R.id.editTask);
            addFinish = itemView.findViewById(R.id.addFinish);
        }
    }

    public void updateTaskList(List<Task> updatedList) {
        taskList.clear();
        taskList.addAll(updatedList);
        notifyDataSetChanged();
    }

    private void showEditTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View editTaskView = LayoutInflater.from(context).inflate(R.layout.edit_task_layout, null);
        builder.setView(editTaskView);

        EditText editTaskDescriptionEditText = editTaskView.findViewById(R.id.editTaskDescriptionEditText);
        EditText editDueDateEditText = editTaskView.findViewById(R.id.editDueDateEditText);
        EditText editDueTimeEditText = editTaskView.findViewById(R.id.editDueTimeEditText);
        Spinner prioritySpinner = editTaskView.findViewById(R.id.prioritySpinner);

        editTaskDescriptionEditText.setText(task.getTask());
        editDueDateEditText.setText(task.getDueDate());
        editDueTimeEditText.setText(task.getDueTime());

        String[] listOptions = context.getResources().getStringArray(R.array.list_options);

        int listIndex = -1;
        for (int i = 0; i < listOptions.length; i++) {
            if (listOptions[i].equals(task.getList())) {
                listIndex = i;
                break;
            }
        }

        if (listIndex != -1) {
            prioritySpinner.setSelection(listIndex);
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editedTaskDescription = editTaskDescriptionEditText.getText().toString();
                String editedDueDate = editDueDateEditText.getText().toString();
                String editedDueTime = editDueTimeEditText.getText().toString();
                String editedList = prioritySpinner.getSelectedItem().toString();

                if(!task.getList().equals("finished")){
                    dbHelper.updateTask(task.getTaskId(), editedTaskDescription, editedDueDate, editedDueTime, editedList);
                    taskList.clear();
                    taskList.addAll(dbHelper.getAllTasks());
                    notifyDataSetChanged();
                    Toast.makeText(context, "The task is added to '"+editedList+"' list", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbHelper.updateTask(task.getTaskId(), editedTaskDescription, editedDueDate, editedDueTime, editedList);
                    taskList.remove(task);
                    notifyDataSetChanged();
                    Toast.makeText(context, "The task is added to '"+editedList+"' list", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
