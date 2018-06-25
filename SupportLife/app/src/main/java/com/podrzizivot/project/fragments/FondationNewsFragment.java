package com.podrzizivot.project.fragments;


import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.podrzizivot.project.pure_java_simple.News;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.squareup.picasso.NetworkPolicy.NO_CACHE;
import static com.squareup.picasso.NetworkPolicy.OFFLINE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FondationNewsFragment extends Fragment {

    private RecyclerView mNewsList;

    private DatabaseReference mNewsDatabase;

    private View progressBar;

    private FirebaseRecyclerOptions<News> options;

    private TextView empty_list;
    private ImageView no_network_img;

    private Toast toast;

    private ValueEventListener valueEventListener;

    private View mMainView;

    private View view;

    private FirebaseDatabase firebaseDatabase;

    private FirebaseRecyclerAdapter<News, FondationNewsFragment.NewsViewHolder> firebaseRecyclerAdapterNews;


    public FondationNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_fondation_news, container, false);
//        if (getActivity() != null) {
//            getActivity().setTitle("Aktuelnosti");
//        }
        mNewsList = mMainView.findViewById(R.id.news_list);
        progressBar = mMainView.findViewById(R.id.progress_news);
        progressBar.setVisibility(View.VISIBLE);
        empty_list = mMainView.findViewById(R.id.text_no_network);
        no_network_img = mMainView.findViewById(R.id.image_no_network);
        //PROBLEM KOD PROGRESS BAR-A JE TO STO JE ON SVE VREME RADI,ALI JE PREKRIVEN DRUGIM SADRZAJEM PA SE NE VIDI
        //TO MOZE DOVESTI DO TROSENJA RESURSA TELEFONA
        firebaseDatabase = FirebaseDatabase.getInstance();
        mNewsDatabase = firebaseDatabase.getReference().child("Users").child("News");
//        String newsId = mNewsDatabase.getKey();
        mNewsDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mNewsList.setHasFixedSize(true);
        mNewsList.setLayoutManager(linearLayoutManager);

        Query kidsQuery = mNewsDatabase.orderByChild("date");

        options = new FirebaseRecyclerOptions.Builder<News>()
                .setQuery(kidsQuery, News.class)
                .build();

