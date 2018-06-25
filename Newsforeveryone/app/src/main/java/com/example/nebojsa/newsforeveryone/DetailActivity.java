package com.example.nebojsa.newsforeveryone;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView image;

    private String detail_image;
    private String url;
    private int index,top;

    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        String detail_description = getIntent().getStringExtra("text_key");
        String description_done = formatDescription(detail_description);
        String detail_title = getIntent().getStringExtra("title_key");
        String title_done = formatTitle(detail_title);
        String detail_author = getIntent().getStringExtra("author_key");
        String author_done = formatAuthor(detail_author);
        String detail_time = getIntent().getStringExtra("time_key");
        String time_done = formatTime(detail_time);
        detail_image = getIntent().getStringExtra("image_key");
        index = getIntent().getIntExtra("index", 0);
        top = getIntent().getIntExtra("top",0);
        url = getIntent().getStringExtra("web");
        //state = getIntent().getParcelableExtra("state");

        TextView description = findViewById(R.id.detail_description);
        TextView title = findViewById(R.id.detail_title);
        TextView author = findViewById(R.id.detail_author);
        TextView time = findViewById(R.id.detail_time);
        image =  findViewById(R.id.image_detail);
        Button btn_url = findViewById(R.id.detail_btn_url);

        description.setText(description_done);
        title.setText(title_done);
        author.setText(author_done);
        time.setText(time_done);

        progressBar = findViewById(R.id.loading_indicator_img_detail);


        progressBar.setVisibility(View.VISIBLE);

        Picasso.with(DetailActivity.this).load(detail_image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        Picasso.with(DetailActivity.this).load(detail_image)
                                .into(image);
                    }
                });
        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to view the earthquake URI
                Uri myUri = Uri.parse(url);
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, myUri);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    private String formatTime(String time) {
        //skidanje 10. i 19. karaktera
        String newTime;
        if(time.length()==7){
            newTime="Unknown time";

        }else {
            newTime = time.substring(0, 10) + " " + time.substring(11);
            newTime = newTime.substring(0, 19);

        }

        return newTime;
    }
    private String formatAuthor(String author) {

        return "Author: " + author;
    }

    private String formatTitle(String title) {
        if(title.length()==0){
            title="Unknown";
        }
        return title;
    }

    private String formatDescription(String description) {
        if(description.length()==0){
            description="Unknown";
        }
        return description;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public Intent getParentActivityIntent() {
        Intent intent = new Intent(this,NewsActivity.class);
        intent.putExtra("index_2", index);
        intent.putExtra("top_2", top);
        //intent.putExtra("state_2", state);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }


}
