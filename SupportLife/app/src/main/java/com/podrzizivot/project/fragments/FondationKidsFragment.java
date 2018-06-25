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
import android.widget.FrameLayout;
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
import com.podrzizivot.project.pure_java_simple.Kids;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FondationKidsFragment extends Fragment {

    private RecyclerView mKidsList;

    private DatabaseReference mKidsDatabase;

    private View progressBar;

    private FirebaseRecyclerOptions<Kids> options;

    private TextView empty_list;

    private ImageView no_network_img;

    private Toast toast;

    private View mMainView;

    private View view;

    private FirebaseDatabase firebaseDatabase;

    private ValueEventListener valueEventListener;

    private FirebaseRecyclerAdapter<Kids, FondationKidsFragment.KidsViewHolder> firebaseRecyclerAdapterKids;


    public FondationKidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_fondation_kids, container, false);
//        if (getActivity() != null) {
//            getActivity().setTitle("Pomozimo deci");
//        }

//        if(getActivity().getIntent()!=null){
//            //index = getIntent().getIntExtra("index", 0);
//            //Parcelable object = getArguments().getParcelable("index_parcelable");
//
//        }
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//             index = bundle.getInt("index_bundle", 0);
//        }

        mKidsList = mMainView.findViewById(R.id.kids_list);
        progressBar = mMainView.findViewById(R.id.progress_kids);
        progressBar.setVisibility(View.VISIBLE);
        empty_list = mMainView.findViewById(R.id.text_no_network);
        no_network_img = mMainView.findViewById(R.id.image_no_network);

        //PROBLEM KOD PROGRESS BAR-A JE TO STO JE ON SVE VREME RADI,ALI JE PREKRIVEN DRUGIM SADRZAJEM PA SE NE VIDI
        //TO MOZE DOVESTI DO TROSENJA RESURSA TELEFONA
        firebaseDatabase = FirebaseDatabase.getInstance();
        mKidsDatabase = firebaseDatabase.getReference().child("Users").child("Kids");
//        String kidId = mKidsDatabase.getKey();
        mKidsDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mKidsList.setHasFixedSize(true);
        mKidsList.setLayoutManager(linearLayoutManager);

        Query kidsQuery = mKidsDatabase.orderByChild("date");

        options = new FirebaseRecyclerOptions.Builder<Kids>()
                .setQuery(kidsQuery, Kids.class)
                .build();

