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
import com.podrzizivot.project.pure_java_simple.Ambasadors;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.squareup.picasso.NetworkPolicy.NO_CACHE;
import static com.squareup.picasso.NetworkPolicy.OFFLINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FondationAmbasadorsFragment extends Fragment {

    private RecyclerView mAmbasadorsList;

    private DatabaseReference mAmbasadorsDatabase;

    private View progressBar;

    private FirebaseRecyclerOptions<Ambasadors> options;

    private TextView empty_list;

    private ImageView no_network_img;

    private Toast toast;

    private ValueEventListener valueEventListener;

    private View mMainView;

    private View view;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseRecyclerAdapter<Ambasadors,
            FondationAmbasadorsFragment.AmbasadorsViewHolder> firebaseRecyclerAdapterAmbasadors;


    public FondationAmbasadorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_fondation_ambasadors, container, false);

//        if (getActivity() != null) {
//            getActivity().setTitle("Ambasadori fondacije");
//        }

        mAmbasadorsList = mMainView.findViewById(R.id.ambasadors_list);
        progressBar = mMainView.findViewById(R.id.progress_ambasadors);
        progressBar.setVisibility(View.VISIBLE);
        empty_list = mMainView.findViewById(R.id.text_no_network);
        no_network_img = mMainView.findViewById(R.id.image_no_network);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAmbasadorsDatabase = firebaseDatabase.getReference().child("Users").child("Ambassadors");
//        String ambasadorsId = mAmbasadorsDatabase.getKey();
        mAmbasadorsDatabase.keepSynced(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        mAmbasadorsList.setHasFixedSize(true);
        mAmbasadorsList.setLayoutManager(gridLayoutManager);

        //gledace id u bazi,rasporedjuje ih leksikografski
        Query kidsQuery = mAmbasadorsDatabase.orderByValue();

        options = new FirebaseRecyclerOptions.Builder<Ambasadors>()
                .setQuery(kidsQuery, Ambasadors.class)
                .build();

//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("name", "Ime i prezime");
//        userMap.put("image", "Hi, there.I'm using project app");
//        userMap.put("date", "10/2/2018");
//
//        Map helpMap = new HashMap();
//        helpMap.put("Users/" + ambasadorsId, userMap);
//
//        mAmbasadorsDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            empty_list.setVisibility(View.GONE);
            no_network_img.setVisibility(View.GONE);
            mAmbasadorsList.setVisibility(View.VISIBLE);

            firebaseRecyclerAdapterAmbasadors = new FirebaseRecyclerAdapter<Ambasadors, FondationAmbasadorsFragment.AmbasadorsViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final FondationAmbasadorsFragment.AmbasadorsViewHolder holder, int position, @NonNull Ambasadors model) {
                    final String list_user_id = getRef(position).getKey();

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String ambImage;
                            Object ambImgObj = dataSnapshot.child("image").getValue();
                            if (ambImgObj != null) {
                                ambImage = ambImgObj.toString();
                            } else {
                                ambImage = "No img";
                            }
                            final String ambName;
                            Object ambNameObj = dataSnapshot.child("name").getValue();
                            if (ambNameObj != null) {
                                ambName = ambNameObj.toString();
                            } else {
                                ambName = "No name";
                            }

                            if (ambName != null) {
                                holder.setName(ambName);
                            }
                            if (ambImage != null) {
                                holder.setUserImage(ambImage, getContext());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    };

                    mAmbasadorsDatabase.child(list_user_id).addValueEventListener(valueEventListener);

                }

                @NonNull
                @Override
                public FondationAmbasadorsFragment.AmbasadorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.ambasadors_single_list, parent, false);

                    return new FondationAmbasadorsFragment.AmbasadorsViewHolder(view);
                }
            };

            firebaseRecyclerAdapterAmbasadors.notifyDataSetChanged();
            mAmbasadorsList.setAdapter(firebaseRecyclerAdapterAmbasadors);
            firebaseRecyclerAdapterAmbasadors.startListening();
        } else {
            if (firebaseRecyclerAdapterAmbasadors != null) {
                firebaseRecyclerAdapterAmbasadors.notifyDataSetChanged();
                mAmbasadorsList.setAdapter(null);
                firebaseRecyclerAdapterAmbasadors.stopListening();
            }
            if (mAmbasadorsDatabase != null && valueEventListener != null) {
                mAmbasadorsDatabase.removeEventListener(valueEventListener);
            }
            if (firebaseDatabase != null)
                firebaseDatabase.goOffline();
            mAmbasadorsList.setVisibility(View.GONE);
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
        if (firebaseRecyclerAdapterAmbasadors != null) {
            firebaseRecyclerAdapterAmbasadors.notifyDataSetChanged();
            mAmbasadorsList.setAdapter(null);
            firebaseRecyclerAdapterAmbasadors.stopListening();
        }
        if (mAmbasadorsDatabase != null && valueEventListener != null) {
            mAmbasadorsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
        mMainView = null;

        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapterAmbasadors != null) {
            firebaseRecyclerAdapterAmbasadors.notifyDataSetChanged();
            mAmbasadorsList.setAdapter(null);
            firebaseRecyclerAdapterAmbasadors.stopListening();
        }
        if (mAmbasadorsDatabase != null && valueEventListener != null) {
            mAmbasadorsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
//        AmbasadorsViewHolder ambasadorsViewHolder = new AmbasadorsViewHolder(mMainView);
        mMainView = null;
//        ambasadorsViewHolder.mView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    public static class AmbasadorsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ProgressBar progressBarImg;
        private TextView userNameView;
        private CircleImageView userImageView;

        private AmbasadorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            if (progressBarImg != null)
                progressBarImg = null;
            progressBarImg = mView.findViewById(R.id.progress_item_ambassadors);

        }

        private void setName(String name) {
            if (userNameView != null)
                userNameView.setText("");
            userNameView = mView.findViewById(R.id.name_list_ambassadors);
            userNameView.setText(name);

        }

        private void setUserImage(final String thumb_image, Context ctx) {
            progressBarImg.setVisibility(View.VISIBLE);
            if (userImageView != null)
                userImageView.setImageResource(android.R.color.transparent);
            userImageView = mView.findViewById(R.id.image_list_ambassadors);
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

}

