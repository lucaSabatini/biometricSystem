package com.luca.sabatini.appello;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class AttendanceService extends IntentService{
    private final String TAG = "AttendanceService";
    private Timer timer = new Timer();
    public static volatile boolean shouldContinue = true;

    public AttendanceService() {
        super("HelloIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: 2 secondi");;   //Your code here
                if (shouldContinue == false) {
                    timer.cancel();
                    return;
                }
            }

        }, 0, 2000);//5 Minutes
    }
}


