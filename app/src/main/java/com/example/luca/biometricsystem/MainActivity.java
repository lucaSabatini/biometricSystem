package com.example.luca.biometricsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button registrazione;
    private Button appello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registrazione = findViewById(R.id.registrazioneButton);
        appello = findViewById(R.id.appelloButton);
    }

    /*public void openRegistrazioneActivity(View view){
        Intent registrazioneActivity = new Intent(this, Registrazione.class);
        startActivity(registrazioneActivity);
    }*/

    public void openAppelloActivity(View view){
    }
}
