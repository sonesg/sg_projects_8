package com.example.korisnik.chatproject;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity3 extends AppCompatActivity {

//    TextView textView;
    private ImageView profileImage;
    private TextView profileSTatus, profileName, profileCountFriendsNum;
    private Button sendRequestBtn,declainBtn;

    private DatabaseReference databaseReference,friendsRequestDatabase,friendsDatabase;
    private DatabaseReference notificationDatabase;

    private FirebaseUser currentUser;

    private View progressBar;

    private String current_state;

    private FirebaseAuth mAuth,current_user;
    private DatabaseReference databaseReferenceOnline,databaseReferenceOnlineLastSeen;
    private String current;

    //TODO UBACITI RAZNE VRSTE NOTIFIKACIJA U BAZU
    //TODO WE WANT TO STORE TOKEN ID IN EACH USER ID
    //IF YOU ARE ALLOWING USERS TO LOGIN THROUGH MULTIPLE DEVICES  THEN YOU'RE GOING TO HAVE MULTIPLE TOKEN IDs
    //token id of user device can be refreshed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id;
        if(getIntent().getStringExtra("user_id") == null){
            //to ce biti slucaj kada preko fja firebase saljemo podatak-user_id
            //posto ne znam kako da primim taj podatak-koristicu current user id
            current_user = FirebaseAuth.getInstance();
            current = current_user.getCurrentUser().getUid();
            user_id =  current;
        }else {
            user_id = getIntent().getStringExtra("user_id");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        databaseReference.keepSynced(true);
        //create another object in our database
        friendsRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        friendsRequestDatabase.keepSynced(true);

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendsDatabase.keepSynced(true);

        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        notificationDatabase.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            databaseReferenceOnline = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnline.keepSynced(true);
            databaseReferenceOnlineLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnlineLastSeen.keepSynced(true);
        }


        //trenutni korisnikov UID
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

//        textView = (TextView) findViewById(R.id.textProfile);
//        textView.setText(data);
        profileImage = (ImageView) findViewById(R.id.imageViewProfile);
        profileSTatus = (TextView) findViewById(R.id.textProfileStatus);
        profileName = (TextView) findViewById(R.id.textProfileName);
        profileCountFriendsNum = (TextView) findViewById(R.id.total_friends);
        sendRequestBtn = (Button) findViewById(R.id.btn_send_request);
        progressBar = findViewById(R.id.progressProfile);
        declainBtn = (Button) findViewById(R.id.profileDeclineBtn);
        //disable that button in a while
        declainBtn.setVisibility(View.INVISIBLE);
        declainBtn.setEnabled(false);

        current_state = "not_friends";

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                profileName.setText(name);
                profileSTatus.setText(status);
                //show progressBar
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(ProfileActivity3.this).load(image)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.img_avatar)
                        .into(profileImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                friendsRequestDatabase.child(currentUser.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(user_id)){
                                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                                            //slucaj ukoliko je vrednost requested,odnosno poslat request od strane nekog drugog user_id
                                            if(req_type.equals("received")){
                                                current_state = "req_received";
                                                sendRequestBtn.setText("ACCEPT FRIEND REQUEST");
                                                declainBtn.setVisibility(View.VISIBLE);
                                                declainBtn.setEnabled(true);
                                            }else if(req_type.equals("sent")){
                                                current_state = "req_sent";
                                                sendRequestBtn.setText("CANCEL FRIEND REQUEST");
                                                declainBtn.setVisibility(View.INVISIBLE);
                                                declainBtn.setEnabled(false);
                                            }else if(req_type.equals("friends")) {
                                                current_state = "friends";
                                                sendRequestBtn.setText("UNFRIEND THIS FRIEND");
                                                declainBtn.setVisibility(View.INVISIBLE);
                                                declainBtn.setEnabled(false);
                                            }

                                        }
                                        if (progressBar != null) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                                Picasso.with(ProfileActivity3.this).load(image)
                                        .placeholder(R.drawable.img_avatar)
                                        .error(R.drawable.img_avatar)
                                        .into(profileImage);

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disable button after clicking
                sendRequestBtn.setEnabled(false);
                //-------NOT FRiENDS STATE-----------------------------
                //check if they're friends almost
                if(current_state.equals("not_friends")){
                    friendsRequestDatabase.child(currentUser.getUid()).child(user_id).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //ako je uspesno slanje,sad radimo primanje tog zahteva u njegovom delu baze
                                friendsRequestDatabase.child(user_id).child(currentUser.getUid()).child("request_type")
                                        .setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //stavljamo na to stanje da kada pritisnemo ponovo button da ne dodje do
                                        // istog procesa kao da je not_friends

                                        HashMap<String,String> notificationData = new HashMap<>();
                                        notificationData.put("from", currentUser.getUid());
                                        notificationData.put("type", "request");

                                        //we need the ID of user to whom we are sending friend request
                                        //push will create random ID to store a single notification
                                        notificationDatabase.child(user_id).push().setValue(notificationData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            current_state = "req_sent";
                                                            sendRequestBtn.setText("CANCEL FRIEND REQUEST");
                                                            declainBtn.setVisibility(View.INVISIBLE);
                                                            declainBtn.setEnabled(false);
                                                            Toast.makeText(ProfileActivity3.this, "Success", Toast.LENGTH_LONG).show();

                                                        }else {
                                                            Toast.makeText(ProfileActivity3.this, "Some problems with sending notification data", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                            }else{
                                Toast.makeText(ProfileActivity3.this, "Failed Sending Request...", Toast.LENGTH_LONG).show();
                            }
                            sendRequestBtn.setEnabled(true);
                        }
                    });
                }
                //-------------CANCEL REQUEST STATE-----------------------------------
                if(current_state.equals("req_sent")){
                    friendsRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                friendsRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //stavljamo na to stanje da kada pritisnemo ponovo button da ne dodje do
                                        // istog procesa kao da je not_friends
                                        notificationDatabase.child(user_id).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            current_state = "not_friends";
                                                            sendRequestBtn.setText("SEND FRIEND REQUEST");
                                                            declainBtn.setVisibility(View.INVISIBLE);
                                                            declainBtn.setEnabled(false);
                                                            Toast.makeText(ProfileActivity3.this, "Success", Toast.LENGTH_LONG).show();


                                                        }
                                                    }
                                                });
                                    }
                                });

                            }else{
                                Toast.makeText(ProfileActivity3.this, "Failed to cancel request", Toast.LENGTH_LONG).show();
                            }
                            sendRequestBtn.setEnabled(true);
                        }
                    });
                }

                //-----------REQ RECEIVED STATE -------------------
                if(current_state.equals("req_received") ){
                    //trenutni datum
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                    friendsRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        friendsRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //TODO moze i na drugaciji nacin da se resi ovo,ali i ovo nije lose resenje
                                                            //TODO drugi nacin je da proverimo samo da li postoji bilo kakav podatak u bazi friends
                                                            //TODO tako da u tom slucaju nece biti neophodno da se ubacuje podatak "friends" u bazi request_type
                                                            friendsRequestDatabase.child(currentUser.getUid()).child(user_id).child("request_type")
                                                                    .setValue("friends").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                friendsRequestDatabase.child(user_id).child(currentUser.getUid()).child("request_type")
                                                                                        .setValue("friends").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        friendsDatabase.child(currentUser.getUid()).child(user_id)
                                                                                                .child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                friendsDatabase.child(user_id).child(currentUser.getUid())
                                                                                                  .child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                        notificationDatabase.child(currentUser.getUid()).removeValue()
                                                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        current_state = "friends";
                                                                                                                        sendRequestBtn.setText("UNFRIEND THIS PERSON");
                                                                                                                        declainBtn.setVisibility(View.INVISIBLE);
                                                                                                                        declainBtn.setEnabled(false);
                                                                                                                        Toast.makeText(ProfileActivity3.this, "Success", Toast.LENGTH_LONG).show();
                                                                                                                    }
                                                                                                                });

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }else{
                                                                                Toast.makeText(ProfileActivity3.this, "Failed to unfriend your friend", Toast.LENGTH_LONG).show();
                                                                            }
                                                                            sendRequestBtn.setEnabled(true);
                                                                        }
                                                                    });

                                                        }
                                                    }
                                                });
                                                }
                                            }
                                        });

                }
                //--------UNFRIEND---------------
                if(current_state.equals("friends")){
                    friendsDatabase.child(currentUser.getUid()).child(user_id).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                //remove data from our user database
                                    if(task.isSuccessful()) {
                                        friendsDatabase.child(user_id).child(currentUser.getUid()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    friendsRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                friendsRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            //stavljamo na to stanje da kada pritisnemo ponovo button da ne dodje do
                                                                            // istog procesa kao da je not_friends
                                                                            current_state = "not_friends";
                                                                            sendRequestBtn.setText("SEND FRIEND REQUEST");
                                                                            declainBtn.setVisibility(View.INVISIBLE);
                                                                            declainBtn.setEnabled(false);
                                                                            Toast.makeText(ProfileActivity3.this, "Success", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(ProfileActivity3.this, "Failed to unfriend this friend", Toast.LENGTH_LONG).show();
                                                        }
                                                        sendRequestBtn.setEnabled(true);
                                                    }
                                                });
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
        declainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declainBtn.setEnabled(false);
                if(current_state.equals("req_received")){
                    friendsRequestDatabase.child(currentUser.getUid()).child(user_id).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        friendsRequestDatabase.child(user_id).child(currentUser.getUid()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        notificationDatabase.child(currentUser.getUid()).removeValue()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        current_state = "not_friends";
                                                                        sendRequestBtn.setText("SEND FRIEND REQUEST");
                                                                        declainBtn.setVisibility(View.INVISIBLE);
                                                                        declainBtn.setEnabled(false);
                                                                    }
                                                                });
                                                    }
                                                });
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
            databaseReferenceOnlineLastSeen.child("lastSeen").setValue("Online").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileActivity3.this, "Uspesno podesavamo vrednost online zbog ulaska u activity Profile", Toast.LENGTH_SHORT).show();
                }
            });
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
