package com.example.luca.biometricsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.luca.biometricsystem.list.ListaCorsi;

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

        nomeCorsoTextView = findViewById(R.id.nome_corso);
        annoCorsoTextView = findViewById(R.id.anno_corso);

        nomeCorsoTextView.setText(nomeCorso);


    }
}
