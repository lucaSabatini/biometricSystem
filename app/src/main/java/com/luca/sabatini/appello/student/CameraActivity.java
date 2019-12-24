package com.luca.sabatini.appello.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.luca.sabatini.appello.R;

import com.luca.sabatini.appello.entities.Student;
import com.luca.sabatini.appello.entities.StudentBuilder;
import com.luca.sabatini.appello.login.LoginActivity;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;

import java.util.Base64;

import static com.luca.sabatini.appello.login.LoginIntroFragment.EXTRA_ACTION;


public class CameraActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    private final String TAG = "CameraActivity";
    public static final String EXTRA_BITMAP = "com.example.luca.biometricsystem.student.CameraActivity";
    private CameraView camera;
    private ImageView fotoCamera;
    private FloatingActionButton floatingActionButtonCamera;
    private Button riprova;
    private Button conferma;
    private byte[] data;
    Context context;

    RequestQueue queue;
    private String action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = this;
        if(Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreate: camera rifiutata");
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }

        queue = Volley.newRequestQueue(this);
        action = getIntent().getStringExtra(EXTRA_ACTION);
        camera = findViewById(R.id.camera);
        fotoCamera = findViewById(R.id.foto_camera);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);
        riprova = findViewById(R.id.riprova);
        conferma = findViewById(R.id.conferma);
        FocusView focusView = new FocusView(this);
        fotoCamera.setScaleType(ImageView.ScaleType.CENTER_CROP);
        camera.addView(focusView);
        camera.setLifecycleOwner(this);
        camera.setFacing(Facing.FRONT);
        camera.setMode(Mode.PICTURE); // for pictures

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                // Picture was taken!
                // If planning to show a Bitmap, we will take care of
                // EXIF rotation and background threading for you...
                //result.toBitmap();
                result.toBitmap(new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        Log.d(TAG, "onBitmapReady: " + bitmap.getByteCount());
                        fotoCamera.setImageBitmap(bitmap);
                        visibleNotVisible("show_photo");

                        //Salviamo nel backend la registrazione avvenuta

                    }
                });
                // If planning to save a file on a background thread,
                // just use toFile. Ensure you have permissions.
                //result.toFile(file, callback);
                // Access the raw data if needed.
                data = result.getData();
                //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //imageView.setImageBitmap(bitmap);

                /*Intent output = new Intent();
                output.putExtra(EXTRA_BITMAP, data);
                setResult(100,output);*/

                camera.close();
            }
        });

    }

    private Canvas temp;
    private Paint paint;
    private Paint p = new Paint();
    private Paint transparentPaint;

    public void takePicture(View view){
        camera.takePicture();
    }

    public void riprova(View view){
        visibleNotVisible("try_again");
        camera.open();
    }

    public void conferma(View view){
        //inviare immagine al server "data"
        registerUser(Base64.getEncoder().encodeToString(data));
    }

    private void registerUser(String photo){
        SharedPrefManager sp = new SharedPrefManager(getBaseContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                RestConstants.postStudentUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        sp.writeIsRegistered(true);
                        if(action.equals("login")){
                            startActivity(new Intent(context, ConfermaPresenza.class));
                        }
                        else if(action.equals("signup")) {
                            startActivity(new Intent(context, UserProfile.class));
                        }
                        else if(action.equals("changePhoto")){
                            startActivity(new Intent(context, UserProfile.class));
                        }else{
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                        finish();
                    }
                }, callbackError){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    Student s = new StudentBuilder()
                    .setMatricola(sp.readMatricola())
                    .setFirebaseId(sp.readFirebaseId())
                    .setSurname(sp.readSurname())
                    .setPhoto(photo)
                    .createStudent();

                    Log.d(TAG, "getBody: "+ s);
                    return new Gson().toJson(s).getBytes();
                } catch (Exception e) {
                    Log.d(TAG, "getBody: " + e.toString());
                    return null;
                }
            }
        };
        queue.add(stringRequest);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.d(TAG, "onRequestPermissionsResult: sto controllando i permessi");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //Log.d(TAG, "onRequestPermissionsResult: camera non accettata");
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    private void visibleNotVisible(String action){
        if(action.equals("show_photo")){
            camera.setVisibility(View.GONE);
            floatingActionButtonCamera.hide();
            riprova.setVisibility(View.VISIBLE);
            conferma.setVisibility(View.VISIBLE);
            fotoCamera.setVisibility(View.VISIBLE);

        }else{
            camera.setVisibility(View.VISIBLE);
            floatingActionButtonCamera.show();
            riprova.setVisibility(View.GONE);
            conferma.setVisibility(View.GONE);
            fotoCamera.setVisibility(View.GONE);

        }
    }

}
