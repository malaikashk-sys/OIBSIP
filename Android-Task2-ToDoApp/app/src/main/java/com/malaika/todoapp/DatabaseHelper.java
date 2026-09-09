package com.malaika.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_TASKS = "tasks";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TASKS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK_NAME TEXT, IS_COMPLETED INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // User Methods
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Task Methods
    public void addTask(String taskTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TASK_NAME", taskTitle);
        contentValues.put("IS_COMPLETED", 0);
        db.insert(TABLE_TASKS, null, contentValues);
    }

    public ArrayList<TaskItem> getAllTasks() {
        ArrayList<TaskItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                boolean isCompleted = cursor.getInt(2) == 1;
                list.add(new TaskItem(id, title, isCompleted));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateTaskStatus(long id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_COMPLETED", isCompleted ? 1 : 0);
        db.update(TABLE_TASKS, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, "ID=?", new String[]{String.valueOf(id)});
    }
}