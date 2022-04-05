package com.shaneroach.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.shaneroach.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {


    public static final String TAG = "SignupActivity";
    public static final String SIGNUP_EMAIL_TAG = "Signup_Email_Tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LinearLayout signupSubmitButton = findViewById(R.id.buttonSignUpUserAccount);
        signupSubmitButton.setOnClickListener(v ->
        {
            String username = ((EditText) findViewById(R.id.editSignUpUserNameSignIn)).getText().toString();
            String password = ((EditText) findViewById(R.id.editSignUpPassWordSignIn)).getText().toString();

            Amplify.Auth.signUp(username,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .userAttribute(AuthUserAttributeKey.nickname(), "Shane")
                            .build(),
                    good ->
                    {
                        Log.i(TAG, "Signup succeeded: " + good.toString());
                        Intent goToLogInIntent = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
                        goToLogInIntent.putExtra(SIGNUP_EMAIL_TAG, username);
                        startActivity(goToLogInIntent);
                    },
                    bad ->
                    {
                        Log.i(TAG, "Signup failed with username: " + "shardbearershane@gmail.com" + " with this message: " + bad.toString());
                        runOnUiThread(() ->
                                {
                                    Toast.makeText(SignUpActivity.this, "Signup failed!", Toast.LENGTH_SHORT);
                                }
                        );
                    }
            );
        });





    }
}