package com.example.liam.tasklist_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class Tasks extends AppCompatActivity {

    public ArrayList<Tasks> currentTasks = new ArrayList<Tasks>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
    }

    public void addTask(View view) {

    }
}

class Task {
    private String todo = "";
    private Date due;
    private int importance = 0; // 0 is not important, 1 somewhat, 2 very important
    public Task(String todo,Date due, int importance) {
        this.todo = todo;
        this.due = due;
        this.importance = importance;
    }
}