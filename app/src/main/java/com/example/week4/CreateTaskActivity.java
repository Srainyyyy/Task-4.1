package com.example.week4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDueDate;
    private ListView listViewTasks;
    private Button buttonSave;
    private TaskManageDB taskDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskDB = new TaskManageDB(this);


        // 初始化视图
        editTextTitle = findViewById(R.id.editText_title);
        editTextDescription = findViewById(R.id.editText_description);
        editTextDueDate = findViewById(R.id.editText_dueDate);
        buttonSave = findViewById(R.id.button_save);


        // 保存按钮点击事件
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });
    }

    // 保存任务
    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();

        // Generate unique task ID
        String taskId = generateTaskId();
        Log.d("TaskManageDB", "Generated Task ID: " + taskId); // Print generated task ID

        // Create a new task object
        Task newTask = new Task(taskId, title, description,dueDate);
        long result = taskDB.addTask(newTask);

        if (result != -1) {
            Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
            //更新列表
            Intent intent = new Intent("com.example.week4.TASK_ADDED");
            ((ArrayAdapter<Task>) listViewTasks.getAdapter()).notifyDataSetChanged();
            finish();
        } else {
            Toast.makeText(this, "Failed to save task", Toast.LENGTH_SHORT).show();
        }
    }


    private String generateTaskId() {
        return "TASK_ID";
    }



}
