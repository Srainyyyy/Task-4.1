package com.example.week4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    private ListView listViewTasks;
    private List<Task> taskList;
    private TaskManageDB taskDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        // 初始化数据库操作类
        taskDB = new TaskManageDB(this);

        // 初始化任务列表
        taskList = taskDB.getAllTasks();

        // 初始化视图
        listViewTasks = findViewById(R.id.listView_tasks);

        // 设置任务列表适配器
        ArrayAdapter<Task> taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(taskAdapter);

        // 点击任务列表项事件
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskList.get(position);
                // 启动任务详情界面，并传递任务对象
                Intent intent = new Intent(TaskListActivity.this, TaskDetails.class);
                intent.putExtra("task", (Parcelable) task);
                startActivity(intent);
            }
        });
    }
}
