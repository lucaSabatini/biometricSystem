package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.biometricsystem.login.LoginActivity;
import com.otaliastudios.cameraview.BitmapCallback;


import static com.example.luca.biometricsystem.login.LoginIntroFragment.EXTRA_ACTION;

public class RegistrazioneFoto extends AppCompatActivity {

    private static final String TAG = "RegistrazioneFoto";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Button buttonCamera;
    private TextView titolo;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_foto);
        action = getIntent().getStringExtra(EXTRA_ACTION);
        //Log.d(TAG, "onCreate: "+action);
        imageView = findViewById(R.id.imageView);
        buttonCamera = findViewById(R.id.button_camera);
        titolo = findViewById(R.id.titolo);
        /*if(action.equals("login")){
            titolo.setText("Login");
        }*/

       if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 2);
        }
    }

    public void openCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(new Intent(this, CameraActivity.class), 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            BitmapCallback bitmapCallback = new BitmapCallback() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }
            };
            /*Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            if(captureImage != null){
                imageView.setImageBitmap(captureImage);
                if(action.equals("signup")){
                    //inviare l'immagine al server
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }else{
                    //interazione con AZZZZZZZZURE
                }
            }*/
        }
    }
}
