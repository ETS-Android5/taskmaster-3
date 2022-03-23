package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.shaneroach.taskmaster.R;

public class ViewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        Intent callingIntent = getIntent();
        String taskTitleString = null;

        if (callingIntent != null){
            taskTitleString = callingIntent.getStringExtra(HomeActivity.TASK_TITLE_TAG);
        }

        TextView taskViewTitleView = (TextView) findViewById(R.id.textTaskViewTitle);


        if (taskTitleString != null){
            taskViewTitleView.setText(taskTitleString);
        } else {
            taskViewTitleView.setText(R.string.no_task_name);
        }

    }
}