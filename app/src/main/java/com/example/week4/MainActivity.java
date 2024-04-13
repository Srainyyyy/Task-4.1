package com.example.week4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listViewTasks;
    private List<Task> taskList;
    private TaskManageDB taskDB;
    private static final int REQUEST_CODE_CREATE_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库操作类
        taskDB = new TaskManageDB(this);

        // 初始化任务列表
        taskList = new ArrayList<>();

        // 初始化视图
        listViewTasks = findViewById(R.id.listView_tasks);

        // 模拟数据
        populateTaskList();

        // 设置任务列表适配器
        ArrayAdapter<Task> taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(taskAdapter);

        // 点击任务列表项事件

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskList.get(position);
                // 处理点击事件，例如跳转到任务详情界面
                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                // 将任务的标题、描述和截止日期传递到 TaskDetails 活动
                intent.putExtra("task", task);
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("dueDate", task.getDueDate());
                startActivity(intent);
            }
        });

        // 新建任务按钮点击事件
        findViewById(R.id.fab_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动新建任务界面
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);

                startActivity(intent);
            }
        });
    }

    // 模拟数据
    private void populateTaskList() {
        // 从数据库中获取任务数据

        taskList.addAll(taskDB.getAllTasks());
        Log.d("TaskList", "Task list size: " + taskList.size());
    }
    // 处理结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_TASK) {
            if (resultCode == RESULT_OK) {
                // 刷新任务列表
                taskList.clear();
                taskList.addAll(taskDB.getAllTasks());
                ((ArrayAdapter<Task>) listViewTasks.getAdapter()).notifyDataSetChanged();
            }
        }
    }
    // 刷新任务列表
    private void refreshTaskList() {
        // 清空任务列表
        taskList.clear();
        // 重新从数据库中获取任务列表
        taskList.addAll(taskDB.getAllTasks());
        // 通知适配器数据集已改变，需要刷新 ListView
        ((ArrayAdapter<Task>) listViewTasks.getAdapter()).notifyDataSetChanged();
    }

    //广播接收器

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter("com.example.week4.TASK_ADDED");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            registerReceiver(taskAddedReceiver, filter);
        } catch (Exception e) {
            Log.e("MainActivity", "Error registering receiver: " + e.getMessage());
        }
    }


    // 广播接收器2
    private BroadcastReceiver taskAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 收到任务添加广播后，更新任务列表
            updateTaskList();
        }
    };

    private void updateTaskList() {
        // 初始化数据库操作类
        TaskManageDB taskDB = new TaskManageDB(this);

        // 获取最新的任务列表
        List<Task> taskList = taskDB.getAllTasks();

        // 设置任务列表适配器
        ArrayAdapter<Task> taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        listViewTasks.setAdapter(taskAdapter);
    }

}