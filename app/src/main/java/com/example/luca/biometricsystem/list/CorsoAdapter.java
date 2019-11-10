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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

public class CorsoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CorsoAdapter";
    //private ArrayList<CorsoItem> listaCorso;
    private OnItemClickListener mListener;
    private CorsoItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    //private ArrayList<ListItem> listItems;

    private TreeMap<DateItem,List<CorsoItem>> dateCourseMap;

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

    //public CorsoAdapter(ArrayList<ListItem> listItems){this.listItems = new ArrayList<>(listItems);}

    public CorsoAdapter(TreeMap<DateItem, List<CorsoItem>> dateCourseMap){ this.dateCourseMap = dateCourseMap;}

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
        ListItem item = fromIndexToItem(position);
        int type = item.getType();
        if(type == ListItem.TYPE_COURSE){
            CorsoItem corso = (CorsoItem) item;
            CorsoViewHolder holder = (CorsoViewHolder) viewHolder;
            holder.imageCorso.setImageResource(corso.getImageResource());
            holder.nomeCorso.setText(corso.getNomeCorso());
        }
        else if(type == ListItem.TYPE_HEADER){
            DateItem date = (DateItem) item;
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Log.d(TAG, "onBindViewHolder: "+date.getYear());
            holder.date.setText("" + date.getYear());

        }
        Log.d(TAG, "onBindViewHolder: " + item);
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

    @Override
    public int getItemViewType(int position) {
        return fromIndexToItem(position).getType();
    }



    @Override
    public int getItemCount() {
        int size = 0;
        for(DateItem key : dateCourseMap.keySet()) {
            size += dateCourseMap.get(key).size() + 1;
        }
        return size;
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
