package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaneroach.taskmaster.R;
import com.shaneroach.taskmaster.adapter.TaskListRecycleReviewAdapter;
import com.shaneroach.taskmaster.database.TaskMasterDatabase;
import com.shaneroach.taskmaster.enums.TaskStatusEnum;
import com.shaneroach.taskmaster.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "TASK";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";
    public static final String USER_USERNAME_TAG = "userUsername";
    SharedPreferences preferences;
    TaskListRecycleReviewAdapter adapter;
    TaskMasterDatabase taskMasterDatabase;
    List<Task> tasks = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskMasterDatabase = Room.databaseBuilder(
                getApplicationContext(),
                TaskMasterDatabase.class,
                "shane_task_master")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        tasks = taskMasterDatabase.taskDao().findAll();

        //setUpDeleteButton();
        setUpAddTaskButton();
        setUpAllTasksButton();
        setUpUserSettingsButton();
        setUpTaskListRecycleView();
    }



    @Override
    protected void onResume(){
        super.onResume();
        String userUsername = preferences.getString(UserSettingsActivity.USER_USERNAME_TAG,"No Username");
        ((TextView) findViewById(R.id.textHomeUsernameView)).setText(getString(R.string.username_with_input, userUsername));
    }

    private void setUpUserSettingsButton(){
        ImageView userSettingsImageView = (ImageView) findViewById(R.id.userSettingsImage);

        userSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToUserSettingsIntent = new Intent(HomeActivity.this, UserSettingsActivity.class);
                startActivity(goToUserSettingsIntent);
            }
        });
    }

    private void setUpDeleteButton() {
        Button buttonDeleteTask = findViewById(R.id.buttonDeleteAllTasks);
        buttonDeleteTask.setOnClickListener(view -> {
            taskMasterDatabase.taskDao().deleteAll(tasks);
        });
    }

    private void setUpAllTasksButton(){
        Button buttonAllTask = findViewById(R.id.buttonAllTask);
        buttonAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTaskActivityIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
                startActivity(goToAllTaskActivityIntent);
            }
        });

    }

    private void setUpAddTaskButton(){
        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        buttonAddTask.setOnClickListener(view -> {
            Intent goToAddTaskActivity = new Intent(HomeActivity.this, AddTaskActivity.class);
            startActivity(goToAddTaskActivity);
        });
    }


    private void setUpTaskListRecycleView() {

        RecyclerView taskListRecycleReview = (RecyclerView) findViewById(R.id.homeTaskRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        taskListRecycleReview.setLayoutManager(layoutManager);

        adapter = new TaskListRecycleReviewAdapter(tasks, this);
        taskListRecycleReview.setAdapter(adapter);
    }
}