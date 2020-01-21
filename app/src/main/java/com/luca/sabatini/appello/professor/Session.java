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
import com.luca.sabatini.appello.entities.StudentBuilder;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Objects;

public class Session extends AppCompatActivity {

    private final String TAG = "Session";
    private RecyclerView attendanceRecycler;
    private SessionAdapter sessionAdapter;
    public static ArrayList<Student> students;
    private RequestQueue queue;
    private final SharedPrefManager sp = new SharedPrefManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_session);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        students = new ArrayList<>();
        StudentBuilder sb = new StudentBuilder();
        //students.add(sb.setSurname("peppo").setMatricola(1231231L).createStudent());
        //students.add(sb.setSurname("giampi").setMatricola(12312351L).createStudent());
        attendanceRecycler = findViewById(R.id.attendanceRecycle);

        sessionAdapter = new SessionAdapter(students);

        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecycler.setAdapter(sessionAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, ClosingService.class);
        i.putExtra("session_id", sp.readSessionId());
        startService(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
        closeAttendanceSessionBackend();
    }

    public void closeAttendanceSession(View v){
        finish();
    }

    @Subscribe
    public void onDatasetUpdated(Student s) {
        //Update RecyclerViews
        Log.d(TAG, "onDatasetUpdated: " + s);
        students.add(s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sessionAdapter.notifyItemInserted(students.size() - 1);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHolder.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusHolder.getInstance().unregister(this);
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

            //startActivity(new Intent(context, ProfessorMain.class));
        }
    };

    
}
