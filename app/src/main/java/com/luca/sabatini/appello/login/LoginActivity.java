package com.luca.sabatini.appello.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luca.sabatini.appello.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.list.ListaCorsi;
import com.luca.sabatini.appello.student.ProfiloUtente;
import com.luca.sabatini.appello.utils.SharedPrefManager;


public class LoginActivity extends AppCompatActivity implements LoginRoutingInterface{

    private final Context context = this;
    private final static String TAG = "LoginActivity";
    private String shown;
    private SharedPrefManager sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = new SharedPrefManager(context);
        //Login automatico
        if(isLogged()){
            if(isStudent()){
                context.startActivity(new Intent(context, ProfiloUtente.class));
            }
            else{
                context.startActivity(new Intent(context, ListaCorsi.class));
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        route("sign_in");
    }

    public boolean isLogged() {
        return !sp.readFirebaseId().equals(SharedPrefManager.NULL_KEY) &&
                !sp.readSurname().equals(SharedPrefManager.NULL_KEY);
    }

    public boolean isStudent() {
        return sp.readMatricola() != -1;
    }

    private Fragment buildFragment(String fragment){

        switch (fragment){

            case "sign_in":
                Bundle bundle1 = new Bundle();
                bundle1.putString("action", "");
                LoginIntroFragment ciao = new LoginIntroFragment();
                ciao.setArguments(bundle1);
                return ciao;

            case "new_account":
                Bundle bundle = new Bundle();
                bundle.putString("action", "register");
                LoginSignInFragment lf = new LoginSignInFragment();
                lf.setArguments(bundle);
                return lf;

            default:
                return new Fragment();
        }
    }

    @Override
    public void route(String dest) {

        if(dest.equals("main_activity")){
            startActivity(new Intent(context, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction tr = fragmentManager.beginTransaction();

            /// qui inseriamo le transizioni, se necessario
            if (dest.equals("new_account"))
                tr.setCustomAnimations(R.anim.slideup_in, R.anim.slideup_out);

            if (dest.equals("sign_in") && shown != null)
                tr.setCustomAnimations(R.anim.slidedown_out, R.anim.slidedown_in);

            tr.replace(R.id.fragment_container, buildFragment(dest)).commit();
            shown = dest;
            Log.d(TAG, "route: shown: " + shown);
        }
    }

    @Override
    public void onBackPressed() {
        if(shown == null || shown.equals("sign_in")) {
            finish();
            return;
        }
        route("sign_in");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}
