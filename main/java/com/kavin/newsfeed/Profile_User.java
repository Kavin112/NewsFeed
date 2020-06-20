package com.kavin.newsfeed;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kavin.newsfeed.AdapterView.User_Adapter_View;
import com.kavin.newsfeed.Display.ItemClickListener;
import com.kavin.newsfeed.General.General;
import com.kavin.newsfeed.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Profile_User extends AppCompatActivity implements  View.OnCreateContextMenuListener{

    Toolbar toolbar;
    ImageButton like, back;
    Button bt_changePass;

    TextView tv_name, tv_phone, tv_email, tv_nameUSER;


    FirebaseDatabase database;
    DatabaseReference profile;
    FirebaseStorage storage;
    StorageReference storageReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__user);


        ///// Init Firebase
        database = FirebaseDatabase.getInstance();
        profile = database.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_nameUSER = findViewById(R.id.tv_nameUSER);



        tv_name.setText(General.currentUser.getName());
        tv_phone.setText(General.currentUser.getPhone());
        tv_email.setText(General.currentUser.getEmail());
        tv_nameUSER.setText(General.currentUser.getName());
//        Picasso.with(getBaseContext()).load(General.currentUser.getImage())
//                .into(imageView2);




        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Profile_User.this, Home.class);
                startActivity(i);
                finish();
            }
        });




        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }





    private void showChangePasswordDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile_User.this);
        alertDialog.setTitle("Change Password !!!");
        alertDialog.setMessage("Fill old & new password: ");
        alertDialog.setIcon(R.drawable.ic_edit_black_24dp);

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_change = inflater.inflate(R.layout.change_password, null);



        final EditText ed_password = layout_change.findViewById(R.id.ed_password);
        final EditText ed_newPassword = layout_change.findViewById(R.id.ed_newPassword);
        final EditText ed_repeatPassword = layout_change.findViewById(R.id.ed_repeatPassword);

        alertDialog.setView(layout_change);

        /// Button
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /////// Dùng AlertDialog from android.app vì AlertDialog V7 đụng với library SpotsDialog
                final android.app.AlertDialog waitingDialog = new SpotsDialog(Profile_User.this);
                waitingDialog.show();

                if (ed_password.getText().toString().equals(General.currentUser.getPass())) {
                    if (ed_newPassword.getText().toString().equals(ed_repeatPassword.getText().toString())) {
                        Map<String, Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("pass", ed_newPassword.getText().toString());

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(General.currentUser.getUser())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDialog.dismiss();
                                        Toast.makeText(Profile_User.this, "Password was update !!!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile_User.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(Profile_User.this, "New Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    waitingDialog.dismiss();
                    Toast.makeText(Profile_User.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                }

            }
        });


        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        alertDialog.show();

    }


}



