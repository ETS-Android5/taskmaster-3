package com.shaneroach.taskmaster.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.shaneroach.taskmaster.R;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "TASK";
    Spinner taskStateSpinner = null;
    Spinner teamSpinner = null;

    CompletableFuture<List<Team>> teamsFuture = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        teamsFuture = new CompletableFuture<>();

        setUpSpinners();
        setUpSaveButtons();


    }

    private void setUpSaveButtons() {
        Button addTaskButton = (Button) findViewById(R.id.buttonAddTaskTaskActivity);
        addTaskButton.setOnClickListener(v -> {

            String title = ((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString();
            String description = ((EditText)findViewById(R.id.editTextTaskDescription)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());
            String selectedTeamString = teamSpinner.getSelectedItem().toString();


            List<Team> teams = null;
            try
            {
                teams = teamsFuture.get();
            }
            catch (InterruptedException ie)
            {
                Log.e(TAG, "InterruptedException while getting teams.");
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException ee)
            {
                Log.e(TAG, "ExecutionException while getting teams.");
            }

            assert teams != null;
            Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

            Task newTask = Task.builder()
                    .title(title)
                    .description(description)
                    .dateCreated(new Temporal.DateTime(currentDateString))
                    .taskStatusEnum((TaskStatusEnum) taskStateSpinner.getSelectedItem())
                    .teamName(selectedTeam)
                    .build();


            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    successResponse -> Log.i(TAG, "Made a Task successfully!"),
                    failureResponse -> Log.i(TAG, "failed with this response: "+ failureResponse)
            );
            Snackbar.make(findViewById(R.id.addTaskActivity), "Task saved!", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void setUpSpinners() {

        teamSpinner = (Spinner) findViewById(R.id.editAddTaskStateSpinnerTeam);
        taskStateSpinner = (Spinner) findViewById(R.id.editAddTaskStateSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read Teams Successfully!");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData()){
                        teamNames.add(team.getName());
                        teams.add(team);
                    }
                    teamsFuture.complete(teams);

                    runOnUiThread(() -> teamSpinner.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.preference_category,
                            teamNames
                    )));
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Failed to add team names!");
                }
        );

        taskStateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.preference_category, TaskStatusEnum.values()));
    }
}