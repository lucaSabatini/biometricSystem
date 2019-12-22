package com.luca.sabatini.appello.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.utils.RestConstants;

import java.util.Objects;


public class RicercaAppelli extends AppCompatActivity {

    private final String TAG = "RicercaAppelli";
    private RequestQueue queue;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        setContentView(R.layout.activity_ricerca_appelli);
        context = this;
        String beaconId = ricercaBeacon();

        if(!(ricercaBeacon().equals(""))){
            Log.d(TAG, "onCreate: " + ricercaBeacon());
            checkAttendanceSessionBackend(beaconId);

        }

    }

    private String ricercaBeacon(){
        //TODO: implementare parte beacons
        return "beaconnuovo";
    }

    private void checkAttendanceSessionBackend(String beaconId){
        //Log.d(TAG, "closeAttendanceSessionBackend: " + sp.readSessionId());
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.checkSessionUrl(beaconId),
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
            if(response.equals("")){
                Toast.makeText(context, "nessun appello trovato", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            CheckSessionResponse corso = new Gson().fromJson(response, CheckSessionResponse.class);
            Log.d(TAG, "onResponse: " + corso);
            //sp.writeSessionId(0L);
            //startActivity(new Intent(context, ProfessorMain.class));
            //finish();

        }
    };
}
