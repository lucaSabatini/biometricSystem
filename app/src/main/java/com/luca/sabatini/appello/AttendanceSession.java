package com.luca.sabatini.appello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AttendanceSession extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_session);
        //AttendanceService.shouldContinue = true;
        //startService(new Intent(this, FirebaseService.class));
    }

    public void closeAttendanceSession(View v){
        AttendanceService.shouldContinue = false;
        //stopService(new Intent(this, AttendanceService.class));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
