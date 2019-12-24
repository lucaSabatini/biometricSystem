package com.luca.sabatini.appello.professor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Student;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class SessionAdapter extends RealmRecyclerViewAdapter<Student, SessionAdapter.AttendanceHolder> {

    public SessionAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Student> data) {
        super(context, data, true);
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.presente_item, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class AttendanceHolder extends RecyclerView.ViewHolder{

        public TextView matricola;
        public TextView cognome;

        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            matricola = itemView.findViewById(R.id.matricola);
            cognome = itemView.findViewById(R.id.nome_studente);
        }

        private void bind(Student student){
            matricola.setText("" + student.matricola);
            cognome.setText(student.surname);

        }
    }
}
