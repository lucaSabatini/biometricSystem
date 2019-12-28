package com.luca.sabatini.appello;

import android.app.Application;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import io.realm.Realm;

public class Appello extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //RealmConfiguration config = new RealmConfiguration.Builder().build();
        sFaceServiceClient = new FaceServiceRestClient("https://appello.cognitiveservices.azure.com/face/v1.0/", "f89ae8d239014d658ae7382e3d7450e9");
    }

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;
}
