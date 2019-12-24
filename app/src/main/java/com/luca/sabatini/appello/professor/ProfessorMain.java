package com.luca.sabatini.appello.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.StatisticsActivity;
import com.luca.sabatini.appello.login.LoginActivity;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import java.util.Objects;

public class ProfessorMain extends AppCompatActivity {

    private static final String TAG = "ProfessorMain";
    private TextView nomeCorsoTextView;
    private TextView annoCorsoTextView;

    SharedPrefManager sp;
    private RequestQueue queue;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appello_or_statistica);
        Intent intent = getIntent();
        context = this;

        //String nomeCorso = intent.getStringExtra(ListaCorsi.EXTRA_TEXT);
        //Long annoCorso = intent.getLongExtra(ListaCorsi.EXTRA_DATE, -1);

        sp = new SharedPrefManager(this);
        nomeCorsoTextView = findViewById(R.id.nome_corso);
        annoCorsoTextView = findViewById(R.id.anno_corso);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        nomeCorsoTextView.setText(sp.readNomeCorso());
        annoCorsoTextView.setText(""+sp.readAnnoCorso());

    }

    public void openAttendanceSession(View v){
        openAttendanceSessionBackend();
    }

    public void logout(View v){
        SharedPrefManager sp = new SharedPrefManager(this);
        FirebaseAuth.getInstance().signOut();
        sp.resetSharedPref();
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void openStatisticsActivtiy(View view){
        startActivity(new Intent(this, StatisticsActivity.class));
    }

    private void openAttendanceSessionBackend(){
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.createSessionUrl(sp.readFirebaseId(), "beaconnuovo", sp.readCorsoId()),
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
            Toast.makeText(context, "Connessione a internet assente. Riprova più tardi", Toast.LENGTH_LONG).show();
        }

    };

    private Response.Listener<String> callbackGet = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Long id = new Gson().fromJson(response, Long.class);
            sp.writeSessionId(id);
            startActivity(new Intent(context, Session.class));
        }
    };
}
