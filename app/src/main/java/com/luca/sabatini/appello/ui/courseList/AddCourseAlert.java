package com.luca.sabatini.appello.ui.courseList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.luca.sabatini.appello.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class AddCourseAlert extends DialogFragment {

    private static final String TAG = "AddCourseAlert";
    private String title;
    private String namePositiveButton;
    private String nameNegativeButton;
    private CourseListFragment listaCorsiFragment;
    private ProvaAlertListener listener;

    public AddCourseAlert(CourseListFragment listaCorsiFragment){
        this.listaCorsiFragment = listaCorsiFragment;
        this.title = "Add course";
        this.nameNegativeButton = "Cancel";
        this.namePositiveButton = "Add";
    }

    int choosenYear = 2017;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_custom);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_add_corso, null);

        /// Spinner code

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = currentYear + 1;
        int prevYear = currentYear - 1;
        ArrayList<Integer> spinnerArray = new ArrayList<>(Arrays.asList(prevYear, currentYear, nextYear));

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>
                (this.getContext(), R.layout.spinner_item,
                        spinnerArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        builder.setView(view)
                .setTitle(title)
                //.setCustomTitle(createTitle())
        //builder.setMessage(Html.fromHtml(title))
                .setPositiveButton(namePositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: ciao");
                        EditText nomeCorso = view.findViewById(R.id.edit_text_nome_corso);
                        if(nomeCorso.getText().toString().equals("")) return;
                        Spinner spinner = view.findViewById(R.id.spinner);
                        listaCorsiFragment.getTextAndYear(nomeCorso.getText().toString(),
                                (Integer) spinner.getSelectedItem());
                        //setNomeCorso(name.getText().toString());
                    }
                })
                .setNegativeButton(nameNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("alert", "onClick: annulla");
                    }
                });
                //.setView(layoutInflater.inflate(R.layout.popup_add_corso, null));
        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

    private View createTitle(){
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        return textView;
    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{

            listener = (ProvaAlertListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }
    } */

    public interface ProvaAlertListener{
        void getTextAndYear(String nomeCorso, int year);
    }


}