//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("image", "https://www.w3schools.com/howto/img_avatar.png");
//        userMap.put("name", "Pomoc Marku Markovicu");
//        userMap.put("desc", "Hi, there.I'm using project app");
//        userMap.put("date", "10/2/2018");
//
//        Map kidsMap = new HashMap();
//        kidsMap.put("Users/" + kidId, userMap);
//
//        mKidsDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            progressBar.setVisibility(View.VISIBLE);
            no_network_img.setVisibility(View.GONE);
            empty_list.setVisibility(View.GONE);
            mKidsList.setVisibility(View.VISIBLE);


            firebaseRecyclerAdapterKids = new FirebaseRecyclerAdapter<Kids, KidsViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final KidsViewHolder holder, int position, @NonNull Kids model) {
                    final String list_user_id = getRef(position).getKey();

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String kidImage;
                            final String kidName;
                            final String kidDesc;
                            final String kidDate;
                            final String imageDetail_1;
                            final String imageDetail_2;
                            final String imageDetail_3;
                            final String imageDetail_4;
                            final Picasso picasso = Picasso.with(getContext());
                            picasso.setIndicatorsEnabled(false);
                            Object kidImageObject = dataSnapshot.child("image").getValue();
                            if (kidImageObject != null) {
                                kidImage = kidImageObject.toString();
                            } else {
                                kidImage = "No Image";
                            }
                            Object kidNameObject = dataSnapshot.child("name").getValue();
                            if (kidNameObject != null) {
                                kidName = kidNameObject.toString();
                            } else {
                                kidName = "No name";
                            }
                            Object kidDescObject = dataSnapshot.child("desc").getValue();
                            if (kidDescObject != null) {
                                kidDesc = kidDescObject.toString();
                            } else {
                                kidDesc = "No description";
                            }
                            Object kidDateObject = dataSnapshot.child("date").getValue();
                            if (kidDateObject != null) {
                                kidDate = kidDateObject.toString();
                            } else {
                                kidDate = "No date";
                            }
                            Object kidImg1Object = (dataSnapshot.child("image-detail-1").getValue());
                            if (kidImg1Object != null) {
                                imageDetail_1 = kidImg1Object.toString();
                            } else {
                                imageDetail_1 = "No img";
                            }
                            Object kidImg2Object = dataSnapshot.child("image-detail-2").getValue();
                            if (kidImg2Object != null) {
                                imageDetail_2 = kidImg2Object.toString();
                            } else {
                                imageDetail_2 = "No img";
                            }
                            Object kidImg3Object = dataSnapshot.child("image-detail-3").getValue();
                            if (kidImg3Object != null) {
                                imageDetail_3 = kidImg3Object.toString();
                            } else {
                                imageDetail_3 = "No img";
                            }
                            Object kidImg4Object = dataSnapshot.child("image-detail-4").getValue();
                            if (kidImg4Object != null) {
                                imageDetail_4 = kidImg4Object.toString();
                            } else {
                                imageDetail_4 = "No img";
                            }

                            if (kidName != null)
                                holder.setName(kidName);
                            if (kidImage != null)
                                holder.setUserImage(kidImage, getContext());
                            if (kidDesc != null)
                                holder.setDesc(kidDesc);
                            if (kidDate != null)
                                holder.setDate(kidDate);

                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (getContext() != null) {
                                        final Dialog settingsDialog = new Dialog(getContext());
                                        if (settingsDialog.getWindow() != null) {
                                            settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                        }
                                        settingsDialog.setContentView(View.inflate(getContext(), R.layout.dialog_layout_kids_list
                                                , null));
                                        final ImageView imageView = settingsDialog.findViewById(R.id.dialog_image_kids_list);
                                        picasso.load(kidImage).networkPolicy(NetworkPolicy.OFFLINE)
                                                .placeholder(R.drawable.white_img)
                                                .into(imageView);
                                        final TextView name_dialog = settingsDialog.findViewById(R.id.dialog_name_kids_list);
                                        name_dialog.setText(kidName);
                                        final TextView date_dialog = settingsDialog.findViewById(R.id.dialog_date_kids_list);
                                        date_dialog.setText(kidDate);
                                        final TextView desc_dialog = settingsDialog.findViewById(R.id.dialog_desc_kids_list);
                                        desc_dialog.setText(kidDesc);

                                        final ImageView detailImg1 = settingsDialog.findViewById(R.id.kidsDetail_imgDet1_dialog);
                                        final ImageView detailImg2 = settingsDialog.findViewById(R.id.kidsDetail_imgDet2_dialog);
                                        final ImageView detailImg3 = settingsDialog.findViewById(R.id.kidsDetail_imgDet3_dialog);
                                        final ImageView detailImg4 = settingsDialog.findViewById(R.id.kidsDetail_imgDet4_dialog);
                                        detailImg1.setVisibility(View.GONE);
                                        detailImg2.setVisibility(View.GONE);
                                        detailImg3.setVisibility(View.GONE);
                                        detailImg4.setVisibility(View.GONE);
                                        final ProgressBar progressBar1 = settingsDialog.findViewById(R.id.progress_kids_detail1_dialog);
                                        final ProgressBar progressBar2 = settingsDialog.findViewById(R.id.progress_kids_detail2_dialog);
                                        final ProgressBar progressBar3 = settingsDialog.findViewById(R.id.progress_kids_detail3_dialog);
                                        final ProgressBar progressBar4 = settingsDialog.findViewById(R.id.progress_kids_detail4_dialog);
                                        final FrameLayout frameLayout1 = settingsDialog.findViewById(R.id.frame_kids1_dialog);
                                        final FrameLayout frameLayout2 = settingsDialog.findViewById(R.id.frame_kids2_dialog);
                                        final FrameLayout frameLayout3 = settingsDialog.findViewById(R.id.frame_kids3_dialog);
                                        final FrameLayout frameLayout4 = settingsDialog.findViewById(R.id.frame_kids4_dialog);
                                        final Dialog[] settingsDetailDialog = new Dialog[4];
                                        ////////////////////////////////////////////////////////////////////////////////////////////////
                                        final View.OnClickListener onClickListenerDissmis = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //   imageView.setImageResource(android.R.color.transparent);
                                                if (settingsDetailDialog[0] != null) {
                                                    Log.i("PrvaSlikaDetail", settingsDetailDialog[0].toString());
                                                    settingsDetailDialog[0].dismiss();
                                                }
                                                if (settingsDetailDialog[1] != null) {
                                                    Log.i("PrvaSlikaDetail", settingsDetailDialog[1].toString());
                                                    settingsDetailDialog[1].dismiss();
                                                }
                                                if (settingsDetailDialog[2] != null) {
                                                    Log.i("PrvaSlikaDetail", settingsDetailDialog[2].toString());
                                                    settingsDetailDialog[2].dismiss();
                                                }
                                                if (settingsDetailDialog[3] != null) {
                                                    Log.i("PrvaSlikaDetail", settingsDetailDialog[3].toString());
                                                    settingsDetailDialog[3].dismiss();
                                                }
                                            }
                                        };
                                        ////////////////////////////////////////////////////////////////////////////////////////////////
                                        final View.OnClickListener onClickListener0 = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                settingsDetailDialog[0] = new Dialog(getContext());
                                                if (settingsDetailDialog[0].getWindow() != null) {
                                                    settingsDetailDialog[0].getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                                }
                                                settingsDetailDialog[0].setContentView(View.inflate(getContext(), R.layout.dialog_layout
                                                        , null));
                                                final ImageView imageView = settingsDetailDialog[0].findViewById(R.id.dialog_image);
                                                picasso.load(imageDetail_1).networkPolicy(NetworkPolicy.OFFLINE)
                                                        .placeholder(R.drawable.white_img)
                                                        .into(imageView);
                                                settingsDetailDialog[0].show();
                                                imageView.setOnClickListener(onClickListenerDissmis);
                                            }
                                        };
                                        ///////////////////////////////////////////////////////////////////////////////////////////////
                                        final View.OnClickListener onClickListener1 = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                settingsDetailDialog[1] = new Dialog(getContext());
                                                if (settingsDetailDialog[1].getWindow() != null) {
                                                    settingsDetailDialog[1].getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                                }
                                                settingsDetailDialog[1].setContentView(View.inflate(getContext(), R.layout.dialog_layout
                                                        , null));
                                                final ImageView imageView = settingsDetailDialog[1].findViewById(R.id.dialog_image);
                                                picasso.load(imageDetail_2).networkPolicy(NetworkPolicy.OFFLINE)
                                                        .placeholder(R.drawable.white_img)
                                                        .into(imageView);
                                                settingsDetailDialog[1].show();
                                                imageView.setOnClickListener(onClickListenerDissmis);
                                            }
                                        };
                                        //////////////////////////////////////////////////////////////////////////////////////////////
                                        final View.OnClickListener onClickListener2 = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                settingsDetailDialog[2] = new Dialog(getContext());
                                                if (settingsDetailDialog[2].getWindow() != null) {
                                                    settingsDetailDialog[2].getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                                }
                                                settingsDetailDialog[2].setContentView(View.inflate(getContext(), R.layout.dialog_layout
                                                        , null));
                                                final ImageView imageView = settingsDetailDialog[2].findViewById(R.id.dialog_image);
                                                picasso.load(imageDetail_3).networkPolicy(NetworkPolicy.OFFLINE)
                                                        .placeholder(R.drawable.white_img)
                                                        .into(imageView);
                                                settingsDetailDialog[2].show();
                                                imageView.setOnClickListener(onClickListenerDissmis);
                                            }
                                        };
                                        /////////////////////////////////////////////////////////////////////////////////////////////
                                        final View.OnClickListener onClickListener3 = new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //final Dialog settingsDetailDialog = new Dialog(getContext());
                                                settingsDetailDialog[3] = new Dialog(getContext());
                                                if (settingsDetailDialog[3].getWindow() != null) {
                                                    settingsDetailDialog[3].getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                                }
                                                settingsDetailDialog[3].setContentView(View.inflate(getContext(), R.layout.dialog_layout
                                                        , null));
                                                final ImageView imageView = settingsDetailDialog[3].findViewById(R.id.dialog_image);
                                                picasso.load(imageDetail_4).networkPolicy(NetworkPolicy.OFFLINE)
                                                        .placeholder(R.drawable.white_img)
                                                        .into(imageView);
                                                settingsDetailDialog[3].show();
                                                imageView.setOnClickListener(onClickListenerDissmis);
                                            }
                                        };
                                        ////////////////////////////////////////////////////////////////////////////////////////////
                                        if (!imageDetail_1.equals("")) {
                                            frameLayout1.setVisibility(View.VISIBLE);
                                            detailImg1.setVisibility(View.VISIBLE);
                                            progressBar1.setVisibility(View.VISIBLE);

                                            picasso.load(imageDetail_1).networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.white_img)
                                                    .into(detailImg1, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            progressBar1.setVisibility(View.GONE);
                                                            detailImg1.setOnClickListener(onClickListener0);
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            picasso.load(imageDetail_1).networkPolicy(NetworkPolicy.NO_CACHE)
                                                                    .placeholder(R.drawable.white_img)
                                                                    .into(detailImg1, new Callback() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            progressBar1.setVisibility(View.GONE);
                                                                        }

                                                                        @Override
                                                                        public void onError() {

                                                                        }
                                                                    });
                                                            detailImg1.setOnClickListener(onClickListener0);
                                                        }
                                                    });

                                        } else {
                                            detailImg1.setVisibility(View.GONE);
                                            progressBar1.setVisibility(View.GONE);
                                            frameLayout1.setVisibility(View.GONE);
                                        }
                                        if (!imageDetail_2.equals("")) {
                                            frameLayout2.setVisibility(View.VISIBLE);
                                            detailImg2.setVisibility(View.VISIBLE);
                                            progressBar2.setVisibility(View.VISIBLE);

                                            picasso.load(imageDetail_2).networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.white_img)
                                                    .into(detailImg2, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            progressBar2.setVisibility(View.GONE);
                                                            detailImg2.setOnClickListener(onClickListener1);
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            picasso.load(imageDetail_2).networkPolicy(NetworkPolicy.NO_CACHE)
                                                                    .placeholder(R.drawable.white_img)
                                                                    .into(detailImg2, new Callback() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            progressBar2.setVisibility(View.GONE);
                                                                        }

                                                                        @Override
                                                                        public void onError() {

                                                                        }
                                                                    });
                                                            detailImg2.setOnClickListener(onClickListener1);
                                                        }
                                                    });

                                        } else {
                                            detailImg2.setVisibility(View.GONE);
                                            progressBar2.setVisibility(View.GONE);
                                            frameLayout2.setVisibility(View.GONE);
                                        }
                                        if (!imageDetail_3.equals("")) {
                                            frameLayout3.setVisibility(View.VISIBLE);
                                            detailImg3.setVisibility(View.VISIBLE);
                                            progressBar3.setVisibility(View.VISIBLE);

                                            picasso.load(imageDetail_3).networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.white_img)
                                                    .into(detailImg3, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            progressBar3.setVisibility(View.GONE);
                                                            detailImg3.setOnClickListener(onClickListener2);
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            picasso.load(imageDetail_3).networkPolicy(NetworkPolicy.NO_CACHE)
                                                                    .placeholder(R.drawable.white_img)
                                                                    .into(detailImg3, new Callback() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            progressBar3.setVisibility(View.GONE);
                                                                        }

                                                                        @Override
                                                                        public void onError() {

                                                                        }
                                                                    });

                                                            detailImg3.setOnClickListener(onClickListener2);
                                                        }
                                                    });

                                        } else {
                                            detailImg3.setVisibility(View.GONE);
                                            progressBar3.setVisibility(View.GONE);
                                            frameLayout3.setVisibility(View.GONE);
                                        }
                                        if (!imageDetail_4.equals("")) {
                                            frameLayout4.setVisibility(View.VISIBLE);
                                            detailImg4.setVisibility(View.VISIBLE);
                                            progressBar4.setVisibility(View.VISIBLE);

                                            picasso.load(imageDetail_4).networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.white_img)
                                                    .into(detailImg4, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            progressBar4.setVisibility(View.GONE);
                                                            detailImg4.setOnClickListener(onClickListener3);
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            picasso.load(imageDetail_4).networkPolicy(NetworkPolicy.NO_CACHE)
                                                                    .placeholder(R.drawable.white_img)
                                                                    .into(detailImg4, new Callback() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            progressBar4.setVisibility(View.GONE);
                                                                        }

                                                                        @Override
                                                                        public void onError() {

                                                                        }
                                                                    });
                                                            detailImg4.setOnClickListener(onClickListener3);
                                                        }
                                                    });

                                        } else {
                                            detailImg4.setVisibility(View.GONE);
                                            progressBar4.setVisibility(View.GONE);
                                            frameLayout4.setVisibility(View.GONE);
                                        }
                                        final ImageView imageViewBack = settingsDialog.findViewById(R.id.dialog_back_list);
                                        settingsDialog.show();
                                        imageViewBack.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                imageViewBack.setImageResource(android.R.color.transparent);
                                                name_dialog.setText("");
                                                date_dialog.setText("");
                                                desc_dialog.setText("");
                                                detailImg1.setImageResource(android.R.color.transparent);
                                                detailImg2.setImageResource(android.R.color.transparent);
                                                detailImg3.setImageResource(android.R.color.transparent);
                                                detailImg4.setImageResource(android.R.color.transparent);
                                                imageView.setImageResource(android.R.color.transparent);
                                                settingsDialog.dismiss();
                                                if (settingsDetailDialog[0] != null)
                                                    settingsDetailDialog[0].dismiss();
                                                if (settingsDetailDialog[1] != null)
                                                    settingsDetailDialog[1].dismiss();
                                                if (settingsDetailDialog[2] != null)
                                                    settingsDetailDialog[2].dismiss();
                                                if (settingsDetailDialog[3] != null)
                                                    settingsDetailDialog[3].dismiss();
                                                detailImg1.setOnClickListener(null);
                                                detailImg2.setOnClickListener(null);
                                                detailImg3.setOnClickListener(null);
                                                detailImg4.setOnClickListener(null);
                                            }
                                        });
                                    }
                                }
                            });
