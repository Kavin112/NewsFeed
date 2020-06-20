package com.kavin.newsfeed;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kavin.newsfeed.General.General;
import com.kavin.newsfeed.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class SigIn extends AppCompatActivity {
///////
///// Layout

    RelativeLayout rellay1, rellay2;
    FirebaseAuth auth;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    Button bt_dangnhap, bt_dangki, bt_forgot;
    EditText et_user, et_pass;
    CheckBox checkRemember;

    FirebaseDatabase database;
    DatabaseReference table_user;

    String validUser = "[a-zA-Z0-9][a-zA-Z0-9\\-]{3,50}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_in);
        auth = FirebaseAuth.getInstance();

        ///////////////////// Animate Login layout ////////////////////////////////////
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 1500); //// timeout for the splash
        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////


        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        bt_dangnhap = findViewById(R.id.bt_dangnhap);
        checkRemember = findViewById(R.id.checkRemember);

        bt_dangki = findViewById(R.id.bt_dangki);


        ///// Init paper
        Paper.init(this);


        /// Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

        bt_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user3 = et_user.getText().toString();
                String user4 = et_pass.getText().toString();

                if (TextUtils.isEmpty(user3) || TextUtils.isEmpty(user4)) {

                    Toast.makeText(SigIn.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {



                    if (General.isConnectedtoInternet(getBaseContext())) {

                        ////// Save user + password ---> Remember me

                        if (checkRemember.isChecked()) {
                            Paper.book().write(General.USER_KEY, et_user.getText().toString());
                            Paper.book().write(General.PASS_KEY, et_pass.getText().toString());
                        }

                        final ProgressDialog mDialog = new ProgressDialog(SigIn.this);
                        mDialog.setTitle("Connecting");
                        mDialog.setMessage("Please waiting...");
                        mDialog.show();

                        Runnable progressRunnable = new Runnable() {

                            @Override
                            public void run() {
                                mDialog.cancel();
                            }
                        };

                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 2000);

                        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                String user1 = et_user.getText().toString();
                                String user2 = et_pass.getText().toString();
                                Matcher matcherUser = Pattern.compile(validUser).matcher(user1);


                                mDialog.dismiss();
                                auth.signInWithEmailAndPassword(user1, user2)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful())
                                                {


                                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    final DatabaseReference table_user = database.getReference("Users");
                                                    table_user.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                        {

                                                            User user = dataSnapshot.child(auth.getUid()).getValue(User.class);
                                                            General.currentUser = user;


                                                            Intent intent = new Intent(SigIn.this, Home.class);

                                                            startActivity(intent);
                                                            finish();

                                                            Toast.makeText(SigIn.this, "Welcome !!!", Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                                        {

                                                            Toast.makeText(SigIn.this, "Failed!!!", Toast.LENGTH_SHORT).show();


                                                        }
                                                    });




                                                } else {


                                                    Toast.makeText(SigIn.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Toast.makeText(SigIn.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }


        });



        bt_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SigIn.this, Register.class);
                startActivity(i);
                finish();
            }
        });


        ///////////// Check Remember
        String userRe = Paper.book().read(General.USER_KEY);
        String passRe = Paper.book().read(General.PASS_KEY);
        if (userRe != null && passRe != null) {
            {
                if (!userRe.isEmpty() && !passRe.isEmpty())
                    login(userRe, passRe);
            }
        }
    }



    ///////////////////// Check Remember ////////////////////////
    private void login(final String userRe, final String passRe) {
        if (General.isConnectedtoInternet(getBaseContext())) {

            /// Firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("Users");


            final android.app.AlertDialog mDialog = new SpotsDialog(SigIn.this);
            mDialog.show();



            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // Check User, if not user ---> exit database
                    if (dataSnapshot.child(auth.getUid()).exists())
                    {
                        // Lay thong tin User

                        mDialog.dismiss();
                        User user = dataSnapshot.child(auth.getUid()).getValue(User.class);

                            Intent i = new Intent(SigIn.this, Home.class);
                            General.currentUser = user;
                            startActivity(i);
                            finish();

                    } else {
                        Toast.makeText(SigIn.this, "User is not register !!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(SigIn.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }



    @Override
    public void onBackPressed()

    {
        AlertDialog.Builder builder=new AlertDialog.Builder(SigIn.this);
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




}
