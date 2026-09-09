package com.malaika.todoapp;

public class TaskItem {
    public long id;
    public String title;
    public boolean isCompleted;

    public TaskItem(long id, String title, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
    }
}