//                            holder.mView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
////                                    index = linearLayoutManager.findFirstVisibleItemPosition();
////                                    Log.i("POREDU", index + "");
//                                    Intent intent = new Intent(getContext(), DetailKidsActivity.class);
//                                    intent.putExtra("imageD_1", imageDetail_1);
//                                    intent.putExtra("imageD_2", imageDetail_2);
//                                    intent.putExtra("imageD_3", imageDetail_3);
//                                    intent.putExtra("imageD_4", imageDetail_4);
//                                    intent.putExtra("name", kidName);
//                                    intent.putExtra("image", kidImage);
//                                    intent.putExtra("date", kidDate);
//                                    intent.putExtra("desc", kidDesc);
////                                    intent.putExtra("index", index);
//                                    startActivity(intent);
//                                }
//                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    mKidsDatabase.child(list_user_id).addValueEventListener(valueEventListener);

                }

                @NonNull
                @Override
                public KidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.user_single_item, parent, false);
                    return new FondationKidsFragment.KidsViewHolder(view);
                }
            };

            firebaseRecyclerAdapterKids.notifyDataSetChanged();
            mKidsList.setAdapter(firebaseRecyclerAdapterKids);
            firebaseRecyclerAdapterKids.startListening();

        } else {
            if (firebaseRecyclerAdapterKids != null) {
                firebaseRecyclerAdapterKids.notifyDataSetChanged();
                mKidsList.setAdapter(null);
                firebaseRecyclerAdapterKids.stopListening();
            }
            if (mKidsDatabase != null && valueEventListener != null) {
                mKidsDatabase.removeEventListener(valueEventListener);
            }
            if (firebaseDatabase != null)
                firebaseDatabase.goOffline();
            mKidsList.setVisibility(View.GONE);
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
                    progressBar.setVisibility(View.VISIBLE);
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
        if (firebaseRecyclerAdapterKids != null) {
            firebaseRecyclerAdapterKids.notifyDataSetChanged();
            mKidsList.setAdapter(null);
            firebaseRecyclerAdapterKids.stopListening();
            //Log.i("KIDSSSSSTOP", mKidsList.toString() + "-----" + firebaseRecyclerAdapterKids.toString());
        }
        if (mKidsDatabase != null && valueEventListener != null) {
            mKidsDatabase.removeEventListener(valueEventListener);
        }
        view = null;
        mMainView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapterKids != null) {
            firebaseRecyclerAdapterKids.notifyDataSetChanged();
            mKidsList.setAdapter(null);
            firebaseRecyclerAdapterKids.stopListening();
            //  Log.i("KIDSSSSDVIEW", mKidsList.toString() + "-----" + firebaseRecyclerAdapterKids.toString());
        }
        if (mKidsDatabase != null && valueEventListener != null) {
            mKidsDatabase.removeEventListener(valueEventListener);
        }
//        KidsViewHolder friendsViewHolder = new KidsViewHolder(mMainView);
        view = null;
        mMainView = null;
//        friendsViewHolder.mView = null;
        if (firebaseDatabase != null)
            firebaseDatabase.goOffline();
    }

    public static class KidsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ProgressBar progressBarImg;
        private TextView userDescView;
        private TextView userNameView;
        private ImageView userImageView;
        private TextView userDate;

        private KidsViewHolder(View itemView) {
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
            progressBarImg.setVisibility(View.VISIBLE);
            if (userImageView != null)
                userImageView.setImageResource(android.R.color.transparent);
            userImageView = mView.findViewById(R.id.list_img);
            final Picasso picasso = Picasso.with(ctx);
            picasso.setIndicatorsEnabled(false);
            picasso.load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE)
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
                            picasso.load(thumb_image).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .placeholder(R.drawable.white_img)
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
