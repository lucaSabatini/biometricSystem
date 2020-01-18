package com.luca.sabatini.appello.ui.courseList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.luca.sabatini.appello.ExitAlert;
import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;
import com.luca.sabatini.appello.professor.ProfessorMain;
import com.luca.sabatini.appello.utils.RestConstants;
import com.luca.sabatini.appello.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class CourseListFragment extends Fragment {


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

    private SharedPrefManager sp;

    private Context context = getContext();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista_corsi, container, false);
        context = root.getContext();
        queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        layoutManager = new LinearLayoutManager(getContext());
        mRealm = Realm.getDefaultInstance();

        swipeRefreshLayout = root.findViewById(R.id.lista_corsi_refresh);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getColor(R.color.design_default_color_primary_dark));
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: refresh");

                StringRequest postRequest = new StringRequest(
                        Request.Method.GET,
                        RestConstants.getAllCoursesByIdUrl("nessuno", sp.readFirebaseId()),
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

        noCoursesLabel = root.findViewById(R.id.noCoursesLabel);
        listaCorsiRecycler = root.findViewById(R.id.lista_corsi);
        FloatingActionButton buttonInsertFab = root.findViewById(R.id.button_insert);
        buttonInsertFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCourseAlert();
            }
        });
        sp = new SharedPrefManager(getContext());

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(sp.readExitIsChecked()) {
                    getActivity().finishAffinity();
                }
                else {
                    ExitAlert exitAlert = new ExitAlert(root.getContext(), getActivity());
                    exitAlert.show(getFragmentManager(), "ExitAlert");
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);


        buildRecyclerView();

        return root;
    }

    public void buildRecyclerView(){

        final RealmResults<Corso> courses = mRealm.where(Corso.class).equalTo("uid", sp.readFirebaseId()).findAll();

        listaCorsiAdapter = new CorsoAdapter(context, courses);

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
                sp.writeCorsoId(c.id);
                sp.writeNomeCorso(c.name);
                sp.writeAnnoCorso(c.year);

                startActivity(new Intent(context, ProfessorMain.class));
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
                    RestConstants.getAllCoursesByIdUrl("nessuno", sp.readFirebaseId()),
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
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        removeAlert.show(getFragmentManager(), "RemoveAlert");
    }

    public void removeItemFromMap(Corso c){
        Log.d(TAG, "removeItemFromMap: " + c);
        StringRequest postRequest = new StringRequest(
                Request.Method.DELETE,
                RestConstants.deleteCourseUrl("noncista", sp.readFirebaseId(), c.id),
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
        Intent intent = new Intent(getContext(), ProfessorMain.class);
        intent.putExtra(EXTRA_TEXT, corso);
        intent.putExtra(EXTRA_DATE, year);
        startActivity(intent);
    }

    public void changeNameItem(String nomeCorso, Long year, Long id){

        StringRequest postRequest = new StringRequest(
                Request.Method.PUT,
                RestConstants.updateCourseUrl("noncista", sp.readFirebaseId()),
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
                    c.uid = sp.readFirebaseId();
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
        renameAlert.show(getFragmentManager(), "RenameAlert");
    }


    public void openAddCourseAlert(){
        AddCourseAlert addCourseAlert = new AddCourseAlert(this);
        addCourseAlert.show(getFragmentManager(), "alert");
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

    public void getTextAndYear(String nomeCorso, int year) {
        Log.d(TAG, "getTextAndYear: "+ isOnline());
        if (isOnline()) {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    RestConstants.postCourseUrl("nono", sp.readFirebaseId()),
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
                        c.uid = sp.readFirebaseId();
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
            c.uid = sp.readFirebaseId();
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
