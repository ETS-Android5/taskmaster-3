package com.shaneroach.taskmaster.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.shaneroach.taskmaster.R;
import com.shaneroach.taskmaster.adapter.TaskListRecycleReviewAdapter;
import com.shaneroach.taskmaster.enums.TaskStatusEnum;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "TASK";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";
    public final String TAG = "MESSAGE";
    SharedPreferences preferences;
    TaskListRecycleReviewAdapter adapter;
    List<Task> tasks = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();


//        Team team1 =
//                Team.builder()
//                .name("Robots")
//                .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                successResponse -> Log.i(TAG, "Made a new Team of Robots!"),
//                failureResponse -> Log.i(TAG, "Failed to make team of Robots.")
//        );
//
//
//        Team team2 =
//                Team.builder()
//                        .name("Humans")
//                        .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                successResponse -> Log.i(TAG, "Made a new Team of Humans!"),
//                failureResponse -> Log.i(TAG, "Failed to make team Humans.")
//        );
//
//
//        Team team3 =
//                Team.builder()
//                        .name("Elves")
//                        .build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                successResponse -> Log.i(TAG, "Made a new Team of Elves!"),
//                failureResponse -> Log.i(TAG, "Failed to make team Elves.")
//        );

        setUpAddTaskButton();
        setUpAllTasksButton();
        setUpUserSettingsButton();
        setUpTaskListRecycleView();
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume(){
        super.onResume();
        String userUsername = preferences.getString(UserSettingsActivity.USER_USERNAME_TAG,"No Username");
        String userTeamName = preferences.getString(UserSettingsActivity.USER_TEAM_NAME_TAG, "No Team");
        ((TextView) findViewById(R.id.textHomeUsernameView)).setText(getString(R.string.username_with_input, userUsername));
        ((TextView) findViewById(R.id.textHomeTeamNameView)).setText(getString(R.string.team_name_with_input, userTeamName));

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Updated Tasks Successfully!");
                    tasks.clear();
                    for(Task databaseTask : success.getData()){
                        if (userTeamName.equals("No Team")){
                           tasks.add(databaseTask);
                        }
                        else if (databaseTask.getTeamName().getName().equals(userTeamName)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                    });
                },

                failure -> Log.i(TAG, "failed with this response: ")
        );
    }

    private void setUpUserSettingsButton(){
        ImageView userSettingsImageView = (ImageView) findViewById(R.id.userSettingsImage);

        userSettingsImageView.setOnClickListener(view -> {
            Intent goToUserSettingsIntent = new Intent(HomeActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettingsIntent);
        });
    }



    private void setUpAllTasksButton(){
        LinearLayout buttonAllTask = findViewById(R.id.buttonAllTask);
        buttonAllTask.setOnClickListener(view -> {
            Intent goToAllTaskActivityIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
            startActivity(goToAllTaskActivityIntent);
        });

    }

    private void setUpAddTaskButton(){
        LinearLayout buttonAddTask = findViewById(R.id.buttonAddTask);
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