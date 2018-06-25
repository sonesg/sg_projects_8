package com.example.korisnik.chatproject.registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.korisnik.chatproject.MainActivity;
import com.example.korisnik.chatproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout displayName;
    private TextInputLayout email;
    private TextInputLayout password;
    private Button createBtn;

    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    private DatabaseReference databaseReference;

    //ProgressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        displayName = (TextInputLayout) findViewById(R.id.reg_display_name);
        email = (TextInputLayout) findViewById(R.id.reg_email);
        password = (TextInputLayout) findViewById(R.id.reg_password);
        createBtn = (Button) findViewById(R.id.reg_create_btn);

        toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name_s = displayName.getEditText().getText().toString();
                String email_s = email.getEditText().getText().toString();
                String password_s = password.getEditText().getText().toString();

                //TODO check if internet permission is available
                if(isNetworkAvailable()) {
                    //Check if all of fields input are not empty
                    if(!TextUtils.isEmpty(display_name_s) || !TextUtils.isEmpty(email_s) || !TextUtils.isEmpty(password_s)){
                        progressDialog.setTitle("Registering user");
                        progressDialog.setMessage("Please wait while we create your account!");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        register_user(display_name_s, email_s, password_s);
                    }else{
                        Toast.makeText(RegisterActivity.this, "You must input all text fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Lose network or the connection is weak",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //check if network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void register_user(final String display_name_s, String email_s, String password_s) {
        mAuth.createUserWithEmailAndPassword(email_s, password_s)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Before dialog dismiss and the intent is startActivity
                            //we want to get the user unique id (uid)
                                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                //TODO  do that kind of checking if the unique user id is different that null if(currentUser != null)
                                if (currentUser != null) {
                                    String token_id = FirebaseInstanceId.getInstance().getToken();
                                    String uid = currentUser.getUid();
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                    //to add complex data we'll use hashMap
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("image", "https://www.w3schools.com/howto/img_avatar.png");
                                    userMap.put("name", display_name_s);
                                    userMap.put("status", "Hi, there.I'm using project app");
                                    userMap.put("thumb_image", "https://www.w3schools.com/howto/img_avatar.png");
                                    userMap.put("lastSeen", "Online");
                                    userMap.put("device_token", token_id);

                                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's informatio
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "task nije successful",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "currentUser is null",
                                            Toast.LENGTH_SHORT).show();
                                }
                        } else {
                            progressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Cannot register.Please check all forms in input fields!",
                                    Toast.LENGTH_SHORT).show();
                            String TAG = "FIREBASE_EXCEPTION";
                            //https://developers.google.com/identity/toolkit/web/configure-service do 5 tacke sam odradio
                            FirebaseException e = (FirebaseException)task.getException();
                            Log.d(TAG, "Reason: " + e.getMessage());
                        }
                    }
                });
    }
}
