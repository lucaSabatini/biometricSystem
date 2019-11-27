package com.luca.sabatini.appello.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.luca.sabatini.appello.AppelloOrStatistica;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaCorsi extends AppCompatActivity implements AddCourseAlert.ProvaAlertListener {

    private static final String TAG = "ListaCorsi";
    public static final String EXTRA_TEXT = "com.example.luca.biometricsystem.list.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.luca.biometricsystem.list.EXTRA_DATE";


    private RecyclerView listaCorsiRecycler;
    private CorsoAdapter listaCorsiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;
    private TextView noCoursesLabel;

    private RequestQueue queue;

    private Realm mRealm;

    private final SharedPrefManager sp = new SharedPrefManager(this);

    private Context context = this;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corsi);

        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        layoutManager = new LinearLayoutManager(this);
        mRealm = Realm.getDefaultInstance();
        swipeRefreshLayout = findViewById(R.id.lista_corsi_refresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getColor(R.color.design_default_color_primary_dark));
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: refresh");

                StringRequest postRequest = new StringRequest(
                        Request.Method.GET,
                        RestConstants.getAllCoursesByIdUrl("nessuno", sp.readString("uid")),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                callbackGet.onResponse(response);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                callbackError.onErrorResponse(error);
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(context,"Sei offline", Toast.LENGTH_LONG);
                            }
                        });

                queue.add(postRequest);

            }
        });

        noCoursesLabel = findViewById(R.id.noCoursesLabel);
        listaCorsiRecycler = findViewById(R.id.lista_corsi);

        buildRecyclerView();
    }
    public void buildRecyclerView(){

        final RealmResults<Corso> courses = mRealm.where(Corso.class).equalTo("uid", sp.readString("uid")).findAll();

        listaCorsiAdapter = new CorsoAdapter(this, courses);

        listaCorsiRecycler.setLayoutManager(layoutManager);

        courses.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Corso>>() {
            @Override
            public void onChange(@NonNull RealmResults<Corso> courses, @NonNull OrderedCollectionChangeSet changeSet) {
                if(courses.isEmpty()) enableNoCoursesUI();
                else disableNoCoursesUI();
            }
        });

        listaCorsiAdapter.setOnItemClickListener(new CorsoAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(Corso c) {
                openActivity(c.name,  c.year);
            }

            @Override
            public void onDeleteClick(Corso c) {
                removeItem(c);
            }

            @Override
            public void onRenameClick(Corso c) {
                openRenameAlert(c);
            }
        });

        listaCorsiRecycler.setAdapter(listaCorsiAdapter);

        if(isOnline()) {
            StringRequest postRequest = new StringRequest(
                    Request.Method.GET,
                    RestConstants.getAllCoursesByIdUrl("nessuno", sp.readString("uid")),
                    callbackGet,
                    callbackError);

            queue.add(postRequest);
        }
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

    private Response.Listener<String> callbackGet = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {

                JSONArray items = new JSONArray(response);

                if(items.length() == 0) {
                    enableNoCoursesUI();
                    return;
                }
                for( int i = 0; i < items.length(); i++){
                        Corso course = new Gson().fromJson(items.getJSONObject(i).toString(), Corso.class);
                        Log.d(TAG, "onResponse: " + course);
                        mRealm.beginTransaction();
                        mRealm.insertOrUpdate(course);
                        mRealm.commitTransaction();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    //TODO: Qui si imposta il layout della lista vuota
    private void enableNoCoursesUI(){
        noCoursesLabel.setVisibility(View.VISIBLE);
        listaCorsiRecycler.setVisibility(View.GONE);
    }

    //TODO: Qui si rende visibile la lista quando ci sono corsi
    private void disableNoCoursesUI(){
        noCoursesLabel.setVisibility(View.GONE);
        listaCorsiRecycler.setVisibility(View.VISIBLE);
    }

    private boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }


    public void removeItem(Corso c){
        RemoveAlert removeAlert = new RemoveAlert(this, c);
        removeAlert.show(getSupportFragmentManager(), "RemoveAlert");
    }

    public void removeItemFromMap(Corso c){
        Log.d(TAG, "removeItemFromMap: " + c);
        StringRequest postRequest = new StringRequest(
                Request.Method.DELETE,
                RestConstants.deleteCourseUrl("noncista", sp.readString("uid"), c.id),
                callbackDelete,
                callbackError);
        queue.add(postRequest);
    }

    private Response.Listener<String> callbackDelete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Long id = Long.parseLong(response);
            mRealm.beginTransaction();
            Corso c = mRealm.where(Corso.class).equalTo("id", id).findFirst();
            c.deleteFromRealm();
            mRealm.commitTransaction();
        }
    };

    public void openActivity(String corso, Long year){
        Intent intent = new Intent(this, AppelloOrStatistica.class);
        intent.putExtra(EXTRA_TEXT, corso);
        intent.putExtra(EXTRA_DATE, year);
        startActivity(intent);
    }

    public void changeNameItem(String nomeCorso, Long year, Long id){

        StringRequest postRequest = new StringRequest(
                Request.Method.PUT,
                RestConstants.updateCourseUrl("noncista", sp.readString("uid")),
                callbackUpdate,
                callbackError){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    Corso c = new Corso();
                    c.name = nomeCorso;
                    c.year = year;
                    c.id = id;
                    c.uid = sp.readString("uid");
                    Log.d(TAG, "getBody: "+ c);
                    return new Gson().toJson(c).getBytes();
                } catch (Exception e) {
                    Log.d(TAG, "getBody: " + e.toString());
                    return null;
                }
            }
        };
        queue.add(postRequest);
    }
    private Response.Listener<String> callbackUpdate = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject item = new JSONObject(response);
                    Corso course = new Gson().fromJson(item.toString(), Corso.class);
                    Log.d(TAG, "onResponse: " + course);
                    mRealm.beginTransaction();
                    mRealm.insertOrUpdate(course);
                    mRealm.commitTransaction();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public void openRenameAlert(Corso c){
        RenameAlert renameAlert = new RenameAlert(this, c);
        renameAlert.show(getSupportFragmentManager(), "RenameAlert");
    }


    public void openAddCourseAlert(View view){
        AddCourseAlert addCourseAlert = new AddCourseAlert();
        addCourseAlert.show(getSupportFragmentManager(), "alert");
    }


    private Response.Listener<String> callbackPost = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            mRealm.beginTransaction();
            try {
                JSONObject item = new JSONObject(response);
                Corso c = new Gson().fromJson(item.toString(), Corso.class);
                Log.e(TAG, c.toString());
                mRealm.copyToRealm(c);

            } catch (JSONException e) {
                Log.e(TAG, "JSON conversion failed");
                e.printStackTrace();
            }
            mRealm.commitTransaction();

        }
    };

    @Override
    public void getTextAndYear(String nomeCorso, int year) {
        Log.d(TAG, "getTextAndYear: "+ isOnline());
        if (isOnline()) {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    RestConstants.postCourseUrl("nono", sp.readString("uid")),
                    callbackPost,
                    callbackError) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        //Log.d(TAG, "getBody: " + buildPostFlightObject().toString());
                        Corso c = new Corso();
                        c.name = nomeCorso;
                        c.year = Long.valueOf(year);
                        c.uid = sp.readString("uid");
                        return new Gson().toJson(c).getBytes();
                    } catch (Exception e) {
                        Log.d(TAG, "getBody: " + e.toString());
                        return null;
                    }
                }
            };
            queue.add(postRequest);
        }
         else {
            mRealm.beginTransaction();

            Corso c = new Corso();
            c.name = nomeCorso;
            c.year = Long.valueOf(year);
            c.uid = sp.readString("uid");
            mRealm.insertOrUpdate(c);
            mRealm.commitTransaction();

        }

        //insertItem(position, name, year);
        Log.d(TAG, "getTextAndYear: " + year);
        Log.d("nomeC", "getText: " + nomeCorso);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listaCorsiRecycler.setAdapter(null);
        mRealm.close();
    }
}

