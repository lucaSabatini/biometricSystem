package com.luca.sabatini.appello.ui.courseList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.luca.sabatini.appello.R;
import com.luca.sabatini.appello.entities.Corso;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CorsoAdapter extends RealmRecyclerViewAdapter<Corso, CorsoAdapter.CorsoViewHolder> {

    private static final String TAG = "CorsoAdapter";
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemCLick(Corso c);
        void onDeleteClick(Corso c);
        void onRenameClick(Corso c);
    }

    public CorsoAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Corso> data) {
        super(context, data, true);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public CorsoAdapter.CorsoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corso_item, parent, false);
        return new CorsoViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CorsoAdapter.CorsoViewHolder viewHolder, int position) {
        viewHolder.bind(getItem(position));
    }

    public static class CorsoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageCorso;
        public TextView annoCorso;
        public TextView nomeCorso;
        public ImageButton trePunti;

        public void bind(Corso corso) {
            imageCorso.setImageResource(R.drawable.image_corso);
            nomeCorso.setText("" + corso.name);
            nomeCorso.setTag(corso);
            annoCorso.setText("" + corso.year);
        }

        public CorsoViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageCorso = itemView.findViewById(R.id.image_corso);
            nomeCorso = itemView.findViewById(R.id.nome_corso);
            trePunti = itemView.findViewById(R.id.tre_punti);
            annoCorso = itemView.findViewById(R.id.anno_corso);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCLick((Corso) nomeCorso.getTag());
                        }
                    }
                }
            });

            trePunti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), trePunti);

                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (listener != null) {
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    switch (item.getItemId()) {
                                        case R.id.renameCorso:
                                            listener.onRenameClick((Corso) nomeCorso.getTag());
                                            return true;
                                        case R.id.deleteCorso:

                                            Log.d(TAG, "onMenuItemClick:"+ view.findViewById(R.id.nome_corso));
                                            listener.onDeleteClick((Corso) nomeCorso.getTag());
                                            return true;
                                        default:
                                            return false;
                                    }
                                    //listener.onDeleteClick(position);
                                }
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });


        }
    }

}
