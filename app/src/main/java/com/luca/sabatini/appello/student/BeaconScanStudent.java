package com.luca.sabatini.appello.student;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.luca.sabatini.appello.BeaconScan;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.CheckSessionResponse;
import com.luca.sabatini.appello.utils.RestConstants;
import java.util.Objects;


public class BeaconScanStudent extends BeaconScan {
    private final String TAG = "BeaconScanStudent";
    public RequestQueue queue;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));

        //TODO:commentare qui
        onBeaconIdReceived("beacondefault");
    }

    @Override
    public void onBeaconIdReceived(String beaconId) {
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
            ConfirmCourseAlert confirmCourseAlert = new ConfirmCourseAlert(corso.getNomeCorso());
            confirmCourseAlert.show(getSupportFragmentManager(), "confirm");
        }
    };

}
