package com.luca.sabatini.appello.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luca.sabatini.appello.R;

import static com.luca.sabatini.appello.login.LoginIntroFragment.EXTRA_ACTION;


public class ConfermaPresenza extends AppCompatActivity {

    private ImageView faccina;
    private TextView scritta;
    private Button conferma;
    private Button riprova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferma_presenza);
        faccina = findViewById(R.id.faccina);
        scritta = findViewById(R.id.scritta);
        conferma = findViewById(R.id.conferma);
        riprova = findViewById(R.id.riprova);
    }


    public void animation(String action){
        if(action.equals("confermato")){
            faccina.setImageResource(R.drawable.ic_sentiment_satisfied_green_300dp);
            scritta.setText("Presenza confermata!");
            conferma.setVisibility(View.VISIBLE);
            riprova.setVisibility(View.GONE);
        }else{
            faccina.setImageResource(R.drawable.ic_sentiment_dissatisfied_red_300dp);
            scritta.setText("Presenza non confermata!");
            conferma.setVisibility(View.GONE);
            riprova.setVisibility(View.VISIBLE);
        }
    }

    public void apriCameraActivityRiprovaOnClick(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("login", EXTRA_ACTION);
        startActivity(intent);
    }

    public void apriProfiloUtenteOkOnClick(View view){
        //aggiungere activity tra login e ricercaAppello
        startActivity(new Intent(this, ProfiloUtente.class));
    }
}
