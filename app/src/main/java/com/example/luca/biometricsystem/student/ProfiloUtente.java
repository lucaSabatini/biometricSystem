package com.example.luca.biometricsystem.student;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.luca.biometricsystem.R;

import static com.example.luca.biometricsystem.login.LoginIntroFragment.EXTRA_ACTION;

public class ProfiloUtente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_utente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void openCameraActivityFromProfiloUtente(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(EXTRA_ACTION, "changePhoto");
        startActivity(intent);
    }
}
