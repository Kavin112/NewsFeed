package com.kavin.newsfeed.ViewHolder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kavin.newsfeed.Interface.ItemClickListener;
import com.kavin.newsfeed.R;


public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName;
    public ImageView imageView;
    public ItemClickListener listener;


    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }


    @Override
    public void onClick(View v)
    {
        listener.onClick(v, getAdapterPosition(),false);


    }
}
