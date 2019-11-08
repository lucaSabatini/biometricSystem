package com.example.luca.biometricsystem.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luca.biometricsystem.R;
import com.example.luca.biometricsystem.entities.Corso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CorsoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CorsoAdapter";
    //private ArrayList<CorsoItem> listaCorso;
    private OnItemClickListener mListener;
    private CorsoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private ArrayList<ListItem> listItems;

    public interface OnItemClickListener{
        void onItemCLick(int position);
        void onDeleteClick(int position);
    }
/*
    public void deleteItem(int position) {
        mRecentlyDeletedItem = listaCorso.get(position);
        mRecentlyDeletedItemPosition = position;
        listaCorso.remove(position);
        notifyItemRemoved(position);
    }

 */


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

/*
    public CorsoAdapter(ArrayList<CorsoItem> listsCorso){
        this.listaCorso = listsCorso;
    }

 */

    public CorsoAdapter(ArrayList<ListItem> listItems){this.listItems = new ArrayList<>(listItems);}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ListItem.TYPE_COURSE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corso_item, parent, false);
            return new CorsoViewHolder(view, mListener);
        }
        else if (viewType == ListItem.TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(view);
        }
        else {
            Log.e(TAG, "onCreateViewHolder: Item type error RecyclerView");
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int type = listItems.get(position).getType();
        if(type == ListItem.TYPE_COURSE){
            CorsoItem corso = (CorsoItem) listItems.get(position);
            CorsoViewHolder holder = (CorsoViewHolder) viewHolder;
            holder.imageCorso.setImageResource(corso.getImageResource());
            holder.nomeCorso.setText(corso.getNomeCorso());
        }
        else if(type == ListItem.TYPE_HEADER){
            DateItem date = (DateItem) listItems.get(position);
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Log.d(TAG, "onBindViewHolder: "+date.getYear());
            holder.date.setText("" + date.getYear());

        }
        Log.d(TAG, "onBindViewHolder: " + listItems);
    }


    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    public void printlist(){
        Log.d(TAG, "printlist: " + listItems);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class CorsoViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageCorso;
        public TextView nomeCorso;


        public CorsoViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imageCorso = itemView.findViewById(R.id.image_corso);
            nomeCorso = itemView.findViewById(R.id.nome_corso);
            //deleteImage = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemCLick(position);
                        }
                    }
                }
            });

            /*deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });*/
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.headerTextView);
        }
    }
}
