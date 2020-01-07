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

import com.luca.sabatini.appello.ProfessorProfile;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.student.CameraActivity;
import com.luca.sabatini.appello.student.UserProfile;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import static com.luca.sabatini.appello.student.CameraActivity.EXTRA_ACTION;

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
        Log.d(TAG, "onCreate: isLogged: " + isLogged() + "isStudent: " + isStudent() + "isRegistered: " + sp.readIsRegistered());
        if(isLogged()){
            if(isStudent()){
                if(sp.readIsRegistered()) {
                    startActivity(new Intent(context, UserProfile.class));
                }
                else {
                    Intent intent = new Intent( context , CameraActivity.class);
                    intent.putExtra(EXTRA_ACTION, "signup");
                    startActivity(intent);
                }
            }
            else{
                startActivity(new Intent(context, ProfessorProfile.class));
            }
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        route("sign_in");
    }

    public boolean isLogged() {
        Log.d(TAG, "isLogged: id" + sp.readFirebaseId() + "surname " + sp.readSurname());
        return !(sp.readFirebaseId().equals(SharedPrefManager.DEFAULT_VALUE)) &&
                !(sp.readSurname().equals(SharedPrefManager.DEFAULT_VALUE));
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
