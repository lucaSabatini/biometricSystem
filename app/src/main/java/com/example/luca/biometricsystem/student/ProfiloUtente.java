package com.example.luca.biometricsystem.student;

import android.content.Intent;
import android.os.Bundle;

import com.example.luca.biometricsystem.entities.Persona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.luca.biometricsystem.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.luca.biometricsystem.login.LoginIntroFragment.EXTRA_ACTION;

public class ProfiloUtente extends AppCompatActivity {

    private TextView studentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_utente);
        studentData = findViewById(R.id.student_data_textView);
        Persona persona = new Persona(FirebaseAuth.getInstance().getCurrentUser().getEmail().trim());
        String temp = persona.getLastName()+"\n\n"+persona.getStudentId();
        studentData.setText(temp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void ricercaAppelliFabOnClick(View view){
        startActivity(new Intent(this, RicercaAppelli.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profilo_utente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.modifica_foto_profilo_item) {
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(EXTRA_ACTION, "changePhoto");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
