package com.podrzizivot.project.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.podrzizivot.project.R;
import com.podrzizivot.project.pure_java_simple.Friends;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.squareup.picasso.NetworkPolicy.NO_CACHE;
import static com.squareup.picasso.NetworkPolicy.OFFLINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FondationFriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;

    private View progressBar;

    private FirebaseRecyclerOptions<Friends> options;

    private TextView empty_list;
    private ImageView no_network_img;

    private ValueEventListener valueEventListener;

    private Toast toast;

    private View mMainView;

    private View view;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseRecyclerAdapter<Friends,
            FondationFriendsFragment.FriendsViewHolder> firebaseRecyclerAdapterFriends;


    public FondationFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_fondation_friends, container, false);

//        if (getActivity() != null) {
//            getActivity().setTitle("Donatori");
//        }

        mFriendsList = mMainView.findViewById(R.id.friends_list);
        progressBar = mMainView.findViewById(R.id.progress_friends);
        progressBar.setVisibility(View.VISIBLE);
        empty_list = mMainView.findViewById(R.id.text_no_network);
        no_network_img = mMainView.findViewById(R.id.image_no_network);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mFriendsDatabase = firebaseDatabase.getReference().child("Users").child("Friends");
//        String friendsId = mFriendsDatabase.getKey();
        mFriendsDatabase.keepSynced(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(gridLayoutManager);

        Query kidsQuery = mFriendsDatabase.orderByValue();

        options = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(kidsQuery, Friends.class)
                .build();

//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("image", "Hi, there.I'm using project app");
//        userMap.put("date", "2018/1/1");
//
//        Map helpMap = new HashMap();
//        helpMap.put("Users/" + friendsId, userMap);
//
//        mFriendsDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//
//                } else {
//                    Toast.makeText(getContext(), "task nije successful",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();


        if (isNetworkAvailable()) {
            if (firebaseDatabase != null)
                firebaseDatabase.goOnline();
            no_network_img.setVisibility(View.GONE);
            empty_list.setVisibility(View.GONE);
            mFriendsList.setVisibility(View.VISIBLE);
            firebaseRecyclerAdapterFriends = new FirebaseRecyclerAdapter<Friends, FondationFriendsFragment.FriendsViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final FondationFriendsFragment.FriendsViewHolder holder, int position, @NonNull Friends model) {
                    final String list_user_id = getRef(position).getKey();

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String friImage;
                            Object friImgObj = dataSnapshot.child("image").getValue();
                            if (friImgObj != null) {
                                friImage = friImgObj.toString();
                            } else {
                                friImage = "No img";
                            }

                            if (friImage != null) {
                                holder.setUserImage(friImage, getContext());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    valueEventListener = mFriendsDatabase.child(list_user_id).addValueEventListener(valueEventListener);

                }

                @NonNull
                @Override
                public FondationFriendsFragment.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        //progressBar = null;
                    }
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.friends_single_list, parent, false);

                    return new FondationFriendsFragment.FriendsViewHolder(view);
                }
            };

            firebaseRecyclerAdapterFriends.notifyDataSetChanged();
            mFriendsList.setAdapter(firebaseRecyclerAdapterFriends);
            firebaseRecyclerAdapterFriends.startListening();
        } else {
            if (firebaseRecyclerAdapterFriends != null) {
                firebaseRecyclerAdapterFriends.notifyDataSetChanged();
                mFriendsList.setAdapter(null);
                firebaseRecyclerAdapterFriends.stopListening();
            }
            if (mFriendsDatabase != null && valueEventListener != null) {
                mFriendsDatabase.removeEventListener(valueEventListener);
            }
            if (firebaseDatabase != null)
                firebaseDatabase.goOffline();
            mFriendsList.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            no_network_img.setVisibility(View.VISIBLE);
            empty_list.setVisibility(View.VISIBLE);
            networkMayAvailable(no_network_img);
        }
    }

    private void networkMayAvailable(ImageView no_network_img) {
        no_network_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    onStart();
                } else {
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(getContext(), "Nema internet konekcije",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapterFriends != null) {
            firebaseRecyclerAdapterFriends.notifyDataSetChanged();
            mFriendsList.setAdapter(null);
            firebaseRecyclerAdapterFriends.stopListening();
        }
        if (mFriendsDatabase != null && valueEventListener != null) {
            mFriendsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
        mMainView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapterFriends != null) {
            firebaseRecyclerAdapterFriends.notifyDataSetChanged();
            mFriendsList.setAdapter(null);
            firebaseRecyclerAdapterFriends.stopListening();
        }
        if (mFriendsDatabase != null && valueEventListener != null) {
            mFriendsDatabase.removeEventListener(valueEventListener);
        }

//        FriendsViewHolder friendsViewHolder = new FriendsViewHolder(mMainView);
        view = null;
        mMainView = null;
//        friendsViewHolder.mView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();

    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ProgressBar progressBarImg;
        private ImageView userImageView;

        private FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            if (progressBarImg != null)
                progressBarImg = null;
            progressBarImg = mView.findViewById(R.id.progress_item_friends);

        }

        private void setUserImage(final String thumb_image, final Context ctx) {
            progressBarImg.setVisibility(View.VISIBLE);
            if (userImageView != null) {
                Log.i("ALFAROMEO_Image", userImageView.toString());
                userImageView.setImageResource(android.R.color.transparent);
                //imgView.setImageDrawable(null);
            }
            userImageView = mView.findViewById(R.id.image_list_friends);
            Log.i("ALFAROMEO_Image111", userImageView.toString());
            final Picasso picasso = Picasso.with(ctx);
            picasso.setIndicatorsEnabled(false);
            picasso.load(thumb_image).networkPolicy(OFFLINE)
                    //.placeholder(R.drawable.white_img)
                    .into(userImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBarImg.setVisibility(View.GONE);
//                    NO_CACHE
//                    Skips checking the disk cache and forces loading through the network.
//                            NO_STORE
//                    Skips storing the result into the disk cache.
//                    OFFLINE
//                    Forces the request through the disk cache only, skipping network.


//                    The color shows the source of image which is being displayed
//
//                    Red color indicates that image is fetched from network.
//
//                            Green color indicates that image is fetched from cache memory.
//
//                            Blue color indicates that image is fetched from disk memory.
                        }

                        @Override
                        public void onError() {
                            picasso.load(thumb_image).networkPolicy(NO_CACHE)
                                    //.placeholder(R.drawable.white_img)
                                    .into(userImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBarImg.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

        }
    }

    private boolean isNetworkAvailable() {
        if (getActivity() != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        return false;
    }

    //https://stackoverflow.com/questions/24547555/how-to-analyze-memory-using-android-studio

}
