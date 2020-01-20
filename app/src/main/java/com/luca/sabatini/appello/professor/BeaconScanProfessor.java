package com.luca.sabatini.appello.professor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import java.util.Objects;


public class BeaconScanProfessor extends BeaconScan {
    private final String TAG = "BeaconScanStudent";
    public RequestQueue queue;
    private Context context;
    private SharedPrefManager sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        sp = new SharedPrefManager(this);

        //TODO:commentare qui
        //onBeaconIdReceived("beacondefault");
    }

    @Override
    public void onBeaconIdReceived(String beaconId) {
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.createSessionUrl(sp.readFirebaseId(), beaconId, sp.readCorsoId(), sp.readRegistrationId()),
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
            Long id = new Gson().fromJson(response, Long.class);
            sp.writeSessionId(id);
            startActivity(new Intent(context, Session.class));
            finish();
        }
    };

}
