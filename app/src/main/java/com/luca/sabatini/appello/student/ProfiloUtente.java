package com.luca.sabatini.appello.student;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import static com.luca.sabatini.appello.login.LoginIntroFragment.EXTRA_ACTION;

public class ProfiloUtente extends AppCompatActivity {

    private TextView matricolaTextView;
    private TextView surnameTextView;
    private SharedPrefManager sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_utente);
        sp = new SharedPrefManager(this);

        matricolaTextView = findViewById(R.id.matricola_textView);
        surnameTextView = findViewById(R.id.surname_textView);

        matricolaTextView.setText("" + sp.readMatricola());
        surnameTextView.setText(sp.readSurname());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void ricercaAppelliFabOnClick(View view){
        startActivity(new Intent(this, RicercaAppelli.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profilo_utente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.modifica_foto_profilo_item) {
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra(EXTRA_ACTION, "changePhoto");
            startActivity(intent);
            return true;
        }
        else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            sp.writeSurname("");
            sp.writeMatricola(-1L);
            sp.writeFirebaseId("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
