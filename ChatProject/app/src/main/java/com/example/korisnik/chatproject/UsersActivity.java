package com.example.korisnik.chatproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewList;

    private DatabaseReference mUsersDatabase;
    private FirebaseRecyclerOptions<Users> options;

    private FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter;

    private FirebaseAuth mAuth;
    private  DatabaseReference databaseReferenceOnline,databaseReferenceOnlineLastSeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = (Toolbar) findViewById(R.id.all_users_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        recyclerViewList = (RecyclerView) findViewById(R.id.users_recyclerview);
        recyclerViewList.setHasFixedSize(true);
        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            databaseReferenceOnline = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnline.keepSynced(true);
            databaseReferenceOnlineLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnlineLastSeen.keepSynced(true);
        }

        options = new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(mUsersDatabase, Users.class)
                        .build();
        //POBOLJSANJE RECYCLER VIEW-A NA SLEDECEM LINKU
        //https://github.com/firebase/FirebaseUI-Android/tree/master/database
    }

    @Override
    protected void onStart() {
        super.onStart();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder> (options) {

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_item, parent, false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Users user) {
                holder.setName(user.getName());
                holder.setStatus(user.getStatus());
                holder.setThumbImage(user.getThumb_image(),getApplicationContext());

                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UsersActivity.this, ProfileActivity.class);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                    }
                });
            }
        };
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            databaseReferenceOnline.child("online").setValue(true);
            databaseReferenceOnlineLastSeen.child("lastSeen").setValue("Online");
        }
        recyclerViewList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            databaseReferenceOnline.child("online").setValue(false);
        }
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        //the mView defines whole complete relative layout for single list item
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
        }
        public void setStatus(String status){
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
        }

        public void setThumbImage(final String thumbImage, final Context context) {
             final CircleImageView imageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(context).load(thumbImage).fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.img_avatar)
                    .error(R.drawable.img_avatar)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(thumbImage).fit().centerCrop()
                                    .placeholder(R.drawable.img_avatar)
                                    .error(R.drawable.img_avatar)
                                    .into(imageView);
                        }
                    });
        }
    }
}
