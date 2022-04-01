package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "Edit Task";

    private Task taskToEdit = null;
    private CompletableFuture<Task> taskCompletableFuture = null;
    private CompletableFuture<List<Team>> teamsFuture = null;
    private EditText taskTitleEditText;
    Spinner taskStateSpinner = null;
    Spinner teamSpinner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamsFuture = new CompletableFuture<>();

        setUpUIElementsEdit();
        setUpSaveButton();
        setUpDeleteButton();
    }

    private void setUpSaveButton() {

        Button saveButton = findViewById(R.id.buttonSaveEditTask);
        saveButton.setOnClickListener(v ->{


            String title = ((EditText)findViewById(R.id.editTextEditTaskTitle)).getText().toString();
            String description = ((EditText)findViewById(R.id.editTextEditTaskDescription)).getText().toString();
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

            Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

            Task taskToSave = Task.builder()
                    .title(title)
                    .id(taskToEdit.getId())
                    .description(description)
                    .dateCreated(taskToEdit.getDateCreated())
                    .taskStatusEnum((TaskStatusEnum) taskStateSpinner.getSelectedItem())
                    .teamName(selectedTeam)
                    .build();


            Amplify.API.mutate(
                    ModelMutation.update(taskToSave),
                    successResponse -> {
                        Log.i(TAG, "Edited a Task successfully!");
                    },
                    failureResponse -> Log.i(TAG, "Failed to edit task with this response: "+ failureResponse)
            );

            Snackbar.make(findViewById(R.id.addTaskActivity), "Task Updated!", Snackbar.LENGTH_SHORT).show();
            Intent goToHomeActivity = new Intent(EditTaskActivity.this, HomeActivity.class);
            startActivity(goToHomeActivity);

        });

    }


    private void setUpDeleteButton(){
        Button deleteButton = findViewById(R.id.buttonEditTeaskDelete);
        deleteButton.setOnClickListener(v ->
        {
            Amplify.API.mutate(
                    ModelMutation.delete(taskToEdit),
                    successResponse ->
                    {
                        Log.i(TAG, "EditTaskActivity.onCreate(): deleted a task successfully");
                        Intent goToHomeActivity = new Intent(EditTaskActivity.this, HomeActivity.class);
                        startActivity(goToHomeActivity);
                    },  // success callback
                    failureResponse -> Log.i(TAG, "EditProductActivity.onCreate(): failed with this response: " + failureResponse)
            );
        });
    }

    private void setUpUIElementsEdit() {
        Intent callingIntent = getIntent();
        String taskId = null;

        if (callingIntent != null)
        {
            taskId = callingIntent.getStringExtra(HomeActivity.TASK_ID_TAG);
        }

        String taskId2 = taskId;

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success ->
                {
                    Log.i(TAG, "Read task successfully!");

                    for (Task databaseTask : success.getData())
                    {
                        if (databaseTask.getId().equals(taskId2))
                        {
                            taskCompletableFuture.complete(databaseTask);
                        }
                    }

                    runOnUiThread(() ->
                    {
                        // Update UI elements
                    });
                },
                failure -> Log.i(TAG, "Did not read tasks successfully!")
        );

        try
        {
            taskToEdit = taskCompletableFuture.get();
        }
        catch (InterruptedException ie)
        {
            Log.e(TAG, "InterruptedException while getting task");
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ee)
        {
            Log.e(TAG, "ExecutionException while getting product");
        }

        EditText taskTitleEditText = ((EditText) findViewById(R.id.editTextEditTaskTitle));
        EditText taskDescriptionEditText = ((EditText) findViewById(R.id.editTextEditTaskDescription));
        taskTitleEditText.setText(taskToEdit.getTitle());
        taskDescriptionEditText.setText(taskToEdit.getDescription());
        setUpSpinners();

    }


    private void setUpSpinners() {

        teamSpinner = (Spinner) findViewById(R.id.spinnerEditTaskTeam);
        taskStateSpinner = (Spinner) findViewById(R.id.spinnerEditTaskStatus);

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
        taskStateSpinner.setSelection(getSpinnerIndex(taskStateSpinner, taskToEdit.getTaskStatusEnum().toString()));

    }




    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck){
        for (int i = 0;i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)){
                return i;
            }
        }
        return 0;
    }

}