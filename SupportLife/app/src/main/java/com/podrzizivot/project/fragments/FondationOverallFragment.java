package com.podrzizivot.project.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.podrzizivot.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FondationOverallFragment extends Fragment {

    private View mView;
    private ImageButton btnEur;
    private ImageButton btnUsd;
    private ImageButton btnLocation;


    public FondationOverallFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_fondation_overall, container, false);

        btnEur = mView.findViewById(R.id.doc_eur);
        btnUsd = mView.findViewById(R.id.doc_usd);
        btnLocation = mView.findViewById(R.id.maps_location);

//        if(getActivity()!=null) {
//            getActivity().setTitle("O nama");
//        }


        btnEur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.podrzizivot.com/wp-content/uploads/2016/12/Uputstvo-za-uplatu-EUR.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btnUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.podrzizivot.com/wp-content/uploads/2016/12/Uputstvo-za-uplatu-USD.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "geo:0,0?q=44.816333,20.483461(Treasure)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();
//        btnEur.setOnClickListener(null);
//        btnUsd.setOnClickListener(null);
//        btnLocation.setOnClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
        btnEur.setOnClickListener(null);
        btnUsd.setOnClickListener(null);
        btnLocation.setOnClickListener(null);
    }
}
