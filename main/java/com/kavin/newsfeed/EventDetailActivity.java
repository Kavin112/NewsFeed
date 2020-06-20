package com.kavin.newsfeed;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.newsfeed.Model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity
{

    private ImageView productImage;
    private TextView productPrice, productDescription, productName;
    private String productID  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        productID = getIntent().getStringExtra("eid");

        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);


        getProductDetails(productID);


    }



    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("event");
        productsRef.child(productID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getEventname());
                    productPrice.setText(products.getVenue());
                    productDescription.setText(products.getDescription());

                    Picasso.with(getBaseContext()).load(products.getImage())
                            .into(productImage);
                    //Picasso.get().load(products.getImage()).into(productImage);
                }
                else
                {
                    Toast.makeText(EventDetailActivity.this,"Iam kavin", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