//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("image", "https://www.w3schools.com/howto/img_avatar.png");
//        userMap.put("name", "Pomoc Marku Markovicu");
//        userMap.put("desc", "Hi, there.I'm using project app");
//        userMap.put("date", "10/2/2018");
//
//        Map newsMap = new HashMap();
//        newsMap.put("Users/" + newsId, userMap);
//
//        mNewsDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//
//                }else{
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
            mNewsList.setVisibility(View.VISIBLE);

            firebaseRecyclerAdapterNews = new FirebaseRecyclerAdapter<News, NewsViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final NewsViewHolder holder, int position, @NonNull News model) {
                    final String list_user_id = getRef(position).getKey();

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String newsImage;
                            Object NewsImgObj = dataSnapshot.child("image").getValue();
                            if (NewsImgObj != null) {
                                newsImage = NewsImgObj.toString();
                            } else {
                                newsImage = "No img";
                            }
                            final String newsName;
                            Object NewsNameObj = dataSnapshot.child("name").getValue();
                            if (NewsNameObj != null) {
                                newsName = NewsNameObj.toString();
                            } else {
                                newsName = "No name";
                            }
                            final String newsDesc;
                            Object NewsDescObj = dataSnapshot.child("desc").getValue();
                            if (NewsDescObj != null) {
                                newsDesc = NewsDescObj.toString();
                            } else {
                                newsDesc = "No description";
                            }
                            final String newsDate;
                            Object NewsDateObj = dataSnapshot.child("date").getValue();
                            if (NewsDateObj != null) {
                                newsDate = NewsDateObj.toString();
                            } else {
                                newsDate = "No date";
                            }


                            if (newsName != null) {
                                holder.setName(newsName);
                            }
                            if (newsImage != null) {
                                holder.setUserImage(newsImage, getContext());
                            }
                            if (newsDesc != null) {
                                holder.setDesc(newsDesc);
                            }
                            if (newsDate != null) {
                                holder.setDate(newsDate);
                            }
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (getContext() != null) {
                                        final Dialog settingsDialog = new Dialog(getContext());
                                        if (settingsDialog.getWindow() != null) {
                                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                        }
                                        settingsDialog.setContentView(View.inflate(getContext(), R.layout.dialog_layout_list
                                                , null));
                                        final ImageView imageView = settingsDialog.findViewById(R.id.dialog_image_list);
                                        Picasso picasso = Picasso.with(getContext());
                                        picasso.setIndicatorsEnabled(false);
                                        picasso.load(newsImage).networkPolicy(OFFLINE)
                                                .placeholder(R.drawable.white_img)
                                                .into(imageView);
                                        final TextView name_dialog = settingsDialog.findViewById(R.id.dialog_name_list);
                                        name_dialog.setText(newsName);
                                        final TextView date_dialog = settingsDialog.findViewById(R.id.dialog_date_list);
                                        date_dialog.setText(newsDate);
                                        final TextView desc_dialog = settingsDialog.findViewById(R.id.dialog_desc_list);
                                        desc_dialog.setText(newsDesc);
                                        settingsDialog.show();
                                        final ImageView imageViewBack = settingsDialog.findViewById(R.id.dialog_back_list);
                                        imageViewBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                imageView.setImageResource(android.R.color.transparent);
                                                name_dialog.setText("");
                                                date_dialog.setText("");
                                                desc_dialog.setText("");
                                                imageViewBack.setImageResource(android.R.color.transparent);
                                                settingsDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    mNewsDatabase.child(list_user_id).addValueEventListener(valueEventListener);

                }

                @NonNull
                @Override
                public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.user_single_item, parent, false);

                    return new FondationNewsFragment.NewsViewHolder(view);
                }
            };

            firebaseRecyclerAdapterNews.notifyDataSetChanged();
            mNewsList.setAdapter(firebaseRecyclerAdapterNews);
            firebaseRecyclerAdapterNews.startListening();
        } else {
            if (firebaseRecyclerAdapterNews != null) {
                firebaseRecyclerAdapterNews.notifyDataSetChanged();
                mNewsList.setAdapter(null);
                firebaseRecyclerAdapterNews.stopListening();
            }
            if (mNewsDatabase != null && valueEventListener != null) {
                mNewsDatabase.removeEventListener(valueEventListener);
            }
            if (firebaseDatabase != null)
                firebaseDatabase.goOffline();
            mNewsList.setVisibility(View.GONE);
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
        if (firebaseRecyclerAdapterNews != null) {
            firebaseRecyclerAdapterNews.notifyDataSetChanged();
            mNewsList.setAdapter(null);
            firebaseRecyclerAdapterNews.stopListening();
        }
        if (mNewsDatabase != null && valueEventListener != null) {
            mNewsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
        mMainView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapterNews != null) {
            firebaseRecyclerAdapterNews.notifyDataSetChanged();
            mNewsList.setAdapter(null);
            firebaseRecyclerAdapterNews.stopListening();
        }
        if (mNewsDatabase != null && valueEventListener != null) {
            mNewsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
//        NewsViewHolder friendsViewHolder = new NewsViewHolder(mMainView);
        mMainView = null;
//        friendsViewHolder.mView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ProgressBar progressBarImg;
        private TextView userDescView;
        private TextView userNameView;
        private ImageView userImageView;
        private TextView userDate;

        private NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            if (progressBarImg != null)
                progressBarImg = null;
            progressBarImg = mView.findViewById(R.id.progress_item);

        }

        private void setDesc(String desc) {
            if (userDescView != null)
                userDescView.setText("");
            userDescView = mView.findViewById(R.id.list_desc);
            userDescView.setText(desc);
        }

        public void setName(String name) {
            if (userNameView != null)
                userNameView.setText("");
            userNameView = mView.findViewById(R.id.list_name);
            userNameView.setText(name);

        }

        private void setUserImage(final String thumb_image, Context ctx) {
            Log.i("SLIKA", thumb_image);
            progressBarImg.setVisibility(View.VISIBLE);
            if (userImageView != null)
                userImageView.setImageResource(android.R.color.transparent);
            userImageView = mView.findViewById(R.id.list_img);
            final Picasso picasso = Picasso.with(ctx);
            picasso.setIndicatorsEnabled(false);
            picasso.load(thumb_image).networkPolicy(OFFLINE)
                    .placeholder(R.drawable.white_img)
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
                                    .placeholder(R.drawable.white_img)
                                    .into(userImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBarImg.setVisibility(View.GONE);
                                            //Bilo je pre invisible
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

        }

        public void setDate(String date) {
            if (userDate != null)
                userDate.setText("");
            userDate = mView.findViewById(R.id.list_date);
            userDate.setText(date);
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
