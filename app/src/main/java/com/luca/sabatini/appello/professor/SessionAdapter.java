package com.luca.sabatini.appello.professor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Student;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.AttendanceHolder> {

    private ArrayList<Student> students;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SessionAdapter(ArrayList<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.presente_item, parent, false);
        return new AttendanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceHolder holder, int position) {
        holder.bind(students.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }

    public class AttendanceHolder extends RecyclerView.ViewHolder{

        public TextView matricola;
        public TextView cognome;
        public ImageView imageView;

        public AttendanceHolder(@NonNull View itemView) {
            super(itemView);
            matricola = itemView.findViewById(R.id.matricola);
            cognome = itemView.findViewById(R.id.nome_studente);
            imageView = itemView.findViewById(R.id.image_presente);
        }

        private void bind(Student student){
            matricola.setText("" + student.matricola);
            cognome.setText(student.surname);
            imageView.setImageResource(R.drawable.ic_correct);
        }
    }
}
