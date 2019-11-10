package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;


public class Registrazione extends AppCompatActivity {

    private static final String TAG = "Registrazione";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FloatingActionButton cameraButton;
    private ImageView userImage;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout studentId;
    private String firstNameValue;
    private String lastNameValue;
    private String studentIdValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        cameraButton = findViewById(R.id.camera_button);
        userImage = findViewById(R.id.user_image);
        firstName = findViewById(R.id.first_name_layout);
        lastName = findViewById(R.id.last_name_layout);
        studentId = findViewById(R.id.student_id_layout);
    }


    public boolean validateLastName(){
        lastNameValue = lastName.getEditText().getText().toString().trim();

        if (lastNameValue.isEmpty()){
            lastName.setError("Field can't be empty");
            return false;
        }else{
            lastName.setError(null);
            return true;
        }
    }

    public boolean validateStudentId(){
        studentIdValue = studentId.getEditText().getText().toString().trim();

        if (studentIdValue.isEmpty()){
            studentId.setError("Field can't be empty");
            return false;
        }else{
            studentId.setError(null);
            return true;
        }
    }

    public boolean validateFirstName(){
        firstNameValue = firstName.getEditText().getText().toString().trim();

        if (firstNameValue.isEmpty()){
            firstName.setError("Field can't be empty");
            return false;
        }else{
            firstName.setError(null);
            return true;
        }
    }

    public void openCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void registraValori(View view){
        if( !validateFirstName() | !validateLastName() | !validateStudentId()){
            return;
        }

        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userImage.setImageBitmap(imageBitmap);
        }
    }
}
