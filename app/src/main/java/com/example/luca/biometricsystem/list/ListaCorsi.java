package com.example.luca.biometricsystem.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.luca.biometricsystem.AppelloOrStatistica;
import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.RemoveAlert;
import com.example.luca.biometricsystem.RenameAlert;
import com.example.luca.biometricsystem.entities.Corso;
import com.example.luca.biometricsystem.entities.Persona;
import com.example.luca.biometricsystem.login.ProvaAlert;
import com.example.luca.biometricsystem.utils.RestConstants;
import com.example.luca.biometricsystem.utils.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaCorsi extends AppCompatActivity implements ProvaAlert.ProvaAlertListener {

    public static final String EXTRA_TEXT = "com.example.luca.biometricsystem.list.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.luca.biometricsystem.list.EXTRA_DATE";

    private static final String TAG = "ListaCorsi";

    private Activity activity = this;
    private RecyclerView listaCorsiRecycler;
    private CorsoAdapter listaCorsiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CorsoItem> listaCorsi;

    private RequestQueue queue;

    private Realm mRealm;

    private final SharedPrefManager sp = new SharedPrefManager(this);


    //private String name;
    //ArrayList<ListItem> items;

    private TreeMap<DateItem, List<CorsoItem>> dateCourseMap = new TreeMap<>(new Comparator<DateItem>() {
        @Override
        public int compare(DateItem d0, DateItem d1) {
            //Log.d(TAG, "compare: " + (dt0.getYear() - t1.getYear()));
            return d1.getYear() - d0.getYear();
        }
    });


    private FloatingActionButton buttonInsert;

    private TextView noCoursesLabel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corsi);
        swipeRefreshLayout = findViewById(R.id.lista_corsi_refresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getColor(R.color.design_default_color_primary_dark));
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //inserire richiesta al server che ritorna la lista dei corsi
                //quando ha terminato la richiesta chiamare
                //swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "onRefresh: refresh");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        Persona persona = (Persona) getIntent().getSerializableExtra("Persona");

        queue = Volley.newRequestQueue(Objects.requireNonNull(this));
        noCoursesLabel = findViewById(R.id.noCoursesLabel);
        setButtons();


        if(isOnline()) {

            StringRequest postRequest = new StringRequest(
                    Request.Method.GET,
                    RestConstants.getAllCoursesByIdUrl("nessuno", sp.readString("uid")),
                    callbackGet,
                    errorGet);

            queue.add(postRequest);
        }
        buildRecyclerView();

        //Log.d(TAG, "onCreate: " + items);
        //listaCorsiAdapter.printlist();

    }
    public void buildRecyclerView(){
        listaCorsiRecycler = findViewById(R.id.lista_corsi);
        //listaCorsiRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRealm = Realm.getDefaultInstance();

        //DB dei voli
        final RealmResults<Corso> courses = mRealm.where(Corso.class).equalTo("uid", sp.readString("uid")).findAll();

        listaCorsiAdapter = new CorsoAdapter(this, courses);

        listaCorsiRecycler.setLayoutManager(layoutManager);


        courses.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Corso>>() {
            @Override
            public void onChange(@NonNull RealmResults<Corso> courses, @NonNull OrderedCollectionChangeSet changeSet) {
                if(courses.isEmpty()) enableNoFlightsUI();
                else disableNoFlightsUI();
            }
        });

        listaCorsiRecycler.setAdapter(listaCorsiAdapter);

        listaCorsiAdapter.setOnItemClickListener(new CorsoAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(Corso c) {
                //Corso corso = (Corso) v.getTag();
                openActivity(c.name,  c.year);
                //changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(Corso c) {
                Log.d(TAG, "onDeleteClick: " + c);
                removeItem(c);
            }

            @Override
            public void onRenameClick(View v) {
                openRenameItem(v);
            }
        });
    }


    private Response.ErrorListener errorGet= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.networkResponse != null) {
                Log.e(TAG, "onErrorResponse: errorGet: " + new String(error.networkResponse.data));
                Log.e(TAG, "onErrorResponse: errorGet: " + error.networkResponse.statusCode);
            } else{
                Log.e(TAG, "onErrorResponse: errorGet: " + error.getMessage());
            }
        }
    };

    private Response.Listener<String> callbackGet = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {

                JSONArray items = new JSONArray(response);
                //non ci sono voli
                if(items.length() == 0) {
                    enableNoFlightsUI();
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

    private void enableNoFlightsUI(){
        noCoursesLabel.setVisibility(View.VISIBLE);
        listaCorsiRecycler.setVisibility(View.GONE);
    }

    private void disableNoFlightsUI(){
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
        //removeItemFromMap(position);
    }

    private ListItem fromIndexToItem(int position){
        ArrayList<ListItem> items = new ArrayList<>();
        for(DateItem key : dateCourseMap.keySet()){
            items.add(key);
            for(CorsoItem corso : dateCourseMap.get(key)){
                items.add(corso);
            }
        }
        return items.get(position);
    }

    private Long toBeDeleted;

    public void removeItemFromMap(Corso c){
        Log.d(TAG, "removeItemFromMap: " + c);
        toBeDeleted = c.id;
        StringRequest postRequest = new StringRequest(
                Request.Method.DELETE,
                RestConstants.deleteCourseUrl("noncista", sp.readString("uid"), c.id),
                callbackDelete,
                errorDelete);
        queue.add(postRequest);
    }

    private Response.ErrorListener errorDelete = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if(error.networkResponse != null) {
                Log.e(TAG, "onErrorResponse: errorPost: " + new String(error.networkResponse.data));
                Log.e(TAG, "onErrorResponse: errorPost: " + error.networkResponse.statusCode);
            } else{
                Log.e(TAG, "onErrorResponse: errorPost: " + error.getMessage());
            }
        }
    };
    private Response.Listener<String> callbackDelete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            if(toBeDeleted != null){
                mRealm.beginTransaction();
                Corso c = mRealm.where(Corso.class).equalTo("id", toBeDeleted).findFirst();
                c.deleteFromRealm();
                mRealm.commitTransaction();
            }

        }
    };


    public void openActivity(String corso, Long year){ ;
        Intent intent = new Intent(this, AppelloOrStatistica.class);
        intent.putExtra(EXTRA_TEXT, corso);
        intent.putExtra(EXTRA_DATE, year);
        startActivity(intent);
    }

    public void changeNameItem(int position, String nomeCorso){
        if(fromIndexToItem(position).getType() == ListItem.TYPE_COURSE){
            CorsoItem corsoItem = (CorsoItem) fromIndexToItem(position);
            corsoItem.setNomeCorso(nomeCorso);
            listaCorsiAdapter.notifyItemChanged(position);
        }
    }

    public void openRenameItem(View v){
        RenameAlert renameAlert = new RenameAlert(this, v);
        renameAlert.show(getSupportFragmentManager(), "RenameAlert");
    }




    private Integer fromIndexToKey(int position) {
        int i = 0;
        for(DateItem key : dateCourseMap.keySet()){
            int k = 0;
            i++;
            for(CorsoItem corso : dateCourseMap.get(key)){
                if(i == position){
                    return key.getYear();
                }
                k++;
                i++;
            }
        }
        return null;
    }


    public void aggiungiCorso(View view){
        openDialog();
    }

    public void openDialog(){
        ProvaAlert provaAlert = new ProvaAlert();
        provaAlert.show(getSupportFragmentManager(), "alert");
    }

    public void setButtons(){
        buttonInsert = findViewById(R.id.button_insert);

        //editTextInsert = findViewById(R.id.edittext_insert);
        //editTextRemove = findViewById(R.id.edittext_remove);

        /*buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });*/
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
        //this.name = name;
        int position = 0;

        Log.d(TAG, "getTextAndYear: "+ isOnline());
        if (isOnline()) {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    RestConstants.postCourseUrl("nono", sp.readString("uid")),
                    callbackPost,
                    errorGet) {
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

