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

        //Task task = (Task) getIntent().getParcelableExtra("task");

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_task_edit, null);
        builder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.editText_title);
        final EditText editTextDescription = dialogView.findViewById(R.id.editText_description);
        final EditText editTextDueDate = dialogView.findViewById(R.id.editText_dueDate);

        // 将当前任务信息显示在编辑界面上
        editTextTitle.setText(task.getTitle());
        editTextDescription.setText(task.getDescription());
        editTextDueDate.setText(task.getDueDate());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户编辑后的任务信息
                String newTitle = editTextTitle.getText().toString();
                String newDescription = editTextDescription.getText().toString();
                String newDueDate = editTextDueDate.getText().toString();

                // 更新任务信息并保存到数据库
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setDueDate(newDueDate);
                taskDB.updateTask(task);

                // 更新任务详情页面的显示
                textViewTitle.setText(task.getTitle());
                textViewDescription.setText(task.getDescription());
                textViewDueDate.setText(task.getDueDate());
            }
        });

    }



}
