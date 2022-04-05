package com.shaneroach.taskmaster.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.shaneroach.taskmaster.R;

import java.util.ArrayList;

public class UserSettingsActivity extends AppCompatActivity {

    private static final String TAG = "";
    public static final String USER_TEAM_NAME_TAG = "";
    SharedPreferences preferences;
    public static final String USER_USERNAME_TAG = "userUsername";
    Spinner teamSpinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpTeamSpinner();

        String userUsername = preferences.getString(USER_USERNAME_TAG,"");
        if (!userUsername.isEmpty()) {
            EditText userUsernameEditText = findViewById(R.id.editTextUserSettingsUsername);
            userUsernameEditText.setText(userUsername);
        }


        LinearLayout saveButton = findViewById(R.id.buttonUserSettingsSaveUsername);

        saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            EditText userUsernameEditText = findViewById(R.id.editTextUserSettingsUsername);
            String userUsernameString = userUsernameEditText.getText().toString();
            String userTeamNameString = teamSpinner.getSelectedItem().toString();
            preferencesEditor.putString(USER_USERNAME_TAG, userUsernameString);
            preferencesEditor.putString(USER_TEAM_NAME_TAG, userTeamNameString);
            preferencesEditor.apply();
            Snackbar.make(findViewById(R.id.userSettingsActivity), "User Settings Saved!", Snackbar.LENGTH_SHORT).show();
        });


    }

    private void setUpTeamSpinner() {

        teamSpinner = findViewById(R.id.spinnerUserSettingsTeam);

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
                    runOnUiThread(() -> teamSpinner.setAdapter(new ArrayAdapter<>(
                            this,
                            android.R.layout.preference_category,
                            teamNames
                    )));
                },
                failure -> Log.i(TAG, "Failed to add team names!")
        );

    }
}