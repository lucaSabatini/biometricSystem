package com.luca.sabatini.appello;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.luca.sabatini.appello.login.LoginActivity;
import com.luca.sabatini.appello.student.UserProfile;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "RemoveAlert";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ProfessorProfile.class);
        startActivity(intent);
        finish();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.d(TAG, " notificationfirebase onSuccess: " + instanceIdResult.getToken());
            }
        });
    }
}
