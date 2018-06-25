package com.example.korisnik.chatproject;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView friendsList;

    private DatabaseReference friendsDatabase,usersDatabase;
    private DatabaseReference checkDatabase;
    private FirebaseAuth mAuth;

    private String currentUser_id;

    private View MainView;

    private TextView emptyView;

    FirebaseRecyclerOptions<Friends> options;

    FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapterFriends;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainView = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsList = (RecyclerView) MainView.findViewById(R.id.friends_list);
        emptyView = (TextView) MainView.findViewById(R.id.empty_view_friends);
        mAuth = FirebaseAuth.getInstance();

        currentUser_id = mAuth.getCurrentUser().getUid();

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUser_id);
        friendsDatabase.keepSynced(true);
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(linearLayoutManager);


        checkDatabase = FirebaseDatabase.getInstance().getReference();
        checkDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Friends").getValue()==null) {
                    friendsList.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    Log.i("onomatopeja", dataSnapshot.child("Friends").hasChild(currentUser_id) + "");
                    if(dataSnapshot.child("Friends").hasChild(currentUser_id)){
                        friendsList.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                    }else {
                        friendsList.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(friendsDatabase, Friends.class)
                .build();

        //POBOLJSANJE RECYCLER VIEW-A NA SLEDECEM LINKU
        //https://github.com/firebase/FirebaseUI-Android/tree/master/database

        return MainView;
    }

    @Override
    public void onStart() {
        super.onStart();

         firebaseRecyclerAdapterFriends = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder> (options) {

            @Override
            public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_item, parent, false);

                return new FriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Friends friend) {
                holder.setDate(friend.getDate());

                final String list_user_id = getRef(position).getKey();//uzimamo UID od trenutnog item-a u listView-u
                usersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("name").getValue().toString();
                        final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                        Boolean user_online =(Boolean)dataSnapshot.child("online").getValue();

                        holder.setName(name);
                        holder.setThumbImage(thumb_image, getContext());
                        holder.setUserOnline(user_online);


                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send Message"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setIcon(R.drawable.img_avatar);
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0){
                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);
                                        }
                                        if(i == 1){
                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", name);
                                            chatIntent.putExtra("user_thumb_image", thumb_image);
                                            startActivity(chatIntent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        friendsList.setAdapter(firebaseRecyclerAdapterFriends);
        firebaseRecyclerAdapterFriends.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapterFriends.stopListening();

    }
//    public void clear() {
//        final int size = data.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                data.remove(0);
//            }
//
//            notifyItemRangeRemoved(0, size);
//        }
//    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        //the mView defines whole complete relative layout for single list item
        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDate(String date){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_status);
            userNameView.setText(date);
        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
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
        public void setUserOnline(Boolean online_status){
            ImageView imageView = (ImageView) mView.findViewById(R.id.user_single_online_icon);
            if(online_status){
                imageView.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.INVISIBLE);
            }
        }


        }
    }

