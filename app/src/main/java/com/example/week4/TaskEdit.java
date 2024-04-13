package com.example.week4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TaskEdit extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextDueDate;
    private Button buttonSave;
    private ListView listViewTasks;
    private Task task;
    private TaskManageDB taskDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        taskDB = new TaskManageDB(this);

        // 初始化视图
        editTextTitle = findViewById(R.id.editText_edittitle);
        editTextDescription = findViewById(R.id.editText_editdescription);
        editTextDueDate = findViewById(R.id.editText_editdueDate);
        buttonSave = findViewById(R.id.button_save);
        taskDB= new TaskManageDB(this);




        // 获取传递过来的任务信息
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra("task");
            if (task != null) {
                editTextTitle.setText(task.getTitle());
                editTextDescription.setText(task.getDescription());
                editTextDueDate.setText(task.getDueDate());
            } else {
                // 如果task为null
                Toast.makeText(TaskEdit.this, "Task object is null", Toast.LENGTH_SHORT).show();
                finish(); // 结束当前Activity
            }
        }else {
            // 如果task为null
            Toast.makeText(TaskEdit.this, "intent object is null", Toast.LENGTH_SHORT).show();
            finish();
        }
        // 保存按钮点击事件
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask();
            }
        });
    }

    // 更新任务信息
    private void updateTask() {
        String newTitle = editTextTitle.getText().toString().trim();
        String newDescription = editTextDescription.getText().toString().trim();
        String newDueDate = editTextDueDate.getText().toString().trim();

        // 更新任务信息并保存到数据库
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        task.setDueDate(newDueDate);
        long result = taskDB.updateTask(task);

        // 判断是否成功更新任务
        if (result != -1) {
            Toast.makeText(TaskEdit.this, "Task updated,Please return to the Task list page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("com.example.week4.TASK_EDIT");
            ((ArrayAdapter<Task>) listViewTasks.getAdapter()).notifyDataSetChanged();
            finish();
        } else {
            Toast.makeText(TaskEdit.this, "Failed to update task", Toast.LENGTH_SHORT).show();
        }
    }
}