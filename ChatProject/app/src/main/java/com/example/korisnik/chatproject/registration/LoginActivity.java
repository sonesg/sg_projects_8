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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout password;
    private Button loginBtn;

    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    private DatabaseReference databaseReference;

    //ProgressDialog
    private ProgressDialog progressDialog;

    private final static String TAG = "FIREBASE EXCEPTIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        email = (TextInputLayout) findViewById(R.id.log_email);
        password = (TextInputLayout) findViewById(R.id.log_password);
        loginBtn = (Button) findViewById(R.id.log_login_btn);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_s = email.getEditText().getText().toString();
                String password_s = password.getEditText().getText().toString();

                //TODO check if internet permission is available
                if(isNetworkAvailable()) {
                    //Check if all of fields input are not empty
                    if( !TextUtils.isEmpty(email_s) || !TextUtils.isEmpty(password_s)){
                        progressDialog.setTitle("Logging user");
                        progressDialog.setMessage("Please wait while we login your account!");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        login_user( email_s, password_s);
                    }else{
                        Toast.makeText(LoginActivity.this, "You must input all text fields",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Lose network or the connection is weak",
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

    private void login_user(String email_s, String password_s) {
        mAuth.signInWithEmailAndPassword(email_s, password_s)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();

                            String currentUser_id = mAuth.getCurrentUser().getUid();
                            String token_id = FirebaseInstanceId.getInstance().getToken();

                            databaseReference.child(currentUser_id).child("device_token").setValue(token_id)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    });
                        } else {
                            progressDialog.hide();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Cannot login in.Please check the form and try again.Email or Password are wrong!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
