package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.biometricsystem.entities.Persona;

public class Appello extends AppCompatActivity {

    private static final String TAG = "Appello";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextView lastName;
    private TextView studentId;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Persona persona = (Persona) getIntent().getSerializableExtra("Persona");
        //lastName = findViewById(R.id.last_name_appello);
        //studentId = findViewById(R.id.student_id_appello);
       //lastName.setText(persona.getLastName().toUpperCase());
       //studentId.setText(persona.getStudentId());
    }

    public void openCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
        }
    }
}
