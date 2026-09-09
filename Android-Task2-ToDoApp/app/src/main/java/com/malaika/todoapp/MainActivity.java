package com.malaika.todoapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTaskInput;
    private MaterialButton btnAddTask;
    private ListView lvTasks;
    private ImageView btnLogout;

    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private TaskAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTaskInput = findViewById(R.id.etTaskInput);
        btnAddTask = findViewById(R.id.btnAddTask);
        lvTasks = findViewById(R.id.lvTasks);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        loadTasksFromDatabase();

        adapter = new TaskAdapter();
        lvTasks.setAdapter(adapter);

        // Add Task Button Click
        btnAddTask.setOnClickListener(v -> {
            String taskTitle = etTaskInput.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                dbHelper.addTask(taskTitle);
                loadTasksFromDatabase();
                adapter.notifyDataSetChanged();
                etTaskInput.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Please enter a task!", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout action
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadTasksFromDatabase() {
        taskList.clear();
        ArrayList<TaskItem> fromDb = dbHelper.getAllTasks();
        if (fromDb != null) {
            taskList.addAll(fromDb);
        }

        if (taskList.isEmpty()) {
            dbHelper.addTask("Complete Oasis Infobyte Internship Tasks");
            dbHelper.addTask("Push final projects to GitHub");
            ArrayList<TaskItem> defaultTasks = dbHelper.getAllTasks();
            if (defaultTasks != null) {
                taskList.addAll(defaultTasks);
            }
        }
    }

    private class TaskAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return taskList.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_task, parent, false);
            }

            CheckBox cbTask = convertView.findViewById(R.id.cbTask);
            TextView tvTaskText = convertView.findViewById(R.id.tvTaskText);
            ImageView btnDeleteTask = convertView.findViewById(R.id.btnDeleteTask);

            TaskItem currentItem = taskList.get(position);
            tvTaskText.setText(currentItem.title);

            // 1. Remove listener before setting checked state to avoid unwanted triggers
            cbTask.setOnCheckedChangeListener(null);
            cbTask.setChecked(currentItem.isCompleted);
            updateStrikeThrough(tvTaskText, currentItem.isCompleted);

            // 2. Set listener for user actions
            cbTask.setOnClickListener(v -> {
                boolean isChecked = cbTask.isChecked();
                currentItem.isCompleted = isChecked;
                dbHelper.updateTaskStatus(currentItem.id, isChecked);
                updateStrikeThrough(tvTaskText, isChecked);
            });

            // 3. Delete Task Event
            btnDeleteTask.setOnClickListener(v -> {
                dbHelper.deleteTask(currentItem.id);
                loadTasksFromDatabase();
                notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }

        private void updateStrikeThrough(TextView textView, boolean isCompleted) {
            if (isCompleted) {
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textView.setTextColor(0xFF94A3B8);
            } else {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textView.setTextColor(0xFF1E293B);
            }
        }
    }
}