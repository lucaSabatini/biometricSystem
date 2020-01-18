package com.luca.sabatini.appello.professor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.luca.sabatini.appello.utils.RestConstants;

import java.util.Objects;

public class ClosingService extends Service {
    private String TAG = "ClosingService";
    private Long sessionId;
    private RequestQueue queue;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sessionId = intent.getLongExtra("session_id", -1L);

        Log.d(TAG, "onStartCommand: " + sessionId);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Handle application closing
        closeAttendanceSessionBackend();

        // Destroy the service
        stopSelf();
    }

    private void closeAttendanceSessionBackend(){
        Log.d(TAG, "closeAttendanceSessionBackend: " + sessionId);
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.closeSessionUrl(sessionId),
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
            //Long id = new Gson().fromJson(response, Long.class);

            //startActivity(new Intent(context, ProfessorMain.class));
        }
    };


}