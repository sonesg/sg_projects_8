package com.podrzizivot.project.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.podrzizivot.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FondationConntactFragment extends Fragment {

    private View mView;
    private Button sendMail;
    private TextView textViewSite;
    private TextView textViewSekretar;
    private TextView textViewKatarina;

    public FondationConntactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fondation_conntact, container, false);

        sendMail = mView.findViewById(R.id.send_email);
        textViewSite = mView.findViewById(R.id.site_p_z);
        textViewSekretar = mView.findViewById(R.id.send_mail_sekretar);
        textViewKatarina = mView.findViewById(R.id.send_mail_katarina);

        //textViewSite.setMovementMethod(LinkMovementMethod.getInstance());

        textViewSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.podrzizivot.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

//        if(getActivity()!=null) {
//            getActivity().setTitle("Kontakt");
//        }

        //final ExplosionField explosionField = ExplosionField.attach2Window(getActivity());

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //explosionField.explode(sendMail);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "kontakt@podrzizivot.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                // need this to prompts email client only
//                intent.setType("message/rfc822");
//                intent.putExtra(Intent.EXTRA_EMAIL, "kontakt@podrzizivot.com");
//
//                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        textViewSekretar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "sekretar@podrzizivot.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        textViewKatarina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "katarina.danojlic@podrzizivot.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();
//        sendMail.setOnClickListener(null);
//        textViewSite.setOnClickListener(null);
//        textViewSekretar.setOnClickListener(null);
//        textViewKatarina.setOnClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
        sendMail.setOnClickListener(null);
        textViewSite.setOnClickListener(null);
        textViewSekretar.setOnClickListener(null);
        textViewKatarina.setOnClickListener(null);
    }
}
