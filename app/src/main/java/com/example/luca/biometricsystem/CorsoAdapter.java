package com.example.luca.biometricsystem;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CorsoAdapter extends RecyclerView.Adapter<CorsoAdapter.CorsoViewHolder> {
    private ArrayList<CorsoItem> listaCorso;
    private OnItemClickListener mListener;
    private CorsoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public interface OnItemClickListener{
        void onItemCLick(int position);
        void onDeleteClick(int position);
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = listaCorso.get(position);
        mRecentlyDeletedItemPosition = position;
        listaCorso.remove(position);
        notifyItemRemoved(position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
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

    public CorsoAdapter(ArrayList<CorsoItem> listsCorso){
        this.listaCorso = listsCorso;
    }

    @NonNull
    @Override
    public CorsoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.corso_item, parent, false);
        CorsoViewHolder corsoViewHolder = new CorsoViewHolder(view, mListener);
        return corsoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CorsoViewHolder holder, int position) {
        CorsoItem currentItem = listaCorso.get(position);

        holder.imageCorso.setImageResource(currentItem.getImageResource());
        holder.nomeCorso.setText(currentItem.getNomeCorso());
    }

    @Override
    public int getItemCount() {
        return listaCorso.size();
    }
}
