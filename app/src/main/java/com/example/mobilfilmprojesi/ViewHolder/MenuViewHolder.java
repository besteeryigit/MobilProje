package com.example.mobilfilmprojesi.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilfilmprojesi.Common.Common;
import com.example.mobilfilmprojesi.Interface.ItemclickListener;
import com.example.mobilfilmprojesi.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    public TextView txtMenuName;
    public ImageView imageView;
    private ItemclickListener itemclickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.menu_image);
        itemView.setOnCreateContextMenuListener(this);
       itemView.setOnClickListener(this);

    }


    public void setItemclickListener(ItemclickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }

    @Override
    public void onClick(View v) {

        itemclickListener.onClick(v,getAdapterPosition(),false);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("İşlemi Seçiniz");
        menu.add(0,1,getAdapterPosition(),Common.DELETE);
    }
}
