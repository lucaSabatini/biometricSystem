package com.luca.sabatini.appello.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.CheckSessionResponse;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import java.util.Objects;

import static com.luca.sabatini.appello.student.CameraActivity.EXTRA_ACTION;


public class ConfermaPresenza extends AppCompatActivity {

    private ImageView faccina;
    private TextView scritta;
    private Button conferma;
    private Button riprova;
    SharedPrefManager sp;
    RequestQueue queue;
    Context context;
    private static final String TAG = "ConfermaPresenza";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferma_presenza);
        faccina = findViewById(R.id.faccina);
        scritta = findViewById(R.id.scritta);
        conferma = findViewById(R.id.conferma);
        riprova = findViewById(R.id.riprova);
        sp = new SharedPrefManager(this);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        context = this;
        boolean action = getIntent().getBooleanExtra("happy", false);
        animation(action);
    }


    public void animation(boolean happy){
        if(happy){
            faccina.setImageResource(R.drawable.ic_sentiment_satisfied_green_300dp);
            scritta.setText("Presenza confermata!");
            conferma.setVisibility(View.VISIBLE);
            riprova.setVisibility(View.GONE);
            //TODO: manda attendance al server
            sendAttendanceToServer();

        }else{
            faccina.setImageResource(R.drawable.ic_sentiment_dissatisfied_red_300dp);
            scritta.setText("Presenza non confermata!");
            conferma.setVisibility(View.GONE);
            riprova.setVisibility(View.VISIBLE);
        }
    }

    public void apriCameraActivityRiprovaOnClick(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(EXTRA_ACTION, "verification");
        startActivity(intent);
    }

    public void apriProfiloUtenteOkOnClick(View view){
        //aggiungere activity tra login e ricercaAppello
        startActivity(new Intent(this, UserProfile.class));
        finish();
    }


    public void sendAttendanceToServer() {
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.createAttendanceUrl(sp.readMatricola(),sp.readSurname(), sp.readRegistrationId()),
                callbackGet,
                callbackError);
        queue.add(postRequest);
    }


    private Response.ErrorListener callbackError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.networkResponse != null) {
                Log.e(TAG, "onErrorResponse: callbackError: " + new String(error.networkResponse.data));
                Log.e(TAG, "onErrorResponse: callbackError: " + error.networkResponse.statusCode);
            } else{
                Log.e(TAG, "onErrorResponse: callbackError: " + error.getMessage());
            }
        }
    };

    private Response.Listener<String> callbackGet = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "onCreate: " + response);
        }
    };
}
