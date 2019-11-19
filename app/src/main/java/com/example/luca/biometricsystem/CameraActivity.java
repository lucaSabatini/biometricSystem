package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class CameraActivity extends AppCompatActivity {
    private final String TAG = "CameraActivity";
    public static final String EXTRA_BITMAP = "com.example.luca.biometricsystem.CameraActivity";
    private CameraView camera;
    private ImageView fotoCamera;
    private FloatingActionButton floatingActionButtonCamera;
    private Button riprova;
    private Button conferma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        camera = findViewById(R.id.camera);
        fotoCamera = findViewById(R.id.foto_camera);
        floatingActionButtonCamera = findViewById(R.id.floatingActionButtonCamera);
        riprova = findViewById(R.id.riprova);
        conferma = findViewById(R.id.conferma);
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
                        camera.setVisibility(View.GONE);
                        floatingActionButtonCamera.hide();
                        riprova.setVisibility(View.VISIBLE);
                        conferma.setVisibility(View.VISIBLE);
                        fotoCamera.setVisibility(View.VISIBLE);

                    }
                });
                // If planning to save a file on a background thread,
                // just use toFile. Ensure you have permissions.
                //result.toFile(file, callback);

                // Access the raw data if needed.
                //byte[] data = result.getData();
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
        camera.setVisibility(View.VISIBLE);
        floatingActionButtonCamera.show();
        riprova.setVisibility(View.GONE);
        conferma.setVisibility(View.GONE);
        fotoCamera.setVisibility(View.GONE);
        camera.open();
    }

    public void conferma(View view){
        //do nothing
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

}
