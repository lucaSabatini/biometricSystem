package com.example.luca.biometricsystem.list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.luca.biometricsystem.AppelloOrStatistica;
import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.RemoveAlert;
import com.example.luca.biometricsystem.RenameAlert;
import com.example.luca.biometricsystem.SwipeToDelete;
import com.example.luca.biometricsystem.entities.Corso;
import com.example.luca.biometricsystem.entities.Persona;
import com.example.luca.biometricsystem.login.ProvaAlert;
import com.example.luca.biometricsystem.utils.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class ListaCorsi extends AppCompatActivity implements ProvaAlert.ProvaAlertListener {

    public static final String EXTRA_TEXT = "com.example.luca.biometricsystem.list.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.luca.biometricsystem.list.EXTRA_DATE";

    private static final String TAG = "ListaCorsi";

    private Activity activity = this;
    private RecyclerView listaCorsiRecycler;
    private CorsoAdapter listaCorsiAdapter;

    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CorsoItem> listaCorsi;

    private final SharedPrefManager sp = new SharedPrefManager(this);
    //private String nomeCorso;
    //ArrayList<ListItem> items;

    private TreeMap<DateItem, List<CorsoItem>> dateCourseMap = new TreeMap<>(new Comparator<DateItem>() {
        @Override
        public int compare(DateItem d0, DateItem d1) {
            //Log.d(TAG, "compare: " + (dt0.getYear() - t1.getYear()));
            return d1.getYear() - d0.getYear();
        }
    });

    private TreeMap<DateItem, List<CorsoItem>> mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    private FloatingActionButton buttonInsert;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corsi);
        Persona persona = (Persona) getIntent().getSerializableExtra("Persona");

        setButtons();

        creaItems();
        createListaCorsi();
        buildRecyclerView();

        //Log.d(TAG, "onCreate: " + items);
        //listaCorsiAdapter.printlist();

    }


    public void insertItem(int position, String nomeCorso, int year){
        Log.d(TAG, "insertItem: " + position);
        Toast.makeText(this, sp.readString("uid"),Toast.LENGTH_LONG);
        DateItem d = new DateItem(year);
        if(dateCourseMap.containsKey(d)){
            dateCourseMap.get(d).add(new CorsoItem(new Corso(nomeCorso), R.drawable.image_corso));
            listaCorsiAdapter.notifyItemInserted(position);
        } else {
            Log.d(TAG, "insertItem: " + year);
            //items.add(position, new DateItem(year));
            //position++;
            dateCourseMap.put(new DateItem(year), new ArrayList<>(Arrays.asList(new CorsoItem(new Corso(nomeCorso), R.drawable.image_corso))));
            listaCorsiAdapter.notifyItemRangeInserted(position, 2);
        }
        Log.d(TAG, "insertItem: " + position);
        //items.add(position, new CorsoItem(new Corso(nomeCorso), R.drawable.image_corso));

        listaCorsiRecycler.scrollToPosition(position);
    }

    public void removeItem(int position){
        RemoveAlert removeAlert = new RemoveAlert(this, position);
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

    public void removeItemFromMap(int position){
        int i = 0;
        for(DateItem key : dateCourseMap.keySet()){
            int k = 0;
            i++;
            for(CorsoItem corso : dateCourseMap.get(key)){
                if(i == position){
                    Log.d(TAG, "removeItemFromMap: " + dateCourseMap);
                    if( dateCourseMap.get(key).size() == 1){
                        dateCourseMap.remove(key);
                        Log.d(TAG, "removeItemFromMap: " + dateCourseMap);
                        listaCorsiAdapter.notifyItemRangeRemoved(position - 1, 2);
                        return;
                    }
                    dateCourseMap.get(key).remove(k);
                    listaCorsiAdapter.notifyItemRemoved(position);
                    return;
                }
                k++;
                i++;
            }

        }
    }


    public void undoDelete(){
        //listaCorsi.add(mRecentlyDeletedItemPosition,mRecentlyDeletedItem);
        //listaCorsiAdapter.notifyItemInserted(mRecentlyDeletedItemPosition);
        dateCourseMap = mRecentlyDeletedItem;
        listaCorsiAdapter.notifyDataSetChanged();
    }

    public void openActivity(String nomeCorso, Integer date){
        //Log.d(TAG, "openActivity: "+ date.toString());
        //listaCorsiAdapter.notifyItemChanged(position);
        //String nomeCorso= listaCorsi.get(position-1).toString();
        Intent intent = new Intent(this, AppelloOrStatistica.class);
        intent.putExtra(EXTRA_TEXT, nomeCorso);
        intent.putExtra(EXTRA_DATE, date);
        startActivity(intent);
    }

    public void changeNameItem(int position, String nomeCorso){
        if(fromIndexToItem(position).getType() == ListItem.TYPE_COURSE){
            CorsoItem corsoItem = (CorsoItem) fromIndexToItem(position);
            corsoItem.setNomeCorso(nomeCorso);
            listaCorsiAdapter.notifyItemChanged(position);
        }
    }

    public void openRenameItem(int position){
        RenameAlert renameAlert = new RenameAlert(this, position);
        renameAlert.show(getSupportFragmentManager(), "RenameAlert");
    }

    public void createListaCorsi(){
        listaCorsi = new ArrayList<>();

    }

    public void creaItems(){
        ArrayList<CorsoItem> corsi = new ArrayList<>();
        corsi.add(new CorsoItem(new Corso("Fondamenti di Programmazione"), R.drawable.image_corso));
        corsi.add(new CorsoItem(new Corso("Computer Grafica"), R.drawable.image_corso));
        ArrayList<CorsoItem> corsi2 = new ArrayList<>();
        corsi2.add(new CorsoItem(new Corso("Computer Vision"),R.drawable.image_corso));
        dateCourseMap.put(new DateItem(2018), corsi);
        dateCourseMap.put(new DateItem(2019), corsi2);
        Log.d(TAG, "creaItems: " + dateCourseMap);
    }

    public void buildRecyclerView(){
        listaCorsiRecycler = findViewById(R.id.lista_corsi);
        //listaCorsiRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        listaCorsiAdapter = new CorsoAdapter(dateCourseMap);

        listaCorsiRecycler.setLayoutManager(layoutManager);
        listaCorsiRecycler.setAdapter(listaCorsiAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(listaCorsiAdapter, this, this));
        itemTouchHelper.attachToRecyclerView(listaCorsiRecycler);
        listaCorsiAdapter.setOnItemClickListener(new CorsoAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(String nomeCorso, int position) {
                openActivity(nomeCorso, fromIndexToKey(position));
                //changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }

            @Override
            public void onRenameClick(int position) {
                openRenameItem(position);
            }
        });
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

    @Override
    public void getTextAndYear(String nomeCorso, int year) {
        //this.nomeCorso = nomeCorso;
        int position = 0;
        DateItem yearItem = new DateItem(year);
        if(dateCourseMap.containsKey(yearItem)){
            for(DateItem key: dateCourseMap.keySet()){
                if(key.getYear() >= year ) {
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
