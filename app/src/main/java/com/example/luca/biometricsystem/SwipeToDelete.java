package com.example.luca.biometricsystem;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luca.biometricsystem.list.CorsoAdapter;
import com.example.luca.biometricsystem.list.ListaCorsi;

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback implements RemoveAlert.RemoveAlertListener {

    private static final String TAG = "SwipeToDelete";
    private CorsoAdapter mAdapter;
    private ListaCorsi listaCorsi;
    private Activity activity;
    private Drawable icon;
    private final ColorDrawable background;

    public SwipeToDelete(CorsoAdapter adapter, Activity activity, ListaCorsi listaCorsi){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        this.activity = activity;
        this.listaCorsi = listaCorsi;
        icon = ContextCompat.getDrawable(activity, R.drawable.delete);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        listaCorsi.removeItem(position);
        //openRemoveDialog();
    }

    public void undoDelete(){
        listaCorsi.undoDelete();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; // is used to push the background behind the edge of the parent view so that it appears underneath the rounded corners (as seen in the .gif below). The larger the corner radius of your view, the larger backgroundCornerOffset should be

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            Log.i("swipe", "left= "+iconLeft);
            int iconRight = itemView.getLeft() + iconMargin;
            Log.i("swipe", "right= "+iconRight);
            icon.setBounds(iconRight, iconTop, iconLeft, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            Log.i("swipe", "right= "+iconLeft);
            int iconRight = itemView.getRight() - iconMargin;
            Log.i("swipe", "right= "+iconRight);
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            //openRemoveDialog();
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
    }

    /*public void openRemoveDialog(){
        RemoveAlert removeAlert = new RemoveAlert(this);;
        removeAlert.show(listaCorsi.getSupportFragmentManager(), "RemoveAlert");
    }*/

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void removeCorso(boolean undo) {
        if(undo){
            listaCorsi.undoDelete();
        }
    }
}