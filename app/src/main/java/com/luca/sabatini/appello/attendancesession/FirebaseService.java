package com.luca.sabatini.appello.attendancesession;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.luca.sabatini.appello.entities.Student;

import io.realm.Realm;

public class FirebaseService extends FirebaseMessagingService {
    private final String TAG = "FirebaseService";

    public FirebaseService() {
        super();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "notificationfirebase onNewToken: s");
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Student s = new Student();
        Realm mRealm = Realm.getDefaultInstance();
        s.matricola = 1234L;
        s.surname = remoteMessage.getData().get("studentId");
        s.studentId = remoteMessage.getData().get("studentId");

        mRealm.beginTransaction();
        mRealm.insertOrUpdate(s);
        mRealm.commitTransaction();
        mRealm.close();

        Log.d(TAG, "notificationfirebase onMessageReceived: " + remoteMessage.getData());

    }
}
