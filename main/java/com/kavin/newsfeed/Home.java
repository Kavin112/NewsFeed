package com.kavin.newsfeed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kavin.newsfeed.General.General;
import com.kavin.newsfeed.Model.Details;
import com.kavin.newsfeed.Model.Products;
import com.kavin.newsfeed.Model.User;

import com.kavin.newsfeed.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    FirebaseDatabase database;
    DatabaseReference category;

    TextView tv_Username;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;



    FirebaseUser firebaseUser;
    DatabaseReference reference;
    SwipeRefreshLayout swipeRefreshLayout;


    private DatabaseReference ProductsRef;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("event");



        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        /////////////Bottom Nav//////////////

        ////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("News Feed");
        setSupportActionBar(toolbar);


        ///// Swipe
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (General.isConnectedtoInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(Home.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (General.isConnectedtoInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(Home.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


        ////// Paper
        Paper.init(this);


        //// Firebase:

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Details");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, AddEventActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();






        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        tv_Username = headerView.findViewById(R.id.tv_Username);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_Username.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tv_Username.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ////// Load Menu
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Animation controller = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.item_animation_fall_down);
        recyclerView.setAnimation(controller);




        if (General.isConnectedtoInternet(this))
            loadMenu();
        else {
            Toast.makeText(this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
            return;
        }


    }




    private void loadMenu()
    {
            FirebaseRecyclerOptions<Products> options= new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef, Products.class).build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options)
            {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                {
                    holder.txtProductName.setText(model.getEventname());

                    Picasso.with(getBaseContext()).load(model.getImage())
                            .into(holder.imageView);


                    holder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            Intent intent = new Intent(Home.this,EventDetailActivity.class);
                            intent.putExtra("eid", model.getEid());
                            startActivity(intent);

                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_items_layout, viewGroup, false);
                    ProductViewHolder holder = new ProductViewHolder(view);
                    return holder;
                }
            };

        recyclerView.setAdapter(adapter);
            adapter.startListening();
        swipeRefreshLayout.setRefreshing(false);



        }


    @Override
    public void onBackPressed()
    {
        android.app.AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
        builder.setMessage("Really Exit ??");
        builder.setTitle("Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok",new MyListener());
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
    public class MyListener implements DialogInterface.OnClickListener
    {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_homepage) {
            loadMenu();
            return true;
        }

        if (id == R.id.action_logout) {
            /////// Delete Remember user+password
            Paper.book().destroy();

            ///// Logout
            Intent signIn = new Intent(Home.this, SigIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
   }

        else if (id == R.id.nav_user) {
            Intent i = new Intent(Home.this, Profile_User.class);
            startActivity(i);
        }

        else if (id == R.id.nav_logout) {

            /////// Delete Remember user+password
            Paper.book().destroy();

            ///// Logout
            Intent signIn = new Intent(Home.this, SigIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


}
