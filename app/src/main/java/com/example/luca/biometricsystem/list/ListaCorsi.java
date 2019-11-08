package com.example.luca.biometricsystem.list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.SwipeToDelete;
import com.example.luca.biometricsystem.entities.Corso;
import com.example.luca.biometricsystem.entities.Persona;
import com.example.luca.biometricsystem.login.ProvaAlert;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class ListaCorsi extends AppCompatActivity implements ProvaAlert.ProvaAlertListener {
    private static final String TAG = "ListaCorsi";
    private Activity activity = this;
    private RecyclerView listaCorsiRecycler;
    private CorsoAdapter listaCorsiAdapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CorsoItem> listaCorsi;
    //private String nomeCorso;
    ArrayList<ListItem> items;

    private TreeMap<Integer, List<Corso>> dateCourseMap = new TreeMap<>(Collections.reverseOrder());

    private CorsoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;


    private FloatingActionButton buttonInsert;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corsi);
        Persona persona = (Persona) getIntent().getSerializableExtra("Persona");
        creaItems();
        createListaCorsi();
        buildRecyclerView();

        buttonInsert = findViewById(R.id.button_insert);
        //buttonRemove = findViewById(R.id.button_remove);
        setButtons();

        Log.d(TAG, "onCreate: " + items);
        listaCorsiAdapter.printlist();

        /*buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });*/



    }


    public void insertItem(int position, String nomeCorso, int year){
        //listaCorsi.add(position ,new CorsoItem(R.drawable.image_corso, nomeCorso + " " + year));
        //dateCourseMap.put(year, nomeCorso);
        //ArrayList<C> key = dateCourseMap.get(year);
        Log.d(TAG, "insertItem: " + position);
        if(dateCourseMap.containsKey(year)){
            dateCourseMap.get(year).add(new Corso(nomeCorso));
        } else {
            Log.d(TAG, "insertItem: " + year);
            items.add(position, new DateItem(year));
            position++;
            dateCourseMap.put(year, new ArrayList<>(Arrays.asList(new Corso(nomeCorso))));
        }
        Log.d(TAG, "insertItem: " + position);
        items.add(position, new CorsoItem(new Corso(nomeCorso), R.drawable.image_corso));
        listaCorsiAdapter.notifyItemInserted(position);
        listaCorsiRecycler.scrollToPosition(position);
    }

    public void removeItem(int position){
        mRecentlyDeletedItem = listaCorsi.get(position);
        mRecentlyDeletedItemPosition = position;
        listaCorsi.remove(position);
        listaCorsiAdapter.notifyItemRemoved(position);
    }

    public void undoDelete(){
        listaCorsi.add(mRecentlyDeletedItemPosition,mRecentlyDeletedItem);
        listaCorsiAdapter.notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public void changeItem(int position, String text){
        //listaCorsi.get(position).changeText1(text);
        listaCorsiAdapter.notifyItemChanged(position);
    }

    public void createListaCorsi(){
        listaCorsi = new ArrayList<>();
        //listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Computer Vision"));
        //listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Models of Computation"));
        //listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Mobile applications and cloud computing"));
    }

    public void creaItems(){

        ArrayList<Corso> corsi = new ArrayList<>();
        corsi.add(new Corso("Fondamenti di Programmazione"));
        corsi.add(new Corso("Computer Grafica"));
        ArrayList<Corso> corsi2 = new ArrayList<>();
        corsi2.add(new Corso("Computer Vision"));
        corsi2.add(new Corso("Sistemi Biometrici"));
        dateCourseMap.put(2018, corsi);
        dateCourseMap.put(2019, corsi2);
        Log.d(TAG, "creaItems: " + dateCourseMap);
    }

    public void buildRecyclerView(){
        listaCorsiRecycler = findViewById(R.id.lista_corsi);
        //listaCorsiRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        items = new ArrayList();
        for(Integer year: dateCourseMap.keySet()){
            items.add(new DateItem(year));
            for(Corso course: dateCourseMap.get(year)){
                items.add(new CorsoItem(course, R.drawable.image_corso));
            }
        }

        listaCorsiAdapter = new CorsoAdapter(items);

        listaCorsiRecycler.setLayoutManager(layoutManager);
        listaCorsiRecycler.setAdapter(listaCorsiAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(listaCorsiAdapter, this, this));
        itemTouchHelper.attachToRecyclerView(listaCorsiRecycler);
        /*listaCorsiAdapter.setOnItemClickListener(new CorsoAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });*/
    }



    public void aggiungiCorso(View view){
        openDialog();
    }

    public void openDialog(){
        ProvaAlert provaAlert = new ProvaAlert();
        provaAlert.show(getSupportFragmentManager(), "alert");
    }

    public void setButtons(){
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

    @Override
    public void getTextAndYear(String nomeCorso, int year) {
        //this.nomeCorso = nomeCorso;
        int position = 0;
        if(dateCourseMap.containsKey(year)){
            for(Integer key: dateCourseMap.keySet()){
                if(key >= year ) {
                    Log.d(TAG, "getTextAndYear: " + key);
                    Log.d(TAG, "getTextAndYear: " + dateCourseMap.get(key));
                    position = position + dateCourseMap.get(key).size() + 1;
                    Log.d(TAG, "getTextAndYear: " + dateCourseMap.get(key).size());
                }
            }
        }
        insertItem(position, nomeCorso, year);
        Log.d(TAG, "getTextAndYear: " + year);
        Log.d("nomeC", "getText: " + nomeCorso);
    }
}
