package com.example.korisnik.chatproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    //    TextView textView;
    private ImageView profileImage;
    private TextView profileSTatus, profileName, profileCountFriendsNum;
    private Button sendRequestBtn,declainBtn;

    private DatabaseReference databaseReference,friendsRequestDatabase,friendsDatabase;
    private DatabaseReference notificationDatabase;
    private DatabaseReference databaseReferenceBasic;

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
        databaseReferenceBasic = FirebaseDatabase.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        databaseReference.keepSynced(true);
        //create another object in our database
        friendsRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        friendsRequestDatabase.keepSynced(true);

        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        notificationDatabase.keepSynced(true);

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        friendsDatabase.keepSynced(true);


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

        if((currentUser.getUid()).equals(user_id)){
            sendRequestBtn.setVisibility(View.INVISIBLE);
            sendRequestBtn.setEnabled(false);
            Log.i("Uskocio sam", currentUser.getUid() + user_id);
        }else{
            sendRequestBtn.setVisibility(View.VISIBLE);
            Log.i("Nisam uskocio", currentUser.getUid() + user_id);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ispitati ako ove vrednosti nisu null,da ne bi dolazilo do greske opet
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();

                profileName.setText(name);
                profileSTatus.setText(status);
                //show progressBar
                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(ProfileActivity.this).load(image)
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
                                Picasso.with(ProfileActivity.this).load(image)
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


                    DatabaseReference newNotificationref = databaseReferenceBasic.child("notifications").child(user_id).push();
                    String newNotificationId = newNotificationref.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", currentUser.getUid());
                    notificationData.put("type", "request");

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map requestMap = new HashMap();
                    requestMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", "sent");
                    requestMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", "received");
                    requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);
                    //ova stavka u bazi ce nam trebati za kreiranje liste u request fragmentu
                    requestMap.put("Received_friend/" + user_id + "/" + currentUser.getUid() + "/date", currentDate);
                    databaseReferenceBasic.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError != null){
                                Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();
                            } else {
                                current_state = "req_sent";
                                sendRequestBtn.setText("Cancel Friend Request");
                                declainBtn.setVisibility(View.GONE);
                                sendRequestBtn.setEnabled(true);
                                finish();
                            }
                        }
                    });
                }
                //-------------CANCEL REQUEST STATE-----------------------------------
                if(current_state.equals("req_sent")){

                    sendRequestBtn.setEnabled(false);

                    Map cancelMap = new HashMap();
                    cancelMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", null);
                    cancelMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", null);
                    cancelMap.put("Received_friend/" + user_id + "/" + currentUser.getUid() + "/date", null);

                    databaseReferenceBasic.updateChildren(cancelMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            notificationDatabase.child(user_id).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            sendRequestBtn.setEnabled(true);
                                            current_state = "not_friends";
                                            sendRequestBtn.setText("Send Friend Request");
                                            declainBtn.setVisibility(View.GONE);
                                            finish();
                                        }
                                    });
                        }
                    });

                }

                //-----------REQ RECEIVED STATE -------------------
                if(current_state.equals("req_received")){

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", "friends");
                    friendsMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", "friends");
                    friendsMap.put("Friends/" + currentUser.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/"  + currentUser.getUid() + "/date", currentDate);
                    friendsMap.put("Received_friend/" + currentUser.getUid() + "/" + user_id + "/date", null);
                    //INACE ZA FIREBASE,KAD SE STAVI VREDNOST U NEKOJ STAVCI BAZE DA JE NULL,ONDA SE AUTOMATSKI BRISE TO IZ BAZE
                    //KAO DA KORISTIMO removeValue()


                    databaseReferenceBasic.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            notificationDatabase.child(currentUser.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        current_state = "friends";
                                        sendRequestBtn.setText("Unfriend this Person");
                                        declainBtn.setVisibility(View.GONE);
                                        sendRequestBtn.setEnabled(true);
                                        finish();
                                    }
                                });
                        }
                    });
                }
                //--------UNFRIEND---------------
                if(current_state.equals("friends")){
                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + currentUser.getUid() + "/" + user_id + "/date", null);
                    unfriendMap.put("Friends/" + user_id + "/" + currentUser.getUid() + "/date", null);
                    unfriendMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", null);
                    unfriendMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", null);

                    databaseReferenceBasic.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                current_state = "not_friends";
                                sendRequestBtn.setText("Send Friend Request");
                                declainBtn.setVisibility(View.GONE);
                                sendRequestBtn.setEnabled(true);
                                finish();
                            }

                    });
                }
            }
        });
        declainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declainBtn.setEnabled(false);
                if (current_state.equals("req_received")) {
                    Map declainMap = new HashMap();
                    declainMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", null);
                    declainMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", null);
                    declainMap.put("Received_friend/" + currentUser.getUid() + "/" + user_id + "/date", null);

                    databaseReferenceBasic.updateChildren(declainMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            notificationDatabase.child(currentUser.getUid())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    current_state = "not_friends";
                                    sendRequestBtn.setText("SEND FRIEND REQUEST");
                                    sendRequestBtn.setEnabled(true);
                                    declainBtn.setVisibility(View.GONE);
                                    finish();
                                }
                            });
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
