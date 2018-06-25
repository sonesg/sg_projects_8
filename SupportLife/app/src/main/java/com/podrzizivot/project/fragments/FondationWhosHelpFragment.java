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
import com.podrzizivot.project.pure_java_simple.Help;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.squareup.picasso.NetworkPolicy.NO_CACHE;
import static com.squareup.picasso.NetworkPolicy.OFFLINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FondationWhosHelpFragment extends Fragment {

    private RecyclerView mHelpList;

    private DatabaseReference mHelpDatabase;

    private View progressBar;

    private FirebaseRecyclerOptions<Help> options;

    private TextView empty_list;
    private ImageView no_network_img;

    private ValueEventListener valueEventListener;

    FondationWhosHelpFragment.HelpViewHolder fondation;

    private Toast toast;

    private View mMainView;

    private View view;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseRecyclerAdapter<Help, FondationWhosHelpFragment.HelpViewHolder> firebaseRecyclerAdapterHelp;


    public FondationWhosHelpFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_fondation_whos_help, container, false);
//        if (getActivity() != null) {
//            getActivity().setTitle("Pomogli smo");
//        }

        mHelpList = mMainView.findViewById(R.id.whos_help_list);
        progressBar = mMainView.findViewById(R.id.progress_whos_help);
        progressBar.setVisibility(View.VISIBLE);
        empty_list = mMainView.findViewById(R.id.text_no_network);
        no_network_img = mMainView.findViewById(R.id.image_no_network);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mHelpDatabase = firebaseDatabase.getReference().child("Users").child("Help");
//        String helpId = mHelpDatabase.getKey();
        mHelpDatabase.keepSynced(true);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        mHelpList.setHasFixedSize(true);
        mHelpList.setLayoutManager(gridLayoutManager);

        //gledace id u bazi,rasporedjuje ih leksikografski
        Query kidsQuery = mHelpDatabase.orderByChild("bird").limitToLast(30);

        options = new FirebaseRecyclerOptions.Builder<Help>()
                .setQuery(kidsQuery, Help.class)
                .build();

//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("name", "Ime i prezime");
//        userMap.put("desc", "Pomoc Marku Markovicu");
//        userMap.put("image", "Hi, there.I'm using project app");
//        userMap.put("number", "15.000 RSD");
//        userMap.put("date", "10/2/2018");
//
//        Map helpMap = new HashMap();
//        helpMap.put("Users/" + helpId, userMap);
//
//        mHelpDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            mHelpList.setVisibility(View.VISIBLE);
            firebaseRecyclerAdapterHelp = new FirebaseRecyclerAdapter<Help, FondationWhosHelpFragment.HelpViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final FondationWhosHelpFragment.HelpViewHolder holder, int position, @NonNull Help model) {
                    final String list_user_id = getRef(position).getKey();

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String helpImage;
                            Object helpImgObj = dataSnapshot.child("image").getValue();
                            if (helpImgObj != null) {
                                helpImage = helpImgObj.toString();
                            } else {
                                helpImage = "No img";
                            }
                            final String helpName;
                            Object helpNameObj = dataSnapshot.child("name").getValue();
                            if (helpNameObj != null) {
                                helpName = helpNameObj.toString();
                            } else {
                                helpName = "No name";
                            }
                            final String helpDesc;
                            Object helpDescObj = dataSnapshot.child("desc").getValue();
                            if (helpDescObj != null) {
                                helpDesc = helpDescObj.toString();
                            } else {
                                helpDesc = "No description";
                            }
                            final String helpNumb;
                            Object helpNumbObj = dataSnapshot.child("number").getValue();
                            if (helpNumbObj != null) {
                                helpNumb = helpNumbObj.toString();
                            } else {
                                helpNumb = "No img";
                            }

                            if (helpName != null) {
                                holder.setName(helpName);
                            }
                            if (helpImage != null) {
                                holder.setUserImage(helpImage, getContext());
                            }
                            if (helpDesc != null) {
                                holder.setDesc(helpDesc);
                            }
                            if (helpNumb != null) {
                                holder.setNumber(helpNumb);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    mHelpDatabase.child(list_user_id).addValueEventListener(valueEventListener);

                }

                @NonNull
                @Override
                public FondationWhosHelpFragment.HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.help_single_list, parent, false);

//                    if (fondation != null) {
//                        fondation = null;
//                        return fondation = new FondationWhosHelpFragment.HelpViewHolder(view);
//                    } else {
//                        fondation = new FondationWhosHelpFragment.HelpViewHolder(view);
//                        return fondation;
//                    }
                    fondation = null;
                    return fondation = new FondationWhosHelpFragment.HelpViewHolder(view);
                }
            };

            firebaseRecyclerAdapterHelp.notifyDataSetChanged();
            mHelpList.setAdapter(firebaseRecyclerAdapterHelp);
            firebaseRecyclerAdapterHelp.startListening();
        } else {
            if (firebaseRecyclerAdapterHelp != null) {
                firebaseRecyclerAdapterHelp.notifyDataSetChanged();
                mHelpList.setAdapter(null);
                firebaseRecyclerAdapterHelp.stopListening();
            }
            if (mHelpDatabase != null && valueEventListener != null) {
                mHelpDatabase.removeEventListener(valueEventListener);
            }
            if (firebaseDatabase != null)
                firebaseDatabase.goOffline();
            mHelpList.setVisibility(View.GONE);
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
        if (firebaseRecyclerAdapterHelp != null) {
            firebaseRecyclerAdapterHelp.notifyDataSetChanged();
            mHelpList.setAdapter(null);
            firebaseRecyclerAdapterHelp.stopListening();
        }
        if (mHelpDatabase != null && valueEventListener != null) {
            mHelpDatabase.removeEventListener(valueEventListener);
        }
        view = null;
        mMainView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapterHelp != null) {
            firebaseRecyclerAdapterHelp.notifyDataSetChanged();
            mHelpList.setAdapter(null);
            firebaseRecyclerAdapterHelp.stopListening();
        }
        if (mHelpDatabase != null && valueEventListener != null) {
            mHelpDatabase.removeEventListener(valueEventListener);
        }

//        HelpViewHolder friendsViewHolder = new HelpViewHolder(mMainView);
        view = null;
        mMainView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
//        friendsViewHolder.mView = null;
    }

    public static class HelpViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ProgressBar progressBarImg;
        private TextView userDescView;
        private TextView userNameView;
        private CircleImageView userImageView;
        private TextView userNumberView;

        private HelpViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            if (progressBarImg != null)
                progressBarImg = null;
            progressBarImg = mView.findViewById(R.id.progress_item_help);

        }

        private void setDesc(String desc) {
            if (userDescView != null)
                userDescView.setText("");
            userDescView = mView.findViewById(R.id.desc_list_help);
            userDescView.setText(desc);
        }

        private void setName(String name) {
            if (userNameView != null)
                userNameView.setText("");
            userNameView = mView.findViewById(R.id.name_list_help);
            userNameView.setText(name);

        }

        private void setNumber(String numb) {
            if (userNumberView != null)
                userNumberView.setText("");
            userNumberView = mView.findViewById(R.id.number_list_help);
            userNumberView.setText(numb);

        }

        private void setUserImage(final String thumb_image, Context ctx) {
            progressBarImg.setVisibility(View.VISIBLE);
            if (userImageView != null)
                userImageView.setImageResource(android.R.color.transparent);
            userImageView = mView.findViewById(R.id.image_list_help);
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
