package com.example.luca.biometricsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luca.biometricsystem.list.ListaCorsi;
import com.example.luca.biometricsystem.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "RemoveAlert";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
