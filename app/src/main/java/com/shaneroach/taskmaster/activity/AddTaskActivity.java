package com.shaneroach.taskmaster.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.shaneroach.taskmaster.R;
import com.shaneroach.taskmaster.database.TaskMasterDatabase;
import com.shaneroach.taskmaster.enums.TaskStatusEnum;
import com.shaneroach.taskmaster.model.Task;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    TaskMasterDatabase taskMasterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);




        taskMasterDatabase = Room.databaseBuilder(
                getApplicationContext(),
                TaskMasterDatabase.class,
                "shane_task_master")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        Spinner taskStateSpinner = (Spinner) findViewById(R.id.editAddTaskStateSpinner);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TaskStatusEnum.values()));

        //Buttons
        Button addTaskButton = (Button) findViewById(R.id.buttonAddTaskTaskActivity);
        addTaskButton.setOnClickListener(v -> {
            Task newTask = new Task(
                    ((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString(),
                    ((EditText)findViewById(R.id.editTextTaskDescription)).getText().toString(),
                    TaskStatusEnum.fromString(taskStateSpinner.getSelectedItem().toString()),
                    new Date()
                    );

            taskMasterDatabase.taskDao().insertATask(newTask);
            //Snackbar.make(findViewById(R.id.userSettingsActivity), "task saved!", Snackbar.LENGTH_SHORT).show();
        });
    }
}