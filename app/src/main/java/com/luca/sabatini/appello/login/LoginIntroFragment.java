package com.luca.sabatini.appello.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.luca.sabatini.appello.ProfessorProfile;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.student.UserProfile;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.luca.sabatini.appello.student.CameraActivity.EXTRA_ACTION;

public class LoginIntroFragment extends Fragment {

    private static final String TAG = "LoginIntroFragment";

    private static String emailPattern = "([a-z]+[.][0-9]+@studenti[.]uniroma1[.]it)|([a-z]+@di[.]uniroma1[.]it)";
    private TextView newAccount;
    private TextInputLayout usernameTextInput;
    private TextInputLayout passwordTextInput;
    private Button signInOrCreate;
    private ProgressBar signInProgressBar;
    private TextView textView;
    private Context context;
    private TextView appName;
    private LoginRoutingInterface callback;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SharedPrefManager sp;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.login_intro_fragment, container, false);
        signInProgressBar = inflate.findViewById(R.id.sign_in_progress_bar);
        textView = inflate.findViewById(R.id.textView_forgot_password);
        newAccount = inflate.findViewById(R.id.login_new_account);
        signInOrCreate = inflate.findViewById(R.id.login_button);
        passwordTextInput = inflate.findViewById(R.id.password);
        usernameTextInput = inflate.findViewById(R.id.username);

        TextView appName = (TextView) inflate.findViewById(R.id.appName);
        //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "font/candal.ttf");
        Typeface tf = ResourcesCompat.getFont(context, R.font.candal);
        appName.setTypeface(tf);

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginNewAccount();
            }
        });

        signInOrCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignInButton();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ForgotPassword.class));
            }
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = new SharedPrefManager(getContext());
        queue = Volley.newRequestQueue(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof LoginRoutingInterface) {
            this.callback = (LoginRoutingInterface) context;
        }
        else {
            this.callback = null;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.callback = null;
    }


    public void loginNewAccount(){
        if(callback != null)
            callback.route("new_account");
    }

    private boolean validateEmail(){
        String emailValue = usernameTextInput.getEditText().getText().toString().trim();
        if (emailValue.isEmpty()) {
            usernameTextInput.setError("Field can't be empty");
            return false;
        } else if(!emailValue.matches(emailPattern)){
            clear();
            usernameTextInput.setError("invalid e-mail");
            return false;
        } else{
            usernameTextInput.setError(null);
            return true;
        }
    }


    private boolean validatePassword(){
        String passwordValue = passwordTextInput.getEditText().getText().toString().trim();
        if (passwordValue.isEmpty()){
            passwordTextInput.setError("Field can't be empty");
            return false;
        }
        else if(passwordValue.length() < 6){
            passwordTextInput.setError("Password is too short");
            return false;
        }else{
            passwordTextInput.setError(null);
            return true;
        }
    }

    private void clear(){
        usernameTextInput.getEditText().getText().clear();
        passwordTextInput.getEditText().getText().clear();
    }

    public Boolean isInputBad(){
        return (!validateEmail() | !validatePassword());
    }

    public boolean isStudent() {
        return sp.readMatricola() != -1;
    }

    public void loginSignInButton(){
        if(isInputBad()) {
            Toast.makeText(getContext(), getResources().getString(R.string.not_valid_credentials), Toast.LENGTH_SHORT).show();
            return;
        }
        signInProgressBar.setVisibility(View.VISIBLE);
        String username = usernameTextInput.getEditText().getText().toString().trim();
        String password = passwordTextInput.getEditText().getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signInProgressBar.setVisibility(View.INVISIBLE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }/*
                        else if(!firebaseAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(context, "e-mail is not verified", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            clear();
                        }*/
                        else {
                            String emailBeforeAt = username.split("@")[0];
                            if (emailBeforeAt.contains(".")) {
                                Log.d(TAG, "setLastNameAndStudentId: login studente");
                                sp.writeMatricola(Long.parseLong(emailBeforeAt.split("\\.")[1]));
                            }
                            sp.writeSurname(emailBeforeAt.split("\\.")[0]);
                            sp.writeFirebaseId(firebaseAuth.getCurrentUser().getUid());

                            if(isStudent()){
                                checkRegistered();
                            }else{
                                startActivity(new Intent(getActivity(), ProfessorProfile.class));
                            }
                            //getActivity().finish();
                        }
                    }
                });

    }

    private void checkRegistered(){
        StringRequest postRequest = new StringRequest(
                Request.Method.GET,
                RestConstants.isRegisteredUrl(sp.readFirebaseId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean result = new Gson().fromJson(response, Boolean.class);
                        Log.d(TAG, "onResponse: " + result);
                        if(result){
                            sp.writeIsRegistered(true);
                            startActivity(new Intent( getActivity(), UserProfile.class));
                        }
                        else {
                            Intent intent = new Intent( getActivity() , LoginActivity.class);
                            intent.putExtra(EXTRA_ACTION, "signup");
                            startActivity(intent);
                        }
                    }
                },
                callbackError);

        queue.add(postRequest);
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

}
