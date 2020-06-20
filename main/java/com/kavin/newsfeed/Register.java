package com.kavin.newsfeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kavin.newsfeed.General.General;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    //    MaterialEditText et_user, et_fullname,et_pass, et_phone,et_email;
    EditText et_user, et_fullname, et_pass, et_phone, et_email, et_image,et_status,et_id;
    Button bt_dangki, bt_dangnhap;
    FirebaseAuth auth;



    String validEmail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
            + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
            + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    String validPass = "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}";
    String validName = "^[\\p{L} .'-]+$";
    String validPhone = "([6-9][0-9]|[0-9][0-9][[0-9]|[0-9]|[0-9]|[0-9]])+([0-9][0-9])\\b";
    String validUser = "[a-zA-Z0-9][a-zA-Z0-9\\-]{3,50}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        et_fullname = findViewById(R.id.et_fullname);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_image = findViewById(R.id.et_image);
        et_status = findViewById(R.id.et_status);
        et_id = findViewById(R.id.et_id);


        bt_dangki = findViewById(R.id.bt_dangki);
        bt_dangnhap = findViewById(R.id.bt_dangnhap);


        auth = FirebaseAuth.getInstance();


        /// Firebase
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         final DatabaseReference table_user = database.getReference("Users");


        bt_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (General.isConnectedtoInternet(getBaseContext()))
                {


                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
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
                    pdCanceller.postDelayed(progressRunnable, 3000);

                    table_user.addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
//// Validate email
                            String email = et_email.getText().toString();
                            Matcher matcherEmail = Pattern.compile(validEmail).matcher(email);
//// Validate pass
                            String pass = et_pass.getText().toString();
                            Matcher matcherPass = Pattern.compile(validPass).matcher(pass);
//// Validate Name
                            String name = et_fullname.getText().toString();
                            Matcher matcherName = Pattern.compile(validName).matcher(name);
//// Validate Phone
                            String phone = et_phone.getText().toString();
                            Matcher matcherPhone = Pattern.compile(validPhone).matcher(phone);
//// Validate User
                            String user1 = et_user.getText().toString();
                            Matcher matcherUser = Pattern.compile(validUser).matcher(user1);

                            if (matcherUser.matches())
                            {
                                if (matcherPass.matches())
                                {
                                    if (matcherName.matches())
                                    {
                                        if (matcherPhone.matches())
                                        {
                                            if (matcherEmail.matches())
                                            {
///Check add new accoun
                                                auth.createUserWithEmailAndPassword(email, pass)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                       if (task.isSuccessful()) {
                                                                                           FirebaseUser firebaseUser = auth.getCurrentUser();
                                                                                           assert firebaseUser != null;
                                                                                           final String userid = firebaseUser.getUid();
                                                                                           //et_id.setText(auth.getUid());

                                                                                           DatabaseReference table_userd = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                                                                                           HashMap<String, String> hashMap = new HashMap<>();
                                                                                           hashMap.put("id", userid);
                                                                                           // hashMap.put("username", username);
                                                                                           //hashMap.put("imageURL", "default");
                                                                                           hashMap.put("status", "offline");
                                                                                           hashMap.put("search", et_user.getText().toString().toLowerCase());
                                                                                           hashMap.put("email", et_email.getText().toString());
                                                                                           hashMap.put("image", "default");
                                                                                           hashMap.put("name", et_fullname.getText().toString());
                                                                                           hashMap.put("user", et_user.getText().toString());
                                                                                           hashMap.put("pass", et_pass.getText().toString());
                                                                                           hashMap.put("phone", et_phone.getText().toString());

                                                                                           table_userd.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                               @Override
                                                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                                                   if (task.isSuccessful()) {

                                                                                                       mDialog.dismiss();


                                                                                                       Toast.makeText(Register.this, "Register successfully !!!", Toast.LENGTH_SHORT).show();
                                                                                                       Intent intent = new Intent(Register.this, SigIn.class);
                                                                                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                       startActivity(intent);
                                                                                                       finish();
                                                                                                   }
                                                                                               }

                                                                                           });
                                                                                       }
                                                                                   }
                                                                               });








                                            } else {
                                                Toast.makeText(Register.this, "Invalid Email !!!", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(Register.this, "Invalid Phone number !!!.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this, "Invalid Your Name !!!", Toast.LENGTH_SHORT).show(); }
                                }
                                else {
                                    Toast.makeText(Register.this, "Invalid Password !!!", Toast.LENGTH_SHORT).show(); }

                            }
                            else {
                                Toast.makeText(Register.this, "Invalid Username !!!", Toast.LENGTH_SHORT).show();}




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(Register.this, "Check your connection !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });


    }




    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");



    @Override
    public void onBackPressed () {
        Intent intent = new Intent(Register.this, SigIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}





