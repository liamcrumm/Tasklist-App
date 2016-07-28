package com.example.liam.tasklist_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Tasks extends AppCompatActivity {

    public ArrayList<Task> currentTasks = new ArrayList<Task>();
    public boolean yesDate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
    }

    public void addTask(View view) {
        currentTasks.add(0,new Task());
        setContentView(R.layout.create_task);
    }
    public void setImportance(View view) {
        currentTasks.get(0).importance = 2131492944-view.getId();
    }
    public void chooseImportance(View view) {
        currentTasks.get(0).todo = ((EditText)findViewById(R.id.task_description)).getText().toString();
        setContentView(R.layout.choose_importance);
    }
    public void pickDay(View view) {
        //this is a bit messy, but it just sets a relative importance based on button id
        currentTasks.get(0).importance = ((RadioGroup)findViewById(R.id.choice))
                .getCheckedRadioButtonId() == R.id.vImp?2:((RadioGroup)findViewById(R.id.choice))
                .getCheckedRadioButtonId() == R.id.sImp?1:0;
        if(((CheckBox)(findViewById(R.id.due_date))).isChecked()) {
            setContentView(R.layout.choose_due_date);
        } else {
            setContentView(R.layout.activity_tasks);
        }
    }
    public void pickTime(View view) {
        currentTasks.get(0).due = new GregorianCalendar(((DatePicker)findViewById(R.id.calendar)).getYear(),
                ((DatePicker)findViewById(R.id.calendar)).getMonth(),
                ((DatePicker)findViewById(R.id.calendar)).getDayOfMonth());
        setContentView(R.layout.choose_due_time);
    }
    public void registerTask(View view) {
        GregorianCalendar temp = currentTasks.get(0).due;
        currentTasks.get(0).due = new GregorianCalendar(temp.YEAR,temp.MONTH,temp.DAY_OF_MONTH,
                ((TimePicker)findViewById(R.id.time)).getHour(),
                ((TimePicker)findViewById(R.id.time)).getMinute());
        System.out.println(currentTasks.get(0).toString());
        setContentView(R.layout.activity_tasks);
    }
}

class Task {
    public String todo = "";
    public GregorianCalendar due;
    public int importance = 0; // 0 is not important, 1 somewhat, 2 very important
    public Task(String todo,GregorianCalendar due, int importance) {
        this.todo = todo;
        this.due = due;
        this.importance = importance;
    }
    public Task() {
        this.todo = "";
        this.due = null;
        this.importance = -1;
    }
    public String toString() {
        return "The task is to " + todo + " with an importance level of " + importance
                + " before " + due.getTime().toString();
    }
}