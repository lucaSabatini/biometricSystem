package com.example.luca.biometricsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.biometricsystem.entities.Persona;

public class Appello extends AppCompatActivity {

    private TextView lastName;
    private TextView studentId;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appello);
        Persona persona = (Persona) getIntent().getSerializableExtra("Persona");
        lastName = findViewById(R.id.last_name_appello);
        studentId = findViewById(R.id.student_id_appello);
        foto = findViewById(R.id.foto_appello);
        lastName.setText(persona.getLastName().toUpperCase());
        studentId.setText(persona.getStudentId());
    }
}
