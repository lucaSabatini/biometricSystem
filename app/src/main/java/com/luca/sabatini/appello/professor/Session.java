package com.luca.sabatini.appello.professor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Student;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


public class Session extends AppCompatActivity {

    private final String TAG = "Session";
    private RecyclerView attendanceRecycler;
    private SessionAdapter sessionAdapter;

    private RequestQueue queue;
    private Realm mRealm;
    private final SharedPrefManager sp = new SharedPrefManager(this);
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_session);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        mRealm = Realm.getDefaultInstance();
        attendanceRecycler = findViewById(R.id.attendanceRecycle);
        final RealmResults<Student> students = mRealm.where(Student.class).findAll();

        sessionAdapter = new SessionAdapter(this, students);

        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecycler.setAdapter(sessionAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeAttendanceSession(null);
    }

    public void closeAttendanceSession(View v){
        mRealm.where(Student.class).findAll().deleteAllFromRealm();
        mRealm.close();
        closeAttendanceSessionBackend();

    }


    private void closeAttendanceSessionBackend(){
        Log.d(TAG, "closeAttendanceSessionBackend: " + sp.readSessionId());
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.closeSessionUrl(sp.readSessionId()),
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
            sp.writeSessionId(0L);
            //startActivity(new Intent(context, ProfessorMain.class));
            finish();
        }
    };
}
