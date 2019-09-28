package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Objects;

import static com.google.api.client.util.Charsets.UTF_8;


public class Registrazione extends AppCompatActivity {
    private final String TAG = "Registrazione";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FloatingActionButton cameraButton;
    private ImageView userImage;
    private TextInputLayout firstName;
    private TextInputLayout lastName;
    private TextInputLayout studentId;
    private String firstNameValue;
    private String lastNameValue;
    private String studentIdValue;
    private Bitmap photo;

    //For REST API requests
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        cameraButton = findViewById(R.id.camera_button);
        userImage = findViewById(R.id.user_image);
        firstName = findViewById(R.id.first_name_layout);
        lastName = findViewById(R.id.last_name_layout);
        studentId = findViewById(R.id.student_id_layout);

        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
    }


    private void saveUser(){
        String requestString = "https://mobile-app-5c2bf.appspot.com/postUser?matricola=" +
                studentId.getEditText().getText()+
                "&name=" + firstName.getEditText().getText() +
                "&surname=" + lastName.getEditText().getText() +
                "&photo=" + bitmapToBase64(photo);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: t'appost");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        String responseBody = null;
                        try {
                            responseBody = new String(error.networkResponse.data, "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        JSONObject data = null;
                        try {
                            data = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = data.optString("msg");
                        Log.i(TAG, "Volley error" + message);
                    }
                });

        queue.add(stringRequest);
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
        /*
        if( !validateFirstName() | !validateLastName() | !validateStudentId()){
            return;
        }
        */

        //saveUser();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {

                    //String SERVICE_ACCOUNT_JSON_PATH = "assets/";

                    Storage storage =
                            StorageOptions.newBuilder()
                                    .setCredentials(
                                            ServiceAccountCredentials.fromStream(
                                                    getAssets().open("credentials.json")))
                                    .build()
                                    .getService();

                    BlobId blobId = BlobId.of("mobile-app-5c2bf.appspot.com", "blob_name");

                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
                    Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            userImage.setImageBitmap(imageBitmap);
            photo = imageBitmap;



        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
