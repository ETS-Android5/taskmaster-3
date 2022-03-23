package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import com.shaneroach.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Buttons
        Button addTaskButton = findViewById(R.id.buttonAddTaskTaskActivity);
        //Event Listener
        addTaskButton.setOnClickListener(view -> ((TextView)findViewById(R.id.textViewAddTaskSubmit)).setText(R.string.Submitted));

    }
}