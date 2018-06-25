package com.podrzizivot.project;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


public class Offline extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//            // Normal app init code...


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // --------PICASSO--------
        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        //-----------------------------
    }
}