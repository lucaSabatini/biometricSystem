package com.example.luca.biometricsystem.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.luca.biometricsystem.MainActivity;
import com.example.luca.biometricsystem.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class ProvaAlert extends DialogFragment {

    private String title;
    private String namePositiveButton;
    private String nameNegativeButton;
    private ProvaAlertListener listener;

    public ProvaAlert(){
        this.title = "Nuovo corso";
        this.nameNegativeButton = "Annulla";
        this.namePositiveButton = "Aggiungi";
    }

    int choosenYear = 2017;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.color_button);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_add_corso, null);

        /// Spinner code

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int nextYear = currentYear + 1;
        ArrayList<Integer> spinnerArray = new ArrayList<>(Arrays.asList(2018, currentYear, nextYear));

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>
                (this.getContext(), android.R.layout.simple_spinner_item,
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
                        listener.getTextAndYear(nomeCorso.getText().toString(),
                                (Integer) spinner.getSelectedItem());
                        //setNomeCorso(nomeCorso.getText().toString());
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
        /*alertDialog.setT
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.design_default_color_primary_dark));
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.design_default_color_primary_dark));
            }
        });*/

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (ProvaAlertListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "implement ProvaAlertListener");
        }
    }

    public interface ProvaAlertListener{
        void getTextAndYear(String nomeCorso, int year);
    }


}
