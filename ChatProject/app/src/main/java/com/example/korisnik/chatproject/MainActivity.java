package com.example.korisnik.chatproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.korisnik.chatproject.registration.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager viewPager;
    private SelectionPagerAdapter selectionPagerAdapter;

    private DatabaseReference databaseReference,databaseReferenceLastSeen;

    private DatabaseReference checkDatabase;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat Project");

        //Tabs
        viewPager = (ViewPager) findViewById(R.id.tabPager);
        selectionPagerAdapter = new SelectionPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(selectionPagerAdapter);
        viewPager.setOffscreenPageLimit(2);//da bi podaci bili synced medjusobno u viewPager-u

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        Log.i("RADINOST", mAuth.getCurrentUser() + "");

        if (mAuth.getCurrentUser() != null) {

            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReference.keepSynced(true);
            databaseReferenceLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceLastSeen.keepSynced(true);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToStart(true);
        } else {
            databaseReference.child("online").setValue(true);
            databaseReferenceLastSeen.child("lastSeen").setValue("Online").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //  Toast.makeText(MainActivity.this, "Uspesno podesavamo vrednost online,odnosno ulazak u MainActivity", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        if(currentUser!=null) {
//            checkDatabase = FirebaseDatabase.getInstance().getReference().
//                    child("Users").child(currentUser.getUid());
//            checkDatabase.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (!(boolean) dataSnapshot.child("online").getValue()) {
//                        sendToStart(false);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {
                    databaseReference.child("online").setValue(false);
                }
//                if(currentUser == null){
//                    FirebaseAuth.getInstance().signOut();
//                    sendToStart(false);
//                }
            }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            databaseReferenceLastSeen.child("lastSeen").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void sendToStart(boolean launched) {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            intent.putExtra("first_launch", launched);
            startActivity(intent);
            finish();
        }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.main_menu, menu);

         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId() == R.id.menu_log_out_btn){

             FirebaseAuth.getInstance().signOut();
             FirebaseUser currentUser = mAuth.getCurrentUser();

             if (currentUser == null) {
                 databaseReference.child("online").setValue(false);
                 databaseReferenceLastSeen.child("lastSeen").setValue(ServerValue.TIMESTAMP);
             }
             sendToStart(false);
         }
         if(item.getItemId() == R.id.menu_acount_settings_btn){
             Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
             startActivity(intentSettings);
         }
        if(item.getItemId() == R.id.menu_all_users_btn){
            Intent intentSettings = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(intentSettings);
        }


         return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
        Log.i("BADINOST", mAuth.getCurrentUser() + "");
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            databaseReference.child("online").setValue(false);
//            databaseReferenceLastSeen.child("lastSeen").setValue(ServerValue.TIMESTAMP);
//        }
//        sendToStart(false);
    }
    //STAVICU KOD SVAKOG INICJALIZOVANJA VREDNSOSTI ZA STAVKU LAST SEEN,DODATNI KOD ADD COMPLETE lISTENER
    //U NADI DA CE TO RESITI MOJ BAG
    //NE FUNKCIONISE
    //SADA SAM STAVIO SAMO DA SE ZAPISE VREME KAD JE KORISNIK BIO ONLINE SAMO U ON STOP METODI I KLASI CHAT ACTIVITY
    //I ON STOP METODI KLASE MAIN ACTIVITY
}
