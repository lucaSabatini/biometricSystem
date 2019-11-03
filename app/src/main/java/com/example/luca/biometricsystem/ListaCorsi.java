package com.example.luca.biometricsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.luca.biometricsystem.entities.Persona;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListaCorsi extends AppCompatActivity {

    private RecyclerView listaCorsiRecycler;
    private CorsoAdapter listaCorsiAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CorsoItem> listaCorsi;

    private FloatingActionButton buttonInsert;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_corsi);
        Persona persona = (Persona) getIntent().getSerializableExtra("Persona");

        createListaCorsi();
        buildRecyclerView();

        buttonInsert = findViewById(R.id.button_insert);
        //buttonRemove = findViewById(R.id.button_remove);
        setButtons();


        /*buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });*/



    }

    public void insertItem(int position){
        listaCorsi.add(position ,new CorsoItem(R.drawable.image_corso, "new corso"+position));
        listaCorsiAdapter.notifyItemInserted(position);
        listaCorsiRecycler.scrollToPosition(position);
    }

    public void removeItem(int position){
        listaCorsi.remove(position);
        listaCorsiAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text){
        listaCorsi.get(position).changeText1(text);
        listaCorsiAdapter.notifyItemChanged(position);
    }

    public void createListaCorsi(){
        listaCorsi = new ArrayList<>();
        listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Computer Vision"));
        listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Models of Computation"));
        listaCorsi.add(new CorsoItem(R.drawable.image_corso, "Mobile applications and cloud computing"));
    }

    public void buildRecyclerView(){
        listaCorsiRecycler = findViewById(R.id.lista_corsi);
        listaCorsiRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listaCorsiAdapter = new CorsoAdapter(listaCorsi);

        listaCorsiRecycler.setLayoutManager(layoutManager);
        listaCorsiRecycler.setAdapter(listaCorsiAdapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(listaCorsiAdapter, this));
        itemTouchHelper.attachToRecyclerView(listaCorsiRecycler);
        listaCorsiAdapter.setOnItemClickListener(new CorsoAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void aggiungiCorso(View view){
        int position = listaCorsi.size();
        insertItem(position);
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
}
