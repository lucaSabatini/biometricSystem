package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
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
    private Context context;

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
        context = this;

        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
    }


    private void saveUser(){
        String requestString = "https://mobile-app-5c2bf.appspot.com/postUser?matricola=" +
                studentId.getEditText().getText()+
                "&name=" + firstName.getEditText().getText() +
                "&surname=" + lastName.getEditText().getText() +
                "&photo=" + photo;
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

        if( !validateStudentId()){
            return;
        }

        TextView matricolaTv = findViewById(R.id.studentId);
        String matricola = matricolaTv.getText().toString();

        MyTaskParams mtp = new MyTaskParams();
        mtp.matricola = matricola;
        mtp.path = this.getFileStreamPath(matricola + ".png").toPath();
        try {

            Storage storage =
                    StorageOptions.newBuilder()
                            .setCredentials(
                                    ServiceAccountCredentials.fromStream(
                                            getAssets().open("credentials.json")))
                            .build()
                            .getService();
            mtp.storage = storage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mtp.photo = photo;

        new caricaFoto().execute(mtp);


        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
    }



     class caricaFoto extends AsyncTask<MyTaskParams, Void, Void> {
        @Override
        protected Void doInBackground(MyTaskParams... mtp) {
            try {
                Storage storage = mtp[0].storage;


                BlobId blobId = BlobId.of("mobile-app-5c2bf.appspot.com", mtp[0].matricola + ".png");

                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
                Blob blob = storage.create(blobInfo, bo(mtp[0].photo));


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "scaricato", Toast.LENGTH_LONG);
        }

        private byte[] bo   (Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }


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

    public static byte[] convertBitmapToByteArrayUncompressed(Bitmap bitmap){
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }

    class MyTaskParams{
        public Path path;
        public String matricola;
        public Storage storage;
        public Bitmap photo;
    }


}
