package com.kavin.newsfeed.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;

import com.kavin.newsfeed.Display.ItemClickListener;
import com.kavin.newsfeed.General.General;

public class User_Adapter_View extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener

{

    public ImageView iv_imageUser;

    private ItemClickListener itemClickListener;

    public User_Adapter_View(@NonNull View itemView) {
        super(itemView);



        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");

        contextMenu.add(0,0,getAdapterPosition(),General.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(),General.DELETE);
    }
}
