package com.luca.sabatini.appello.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.luca.sabatini.appello.LoadingDialog;
import com.luca.sabatini.appello.R;

import com.luca.sabatini.appello.entities.CheckSessionResponse;
import com.luca.sabatini.appello.entities.Student;
import com.luca.sabatini.appello.entities.StudentBuilder;
import com.luca.sabatini.appello.login.LoginActivity;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    private final String TAG = "CameraActivity";
    private CameraView camera;
    private ImageView fotoCamera;
    private FloatingActionButton floatingActionButtonCamera;
    private Button riprova;
    private Button conferma;
    private byte[] data;
    Context context;
    public final static String EXTRA_ACTION = "com.example.luca.biometricsystem.logingeneroso";
    SharedPrefManager sp;
    RequestQueue queue;
    private String action;
    // Progress dialog popped up when communicating with server.
    LoadingDialog loadingDialog;

    FaceServiceClient faceServiceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        faceServiceClient = new FaceServiceRestClient("https://face-subscription.cognitiveservices.azure.com/face/v1.0/","816bb822c29241f5aae719e540404311");
        loadingDialog = new LoadingDialog(this, this);
        sp = new SharedPrefManager(this);

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


    public void takePicture(View view){
        camera.takePicture();
    }

    public void riprova(View view){
        visibleNotVisible("try_again");
        camera.open();
    }

    public void conferma(View view){

        loadingDialog.show(getSupportFragmentManager(), "LoadingDialog");

        if(action.equals("signup")){
            Log.d(TAG, "signup");
            registerUser(Base64.getEncoder().encodeToString(data));
        }
        else if(action.equals("verification")) {
            Log.d(TAG, "verification");
            verifyUser();
        }
        else if(action.equals("changePhoto")){
            Log.d(TAG, "changePhoto");
            postNewRegistrationPhoto(data);
            //startActivity(new Intent(context, UserProfile.class));
        }else{
            Log.e(TAG, "Invalid Login action " + action );
        }
    }

    private void postNewRegistrationPhoto(byte[] photo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                RestConstants.changeRegistrationPhotoUrl(sp.readMatricola()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        //startActivity(new Intent(context, UserProfile.class));
                        Toast.makeText(context, "Foto profilo aggiornata", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, callbackError){
            @Override
            public String getBodyContentType() {
                return "application/octet-stream; charset=utf-8";
            }
            @Override
            public byte[] getBody() {
                try {
                    return photo;
                } catch (Exception e) {
                    Log.d(TAG, "getBody: " + e.toString());
                    return null;
                }
            }
        };
        queue.add(stringRequest);
    }

    private void verifyUser(){

        //startActivity(new Intent(context, ConfermaPresenza.class));
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                RestConstants.getRegistrationPhotoUrl(sp.readMatricola()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        byte[] registrationPhoto = new Gson().fromJson(response, byte[].class);
                        //sp.writeIsRegistered(true);
                        //startActivity(new Intent(context, UserProfile.class));
                        detect(data, registrationPhoto);
                        //finish();
                    }
                }, callbackError);
        //queue.add(stringRequest);

        BinaryRequest binaryRequest = new BinaryRequest(0, RestConstants.getRegistrationPhotoUrl(sp.readMatricola()), new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Log.d(TAG, "onResponse: " + response);
                //byte[] registrationPhoto = new Gson().fromJson(response, byte[].class);
                //sp.writeIsRegistered(true);
                //startActivity(new Intent(context, UserProfile.class));
                detect(response, data);
                //finish();
            }
        }, callbackError
        );

        queue.add(binaryRequest);

    }

    private class VerificationTask extends AsyncTask<Void, String, VerifyResult> {
        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask (UUID faceId0, UUID faceId1) {
            Log.i("CIAO", "faceid0: "+faceId0.toString());
            Log.i("CIAO", "faceid1: "+faceId1.toString());
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
        }

        @Override
        protected VerifyResult doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.
            try{
                publishProgress("Verifying...");

                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            }  catch (Exception e) {
                publishProgress(e.getMessage());
//                addLog(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(VerifyResult result) {
            if (result != null) {
                /*addLog("Response: Success. Face " + mFaceId0 + " and face "
                        + mFaceId1 + (result.isIdentical ? " " : " don't ")
                        + "belong to the same person");*/
                Log.i("CIAO", "onPostExecute: "+result.isIdentical);
                Log.d(TAG, "onPostExecute: " + result.confidence);
                Toast.makeText(context, "confidence " + result.confidence, Toast.LENGTH_LONG).show();
                if(result.confidence > 0.6){
                    Intent i = new Intent(context, ConfermaPresenza.class);
                    i.putExtra("happy", true);
                    startActivity(i);
                } else {
                    Intent i = new Intent(context, ConfermaPresenza.class);
                    i.putExtra("happy", false);
                    startActivity(i);
                }
            }
            loadingDialog.dismiss();

        }
    }


    // Background task of face detection.
    private class DetectionTask extends AsyncTask<InputStream, String, Face[]> {

        @Override
        protected Face[] doInBackground(InputStream... params) {
            Face[]  res = new Face[2];
            for (int i = 0; i < 2; i ++){
                try{
                    Face[] result1 = faceServiceClient.detect(
                            params[i],  /* Input stream of image to detect */
                            true,       /* Whether to return face ID */
                            false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                            null);
                    if (result1.length == 0){
                        Intent intent = new Intent(context, ConfermaPresenza.class);
                        intent.putExtra("happy", false);
                        startActivity(intent);
                        return null;
                    } else{
                        res[i] = result1[0];
                    }

                }  catch (Exception e) {
                    publishProgress(e.getMessage());
                }
            }
            return res;
        }


        @Override
        protected void onPostExecute(Face[] result) {
            // Show the result on screen when detection is done.
            if (result == null) return;

            Log.i(TAG, "identification: ");
            Log.d(TAG, "onPostExecute: " + result[0].faceId);
            Log.d(TAG, "onPostExecute: " + result[1].faceId);
            new VerificationTask(result[0].faceId, result[1].faceId).execute();
            //setUiAfterDetection(result, mIndex, mSucceed);
        }
    }




    // Start detecting in image specified by index.
    private void detect(byte[] photo, byte[]registrationPhoto) {
        new DetectionTask().execute(new ByteArrayInputStream(photo), new ByteArrayInputStream(registrationPhoto));
    }


    private void registerUser(String photo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                RestConstants.postStudentUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        sp.writeIsRegistered(true);
                        startActivity(new Intent(context, UserProfile.class));
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
