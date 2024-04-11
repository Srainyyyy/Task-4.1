package com.example.week4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskManageDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "tasks";
    private static final String TABLE_NAME = "tasks"; // 修改此处为正确的表名

    public TaskManageDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建任务表
        String sqlDB = "CREATE TABLE " + TABLE_NAME + " (id TEXT PRIMARY KEY,title TEXT, description TEXT, dueDate TEXT)";
        db.execSQL(sqlDB);
    }

    public Task getTask(String id) {
        SQLiteDatabase sql_DB = this.getReadableDatabase();
        Cursor query = sql_DB.query(TABLE_NAME, new String[]{"id", "title", "description", "dueDate"},
                "id=?", new String[]{id}, null, null, null, null);
        Task task = null;
        if (query != null && query.moveToFirst()) {
            task = new Task(query.getString(0), query.getString(1), query.getString(2), query.getString(3));
        }
        if (query != null) {
            query.close();
        }
        sql_DB.close();
        return task;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级操作
    }

    // 添加任务到数据库
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String taskId = generateTaskId();
        values.put("id", taskId);
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // 获取所有任务列表
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return taskList;
    }

    // 更新任务
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("description", task.getDescription());
        values.put("dueDate", task.getDueDate());
        int result = db.update(TABLE_NAME, values, "id=?", new String[]{task.getId()});
        db.close();
        return result;
    }

    // 删除任务
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{task.getId()});
        db.close();
    }

    private String generateTaskId() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().substring(0, 8);
        String generatedId = timestamp + random;
        Log.d("TaskManageDB", "Generated Task ID: " + generatedId);
        return generatedId;
    }

}
