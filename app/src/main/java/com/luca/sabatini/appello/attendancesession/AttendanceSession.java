package com.luca.sabatini.appello.attendancesession;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Student;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;


public class AttendanceSession extends AppCompatActivity {

    private RecyclerView attendanceRecycler;
    private AttendanceAdapter attendanceAdapter;

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

        attendanceAdapter = new AttendanceAdapter(this, students);

        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecycler.setAdapter(attendanceAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void closeAttendanceSession(View view){

        finish();
        mRealm.where(Student.class).findAll().deleteAllFromRealm();

    }
}
