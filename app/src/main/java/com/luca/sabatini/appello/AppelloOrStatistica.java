package com.luca.sabatini.appello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.luca.sabatini.appello.attendancesession.AttendanceSession;
import com.luca.sabatini.appello.ui.courseList.ListaCorsi;

public class AppelloOrStatistica extends AppCompatActivity {

    private static final String TAG = "AppelloOrStatistica";
    private TextView nomeCorsoTextView;
    private TextView annoCorsoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appello_or_statistica);
        Intent intent = getIntent();
        String nomeCorso = intent.getStringExtra(ListaCorsi.EXTRA_TEXT);
        Long annoCorso = intent.getLongExtra(ListaCorsi.EXTRA_DATE, -1);

        nomeCorsoTextView = findViewById(R.id.nome_corso);
        annoCorsoTextView = findViewById(R.id.anno_corso);

        nomeCorsoTextView.setText(nomeCorso);
        annoCorsoTextView.setText(""+annoCorso);

    }

    public void openAttendanceSession(View v){
        startActivity(new Intent(this, AttendanceSession.class));
    }

    public void openStatisticsActivity(View view){
        startActivity(new Intent(this, StatisticsActivity.class));
    }

}
