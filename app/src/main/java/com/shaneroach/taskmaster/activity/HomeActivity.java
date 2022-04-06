package com.shaneroach.taskmaster.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.shaneroach.taskmaster.R;
import com.shaneroach.taskmaster.adapter.TaskListRecycleReviewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "TASK";
    public static final String TASK_BODY_TAG = "BODY";
    public static final String TASK_STATE_TAG = "STATE";
    public static final String TASK_ID_TAG = "Task ID Tag";
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

//        String emptyFileName = "emptyTestFileName";
//        File emptyFile = new File(getApplicationContext().getFilesDir(), "emptyTestFileName");
//
//        try {
//            BufferedWriter emptyFileBufferedWriter = new BufferedWriter(new FileWriter(emptyFile));
//            emptyFileBufferedWriter.append("Some test text");
//            emptyFileBufferedWriter.close();
//        } catch (IOException ioe) {
//            Log.e(TAG, "Could not write file locally with filename" + emptyFileName);
//        }
//
//        String emptyFileS3Key = "someFileOns3";
//
//        Amplify.Storage.uploadFile(
//                emptyFileS3Key,
//                emptyFile,
//                success -> {
//                    Log.i(TAG, "S3 upload succeeded: " + success.getKey());
//                },
//                failure -> {
//                    Log.i(TAG, ("S3 upload failed"));
//
//                });
//



        setUpAddTaskButton();
        setUpAllTasksButton();
        setUpLoginButton();
        setUpSignOutButton();
        setUpUserSettingsButton();
        setUpTaskListRecycleView();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        String userTeamName = preferences.getString(UserSettingsActivity.USER_TEAM_NAME_TAG, "No Team");
        ((TextView) findViewById(R.id.textHomeTeamNameView)).setText(getString(R.string.team_name_with_input, userTeamName));


        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username = "";
        if (authUser == null)
        {
            String userUsername = preferences.getString(UserSettingsActivity.USER_USERNAME_TAG, "No Username");
            ((TextView) findViewById(R.id.textHomeUsernameView)).setVisibility(View.INVISIBLE);
            LinearLayout loginButton = findViewById(R.id.buttonToLoginTask);
            loginButton.setVisibility(View.VISIBLE);
            LinearLayout logoutButton = findViewById(R.id.buttonToSignOut);
            logoutButton.setVisibility(View.INVISIBLE);
        }
        else  // authUser is not null
        {
            Log.i(TAG, "Username is: " + username);
            LinearLayout loginButton = findViewById(R.id.buttonToLoginTask);
            loginButton.setVisibility(View.INVISIBLE);
            LinearLayout logoutButton = findViewById(R.id.buttonToSignOut);
            logoutButton.setVisibility(View.VISIBLE);

            // Not strictly required for your lab, but useful for your project
            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: " + username);

                        for (AuthUserAttribute userAttribute : success)
                        {
                            if (userAttribute.getKey().getKeyString().equals("nickname"))
                            {
                                String userNickname = userAttribute.getValue();
                                runOnUiThread(() ->
                                        {
                                            ((TextView) findViewById(R.id.textHomeUsernameView)).setText(userNickname);
                                            ((TextView) findViewById(R.id.textHomeUsernameView)).setVisibility(View.VISIBLE);
                                        }
                                );
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: " + failure.toString());
                    }
            );
        }


        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Updated Tasks Successfully!");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {
                        if (userTeamName.equals("No Team")) {
                            tasks.add(databaseTask);
                        } else if (databaseTask.getTeamName().getName().equals(userTeamName)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                },

                failure -> Log.i(TAG, "failed with this response: ")
        );
    }

    private void setUpUserSettingsButton() {
        ImageView userSettingsImageView = (ImageView) findViewById(R.id.userSettingsImage);

        userSettingsImageView.setOnClickListener(view -> {
            Intent goToUserSettingsIntent = new Intent(HomeActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettingsIntent);
        });
    }


    private void setUpAllTasksButton() {
        LinearLayout buttonAllTask = findViewById(R.id.buttonAllTask);
        buttonAllTask.setOnClickListener(view -> {
            Intent goToAllTaskActivityIntent = new Intent(HomeActivity.this, AllTasksActivity.class);
            startActivity(goToAllTaskActivityIntent);
        });

    }

    private void setUpAddTaskButton() {
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

    private void setUpLoginButton() {
        LinearLayout buttonAddTask = findViewById(R.id.buttonToLoginTask);

        buttonAddTask.setOnClickListener(view -> {
            Intent goToAddTaskActivity = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(goToAddTaskActivity);
        });
    }

    private void setUpSignOutButton(){

        LinearLayout logoutButton = findViewById(R.id.buttonToSignOut);
        logoutButton.setOnClickListener(v ->
        {
            Amplify.Auth.signOut(
                    () ->
                    {
                        Log.i(TAG, "Logout succeeded!");
                        runOnUiThread(() ->
                                {
                                    ((TextView) findViewById(R.id.textHomeUsernameView)).setText("");
                                    ((TextView) findViewById(R.id.textHomeUsernameView)).setVisibility(View.INVISIBLE);

                                }
                        );
                        Intent goToLogInIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Logout failed: " + failure.toString());
                        runOnUiThread(() ->
                        {
                        });
                    }
            );
        });
    }
}