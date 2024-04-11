package com.example.week4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskDetails extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDueDate;

    private Button buttonDelete;
    private ListView listViewTasks;
    private List<Task> taskList;
    private Task task;
    private TaskManageDB taskDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // 初始化视图
        textViewTitle = findViewById(R.id.textView_title);
        textViewDescription = findViewById(R.id.textView_description);
        textViewDueDate = findViewById(R.id.textView_dueDate);
        buttonDelete = findViewById(R.id.button_delete);
        taskDB= new TaskManageDB(this);
        taskList = new ArrayList<>(); // 在这里进行初始化

        Task task = (Task) getIntent().getParcelableExtra("task");

        // 获取传递过来的任务信息
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String dueDate = intent.getStringExtra("dueDate");

            // 设置任务详情信息
            textViewTitle.setText(title);
            textViewDescription.setText(description);
            textViewDueDate.setText(dueDate);
        }



        //删除按钮
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    private void deleteTask() {
        Task task = (Task) getIntent().getSerializableExtra("task");
        if (task != null) {
            taskDB.deleteTask(task);
            taskList.remove(task);
            ((ArrayAdapter<Task>) listViewTasks.getAdapter()).notifyDataSetChanged();
            //setResult(RESULT_OK);
            finish();
        }
    }



}
