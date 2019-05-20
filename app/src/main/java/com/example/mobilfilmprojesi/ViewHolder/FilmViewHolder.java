package com.example.mobilfilmprojesi.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Interface.ItemclickListener;
import com.example.mobilfilmprojesi.R;

public class FilmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtFilmName,txtTeknik;
        public ImageButton fav_button;
    public ImageView film_image;
    private ItemclickListener itemclickListener;

    public void setItemclickListener(ItemclickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }

    public FilmViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFilmName=(TextView)itemView.findViewById(R.id.film_name);
        txtTeknik=(TextView)itemView.findViewById(R.id.film_teknikbilgi);
        film_image=(ImageView)itemView.findViewById(R.id.film_image);
        itemView.setOnClickListener(this);

        itemView.setOnCreateContextMenuListener(this);

    }





    @Override
    public void onClick(View v) {
        itemclickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("İşlemi seçiniz");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
