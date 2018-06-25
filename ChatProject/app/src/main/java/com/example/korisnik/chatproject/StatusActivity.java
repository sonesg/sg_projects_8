package com.example.korisnik.chatproject;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextInputLayout statusInput;
    private Button savedButton;

    //ProgressDialog
    private ProgressDialog progressDialog;

    //Firebase
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceOnline,databaseReferenceOnlineLastSeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        //get data from SettingsActivity,through intent
        String status_value = getIntent().getStringExtra("status_value");

        statusInput = (TextInputLayout) findViewById(R.id.status_input);
        savedButton = (Button) findViewById(R.id.status_change_btn);

        statusInput.getEditText().setText(status_value);

        //Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            databaseReferenceOnline = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnline.keepSynced(true);
            databaseReferenceOnlineLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnlineLastSeen.keepSynced(true);
        }


        toolbar = (Toolbar) findViewById(R.id.status_appBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get value from inputLayout
                String status = statusInput.getEditText().getText().toString();
                if(status.isEmpty()){
                    Toast.makeText(StatusActivity.this, "The edit input is empty.Please enter some text.", Toast.LENGTH_LONG).show();
                }else {
                    //Progress
                    progressDialog = new ProgressDialog(StatusActivity.this);
                    progressDialog.setTitle("Saving Changes");
                    progressDialog.setMessage("Please wait while we save the changes");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                //back to settings Activity
                                finish();
                            } else {
                                Toast.makeText(StatusActivity.this, "There are some error in saving changes", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            databaseReferenceOnline.child("online").setValue(true);
            databaseReferenceOnlineLastSeen.child("lastSeen").setValue("Online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            databaseReferenceOnline.child("online").setValue(false);
        }
    }
}
