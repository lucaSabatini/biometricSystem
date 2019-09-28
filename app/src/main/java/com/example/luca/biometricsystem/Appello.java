package com.example.luca.biometricsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class Appello extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appello);

        context = this;

    }

     public void cercaFoto(View v){
         TextView matricolaTv = findViewById(R.id.matricolaField);
         String matricola = matricolaTv.getText().toString();
         if(matricola.equals("")){
             Toast.makeText(context, "inserisci matricola", Toast.LENGTH_LONG);
             return;
         }

         MyTaskParams mtp = new MyTaskParams();
         mtp.matricola = matricola;
         mtp.path = context.getFileStreamPath(matricola + ".png").toPath();
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

         new scaricaFoto().execute(mtp);

     }
     class MyTaskParams{
        public Path path;
        public String matricola;
        public Storage storage;
     }

      class scaricaFoto extends AsyncTask<MyTaskParams, Void, MyTaskParams>  {
        @Override
        protected MyTaskParams doInBackground(MyTaskParams... mtp) {
            try {
                Storage storage = mtp[0].storage;

                // Get specific file from specified bucket
                Blob blob = storage.get(BlobId.of("mobile-app-5c2bf.appspot.com", mtp[0].matricola + ".png"));

                // Download file to specified path
                blob.downloadTo(mtp[0].path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mtp[0];
        }

         @Override
         protected void onPostExecute(MyTaskParams mtp) {
             super.onPostExecute(mtp);
             ImageView im = findViewById(R.id.imageServer);
             im.setImageBitmap(BitmapFactory.decodeFile(context.getFileStreamPath(mtp.matricola + ".png").toString()));


         }
     }


}
