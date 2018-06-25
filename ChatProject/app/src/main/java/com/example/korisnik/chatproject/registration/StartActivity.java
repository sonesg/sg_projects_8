package com.example.korisnik.chatproject.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.korisnik.chatproject.R;
import com.example.korisnik.chatproject.SlideOnlyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

public class StartActivity extends AppCompatActivity {

    private Button mRegBut,mLogBtn;
    private static final int INSTRUCTIONS_CODE = 231;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", true);

//        if(!getIntent().getBooleanExtra("first_launch", true)){
//            firstRun = false;
//        }
        if (firstRun) {
            // here run your first-time instructions, for example :
            startActivityForResult(new Intent(StartActivity.this, SlideOnlyActivity.class),
                    INSTRUCTIONS_CODE);
        }

            mRegBut = (Button) findViewById(R.id.create_a_account_start);
            mLogBtn = (Button) findViewById(R.id.login_start_btn);

            mRegBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
            mLogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
    }

    // when your InstructionsActivity ends, do not forget to set the firstRun boolean
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == INSTRUCTIONS_CODE) {
            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
        }
        finish();
    }
}
