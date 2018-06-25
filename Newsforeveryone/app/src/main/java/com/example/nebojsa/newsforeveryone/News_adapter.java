package com.example.nebojsa.newsforeveryone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class News_adapter extends ArrayAdapter<News> {

    private ArrayList<News> news;
    private Context context;
    private ImageView imageView;

    private View progressBar;



    News_adapter(Context context, ArrayList<News> earthquakes) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, earthquakes);
        this.news = earthquakes;
        this.context = context;

    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        final News currentNews = getItem(position);
        news.add(currentNews);


        if(currentNews!=null) {
            TextView newsAuthor = listItemView.findViewById(R.id.author);
            String formattedAuthor = formatAuthor(currentNews.getAuthor());
            newsAuthor.setText(formattedAuthor);

            TextView newTitle = listItemView.findViewById(R.id.title);
            String formattedTitle = formatTitle(currentNews.getTitle());
            newTitle.setText(formattedTitle);

            TextView newsDescription = listItemView.findViewById(R.id.description);
            String formattedDescription = formatDescription(currentNews.getDescription());
            newsDescription.setText(formattedDescription);

            TextView newsTime = listItemView.findViewById(R.id.time);
            String formattedTime = formatTime(currentNews.getTime());
            newsTime.setText(formattedTime);


            imageView = listItemView.findViewById(R.id.image_from_url);
//        Picasso.with(context).load(currentNews.getUrlToImage()).fit().centerCrop()
//                .placeholder(R.drawable.wait_img)
//                .error(R.drawable.error_img)
//                .into(imageView);
            //Picasso.with(context).load(currentNews.getUrlToImage()).into(imageView);
            // Show progress bar

            progressBar = listItemView.findViewById(R.id.loading_indicator_img);


            progressBar.setVisibility(View.VISIBLE);
// Hide progress bar on successful load
            Picasso.with(context).load(currentNews.getUrlToImage())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                            Picasso.with(context).load(currentNews.getUrlToImage())
                                    .into(imageView);
                        }
                    });

        }
            return listItemView;
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




}




