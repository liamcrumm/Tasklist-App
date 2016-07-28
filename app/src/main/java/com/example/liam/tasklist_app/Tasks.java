package com.example.liam.tasklist_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class Tasks extends AppCompatActivity {

    public boolean running = true;
    public ArrayList<Task> currentTasks = new ArrayList<Task>();
    public boolean yesDate = false;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(running) {
                    int i = 0;
                    listItems = new ArrayList<String>();
                    Collections.sort(currentTasks);
                    while (i < currentTasks.size()) {
                        long difference = currentTasks.get(i).due.getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
                        if (difference<=30000) {
                            currentTasks.remove(i);
                            continue;
                        }
                        long diffMinutes = difference / (60 * 1000) % 60;
                        long diffHours = difference / (60 * 60 * 1000) % 24;
                        long diffDays = difference / (24 * 60 * 60 * 1000);
                        listItems.add(currentTasks.get(i).todo + " " + "due in " + Long.toString(diffDays) + " days, " + Long.toString(diffHours) + " hours, " + Long.toString(diffMinutes) + " minutes");
                        i++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    public void addTask(View view) {
        currentTasks.add(0, new Task());
        running = false;
        setContentView(R.layout.create_task);
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
        currentTasks.get(0).due = new GregorianCalendar(temp.get(temp.YEAR),temp.get(temp.MONTH),temp.get(temp.DAY_OF_MONTH),
                ((TimePicker)findViewById(R.id.time)).getHour(),
                ((TimePicker)findViewById(R.id.time)).getMinute());
        setContentView(R.layout.activity_tasks);
        updateTasks();
    }
    public void updateTasks() {
        listItems = new ArrayList<String>();
        int i = 0;
        Collections.sort(currentTasks);
        while (i < currentTasks.size()) {
            long difference = currentTasks.get(i).due.getTimeInMillis() - GregorianCalendar.getInstance().getTimeInMillis();
            if (difference<=30000) {
                currentTasks.remove(i);
                continue;
            }
            long diffMinutes = difference / (60 * 1000) % 60;
            long diffHours = difference / (60 * 60 * 1000) % 24;
            long diffDays = difference / (24 * 60 * 60 * 1000);
            listItems.add(currentTasks.get(i).todo + " " + "due in " + Long.toString(diffDays) + " days, " + Long.toString(diffHours) + " hours, " + Long.toString(diffMinutes) + " minutes");
            i++;
        }
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
        adapter.notifyDataSetChanged();
        running = true;
    }
}

class Task implements Comparable {
    public String todo = "";
    public GregorianCalendar due;
    public int importance = 0; // 0 is not important, 1 somewhat, 2 very important

    public Task(String todo, GregorianCalendar due, int importance) {
        this.todo = todo;
        this.due = due;
        this.importance = importance;
    }

    public Task() {
        this.todo = "";
        this.due = null;
        this.importance = -1;
    }

    @Override
    public int compareTo(Object task) {
        Task t = (Task)task;
        if(t.importance > importance) {
            return 1;
        } else if(importance > t.importance) {
            return -1;
        } else {
            if(t.due.getTimeInMillis() < due.getTimeInMillis()) {
                return 1;
            } else if (t.due.getTimeInMillis() < due.getTimeInMillis()) {
                return -1;
            } else {
                return todo.compareTo(t.todo);
            }
        }
    }

    public String toString() {
        return "The task is to " + todo + " with an importance level of " + importance
                + " before " + due.getTime().toString();
    }
}
