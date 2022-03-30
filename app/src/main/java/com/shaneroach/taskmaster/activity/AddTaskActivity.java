package com.shaneroach.taskmaster.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStatusEnum;
import com.shaneroach.taskmaster.R;



import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        Spinner taskStateSpinner = (Spinner) findViewById(R.id.editAddTaskStateSpinner);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.preference_category, TaskStatusEnum.values()));

        Button addTaskButton = (Button) findViewById(R.id.buttonAddTaskTaskActivity);
        addTaskButton.setOnClickListener(v -> {

            String title = ((EditText)findViewById(R.id.editTextTaskTitle)).getText().toString();
            String description = ((EditText)findViewById(R.id.editTextTaskDescription)).getText().toString();
            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());


            Task newTask = Task.builder()
                    .title(title)
                    .description(description)
                    .dateCreated(new Temporal.DateTime(currentDateString))
                    .taskStatusEnum((TaskStatusEnum) taskStateSpinner.getSelectedItem())
                    .build();


            Amplify.API.mutate(
                ModelMutation.create(newTask),
                successResponse -> Log.i(TAG, "Made a Task successfully!"),
                failureResponse -> Log.i(TAG, "failed with this response: "+ failureResponse)
            );

        });
    }
}