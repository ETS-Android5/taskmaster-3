package com.shaneroach.taskmaster;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class TaskMasterAmplifyApplication extends Application {

    public static final String TAG = "taskMasterApplication";


    @Override
    public void onCreate(){
        super.onCreate();
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae){
            Log.e(TAG, "Error with Amplify" + ae.getMessage(), ae);
        }

    }
}
