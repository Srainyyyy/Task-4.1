package com.example.week4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskDetails extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDueDate;

    private Button buttonDelete;
    private Button buttonEdit;
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
        buttonEdit=findViewById(R.id.button_edit);
        taskDB= new TaskManageDB(this);
        taskList = new ArrayList<>(); // 在这里进行初始化




        // 获取传递过来的任务信息
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task) intent.getSerializableExtra("task");

            if (task != null) {
                String title = intent.getStringExtra("title");
                String description = intent.getStringExtra("description");
                String dueDate = intent.getStringExtra("dueDate");
                // 设置任务详情信息
                textViewTitle.setText(title);
                textViewDescription.setText(description);
                textViewDueDate.setText(dueDate);
            } else {
                Toast.makeText(TaskDetails.this, "Task object is null", Toast.LENGTH_SHORT).show();
                finish(); // 结束当前Activity
            }
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask();
            }
        });

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
    private void editTask() {

        // 获取当前任务的标题、描述和截止日期
        String title = textViewTitle.getText().toString();
        String description = textViewDescription.getText().toString();
        String dueDate = textViewDueDate.getText().toString();

        // 创建编辑任务的意图
        Intent intent = new Intent(TaskDetails.this, TaskEdit.class);
        intent.putExtra("task", task);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("dueDate", dueDate);
        startActivity(intent);
    }



}
