package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shaneroach.taskmaster.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        Button buttonAllTask = findViewById(R.id.buttonAllTask);

        buttonAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTaskActivityIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
                startActivity(goToAllTaskActivityIntent);
            }
        });

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTaskActivityIntent = new Intent(HomeActivity.this, AddTaskActivity.class);
                startActivity(goToAddTaskActivityIntent);
            }

        });




    }
}