package com.luca.sabatini.appello.professor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.StatisticsActivity;
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
        context = this;
        sp = new SharedPrefManager(this);
        nomeCorsoTextView = findViewById(R.id.nome_corso);
        annoCorsoTextView = findViewById(R.id.anno_corso);
        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        nomeCorsoTextView.setText(sp.readNomeCorso());
        annoCorsoTextView.setText(""+sp.readAnnoCorso());
    }


    public void openStatisticsActivity(View view){
        startActivity(new Intent(this, StatisticsActivity.class));
    }

    public void openAttendanceSession(View v){
        startActivity(new Intent(this, BeaconScanProfessor.class));
    }





}
