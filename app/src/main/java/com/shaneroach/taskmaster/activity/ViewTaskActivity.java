package com.shaneroach.taskmaster.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.shaneroach.taskmaster.R;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ViewTaskActivity extends AppCompatActivity {

    private static final String TAG = "Edit Task";

    private Task taskToEdit = null;
    private CompletableFuture<Task> taskCompletableFuture = null;
    private CompletableFuture<List<Team>> teamsFuture = null;
    private EditText taskTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        taskCompletableFuture = new CompletableFuture<>();
        teamsFuture = new CompletableFuture<>();


        setUpUIElementsOG();
        setUpUIElementsEdit();
        setUpEditTaskButton();
    }

    private void setUpEditTaskButton() {
        Button buttonEditTask = findViewById(R.id.buttonEditTaskViewTask);
        buttonEditTask.setOnClickListener(view -> {
            Intent goToEditTaskActivityIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
            goToEditTaskActivityIntent.putExtra(HomeActivity.TASK_ID_TAG, taskToEdit.getId());
            startActivity(goToEditTaskActivityIntent);
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

//        EditText taskTitleEditText = ((EditText) findViewById(R.id.editTaskTitleEditText));
//        taskTitleEditText.setText(taskToEdit.getTitle());


        String imageS3Key = taskToEdit.getTaskImageS3Key();
        if (imageS3Key != null && !imageS3Key.isEmpty()){
            Amplify.Storage.downloadFile(
                    imageS3Key,
                    new File(getApplication().getFilesDir(),imageS3Key),
                    success -> {
                        ImageView viewTaskImageUpload = findViewById(R.id.imageViewViewTaskUploadFile);
                        viewTaskImageUpload.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                    },
                    failure -> {
                        Log.e(TAG, "Unable to get image from s3 for: " + imageS3Key);
                    }
            );
        }

    }


    private void setUpUIElementsOG(){
        Intent callingIntent = getIntent();
        String taskTitleString = null;
        String taskBodyString = null;
        String taskStateEnum = null;

        if (callingIntent != null){
            taskTitleString = callingIntent.getStringExtra(HomeActivity.TASK_TITLE_TAG);
            taskBodyString = callingIntent.getStringExtra(HomeActivity.TASK_BODY_TAG);
            taskStateEnum = callingIntent.getStringExtra(HomeActivity.TASK_STATE_TAG);
        }

        TextView taskViewTitleView = (TextView) findViewById(R.id.textTaskViewTitle);
        TextView taskViewBodyView = (TextView) findViewById(R.id.textTaskViewBody);
        TextView taskViewStateView = (TextView) findViewById(R.id.textTaskViewStatus);


        if (taskTitleString != null){
            taskViewTitleView.setText(taskTitleString);
        } else {
            taskViewTitleView.setText(R.string.no_task_name);
        }

        if (taskBodyString != null){
            taskViewBodyView.setText(taskBodyString);
        } else {
            taskViewBodyView.setText(R.string.no_task_name);
        }

        if (taskStateEnum != null){
            taskViewStateView.setText(taskStateEnum);
        } else {
            taskViewStateView.setText(R.string.no_task_state);
        }
    }
}