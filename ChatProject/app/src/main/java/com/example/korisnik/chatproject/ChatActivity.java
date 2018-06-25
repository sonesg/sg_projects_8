package com.example.korisnik.chatproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    String chat_user,user_name,user_thumb_image;

    private Toolbar toolbar;

    private DatabaseReference databaseReference,databaseReferenceOnline,databaseReferenceOnlineLastSeen;
    private DatabaseReference databaseReferenceMessaging,getDatabaseReferenceMessagingMore;
    private TextView profileName,lastSeen;
    private CircleImageView circleImageView;

    private FirebaseAuth auth,mAuth;
    private String currentUserId;

    private ImageButton chatImageBtn,chatSendBtn;
    private EditText chatEditTextessaging;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int currentPage = 1;

    private String lastKey = "";
    private String prevKey = "";
    private int itemPosition = 0;
    private int position = 0;

    private static final int GALLERY_PICK = 1;
    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_user = getIntent().getStringExtra("user_id");
        user_name = getIntent().getStringExtra("user_name");
        user_thumb_image = getIntent().getStringExtra("user_thumb_image");

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        databaseReferenceMessaging = FirebaseDatabase.getInstance().getReference();
        getDatabaseReferenceMessagingMore = FirebaseDatabase.getInstance().getReference();
        //JAKO BITNA STVAR KOD INICJALIZOVANJA DATABASEREFERENCE.MORAMO DA SE POBRINEMO DA DATABASEREF BUDE INICIJALIZOVANA
        //STO PRE U KODU
        imageStorage = FirebaseStorage.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.chat_app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setTitle(user_name);

        chatImageBtn = (ImageButton) findViewById(R.id.chatAddbtn);
        chatSendBtn = (ImageButton) findViewById(R.id.chatSendBtn);
        chatEditTextessaging = (EditText) findViewById(R.id.chatEditTextMess);
        recyclerView = (RecyclerView) findViewById(R.id.messages_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            databaseReferenceOnline = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnline.keepSynced(true);
            databaseReferenceOnlineLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnlineLastSeen.keepSynced(true);

        }

        messageAdapter = new MessageAdapter(messagesList,this,mAuth.getCurrentUser().getUid());
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        databaseReference.child("Chat").child(currentUserId).child(chat_user).child("seen").setValue(true);

        loadMessages();



        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);

        profileName = (TextView) findViewById(R.id.custom_bar_name);
        lastSeen = (TextView) findViewById(R.id.custom_bar_last_seen);
        circleImageView = (CircleImageView) findViewById(R.id.custom_bar_image);

        profileName.setText(user_name);

        Picasso.with(ChatActivity.this).load(user_thumb_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.img_avatar)
                .into(circleImageView);

                        databaseReference.child("Users").child(chat_user).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String online = dataSnapshot.child("lastSeen").getValue().toString();

                                if (online.equals("Online")) {
                                    lastSeen.setText(online);
                                } else {
                                    GetTimeAgo TimeAgo = new GetTimeAgo();
                                    long lastTime = Long.parseLong(online);
                                    String lastSeenTime = TimeAgo.getTimeAgo(lastTime, getApplicationContext());
                                    lastSeen.setText(lastSeenTime);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



        databaseReference.child("Chat").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ukoliko baza Chat:curent_user:nema chat_user ciji profil upravo gledamo
                if(!dataSnapshot.hasChild(chat_user)){
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + currentUserId + "/" + chat_user,chatAddMap);
                    chatUserMap.put("Chat/" + chat_user + "/" + currentUserId,chatAddMap);

                    //ubacujemo te nove vrednosti u bazu
                    databaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null){
                                //Work with errors
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        chatImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                chatImageBtn.setEnabled(false);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
//                chatImageBtn.setEnabled(true);
            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("GLEDAJ OVOOO", lastKey);
                Log.d("KITAVELIKA", "" + itemPosition);
                Log.i("BROJ", "" + itemPosition);
                if(itemPosition == 10) {
                    itemPosition = 0;
                    position+=10;
                    if(position == 10){
                        loadMoreMessages();
                    }else if(position > 10){
                        position--;
                        loadMoreMessages();
                    }
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            final String current_user_ref = "messages/" + currentUserId + "/" + chat_user;
            final String chat_user_ref = "messages/" + chat_user + "/" + currentUserId;

            DatabaseReference user_message_push = databaseReference.child("messages")
                    .child(currentUserId).child(chat_user).push();

            final String push_id = user_message_push.getKey();

            StorageReference filePath = imageStorage.child("message_images").child(push_id + ".jpg");
            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        String download_uri = task.getResult().getDownloadUrl().toString();

                        Map messageMap = new HashMap();
                        messageMap.put("message", download_uri);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", currentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        chatEditTextessaging.setText("");

                        databaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError != null){
                                    //rad sa greskama
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void loadMoreMessages() {
        DatabaseReference messageReference = getDatabaseReferenceMessagingMore.child("messages").child(currentUserId).child(chat_user);
        Query messageQuery = messageReference.orderByKey().endAt(lastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();
                if(!prevKey.equals(messageKey)){
                    messagesList.add(itemPosition++, message);
                } else {
                    prevKey = lastKey;
                }
                Log.v("PUTIC", "" + message.getMessage());

                if(itemPosition == 1) {

                    lastKey = messageKey;

                }
                if(itemPosition == 9){
                    itemPosition = 10;
                    //ova provera je neophodna radi lepseg i istog ucitavanja novih podataka u listi
                }


                //Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                messageAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);

                linearLayoutManager.scrollToPositionWithOffset(messagesList.size() - position , 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadMessages() {
        DatabaseReference messageReference = databaseReferenceMessaging.child("messages").child(currentUserId).child(chat_user);
        //limitira nas na 10 poruka//
        Query messageQuery = messageReference.limitToLast(TOTAL_ITEMS_TO_LOAD);

                messageQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages message = dataSnapshot.getValue(Messages.class);

                        itemPosition++;

                        if(itemPosition == 1){
                            String messageKey = dataSnapshot.getKey();
                            lastKey = messageKey;
                            prevKey = messageKey;
                            Log.i("opaca", messageKey);
                        }
                        Log.i("PUTICI", "" + itemPosition);

                        messagesList.add(message);
                        messageAdapter.notifyDataSetChanged();

                        //this is the bottom of uor recycler view
                        recyclerView.scrollToPosition(messagesList.size()-1);

                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            databaseReferenceOnline.child("lastSeen").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void sendMessage() {
        chatSendBtn.setEnabled(false);
        String message = chatEditTextessaging.getText().toString();
        if(!message.isEmpty()){

            //////////////////////////////////////////////////////////////////
            //HIDING KEYBOARD AFTER CLICKING SEND BUTTON
            InputMethodManager inputMethodManager =
                    (InputMethodManager) ChatActivity.this.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    ChatActivity.this.getCurrentFocus().getWindowToken(), 0);
            //////////////////////////////////////////////////////////////////
            chatEditTextessaging.setText("");

            Map messageMapStuff = new HashMap();

            String currentUserRef = "messages/" + currentUserId + "/" + chat_user;
            final String chatUserRef = "messages/" + chat_user + "/" + currentUserId;

            DatabaseReference user_message_push = databaseReference.child("messages").child(currentUserId)
                    .child(chat_user).push();
            String push_key = user_message_push.getKey();//Ne znam konkretno zasto je potrebno da se ubaci i taj key ispod dva usera ali izgleda da je to dobra praksa da se tako radi
            //Znam zasto.Jer je Firebase tako napravljen da svaki novi podatak koji se ubaci na isto mesto,bice tu,odnosno stari podatak na tom mestu ce biti zamenjen
            //odnosno kada saljemo vise poruka istoj osobi,ukoliko nemamo taj key koji ce biti kod svake poruke razlicit,nove poruke ce brisati stare i uvek cemo imati u bazi samo jednu poruku u jednoj konverzaciji,iako je mozda tu bilo 100 poruka
            //Kreiramo kontente koji ce ici u odeljku baze podataka gde se nalaze messages
            messageMapStuff.put("message", message);
            messageMapStuff.put("seen", false);
            messageMapStuff.put("type", "text");
            messageMapStuff.put("time", ServerValue.TIMESTAMP);
            messageMapStuff.put("from", currentUserId);

            Map messageMap = new HashMap();

            messageMap.put(currentUserRef + "/" + push_key, messageMapStuff);
            messageMap.put(chatUserRef + "/" + push_key, messageMapStuff);

            databaseReference.child("Chat").child(currentUserId).child(chat_user).child("seen").setValue(true);
            databaseReference.child("Chat").child(currentUserId).child(chat_user).child("timestamp").setValue(ServerValue.TIMESTAMP);

            databaseReference.child("Chat").child(chat_user).child(currentUserId).child("seen").setValue(false);
            databaseReference.child("Chat").child(chat_user).child(currentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);

            databaseReference.updateChildren(messageMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        //Work with errors
                    }
                    chatSendBtn.setEnabled(true);
                }
            });

        }
    }
}
