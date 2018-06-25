package com.example.korisnik.chatproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Korisnik on 2/13/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    public int[] slide_images = {
            R.drawable.circle_slide_1,
            R.drawable.circle_slide_2,
            R.drawable.circle_slide_3
    };

    public String[] slide_headings = {
            "FIRST HEADLINE",
            "SECOND HEADLINE",
            "THIRD HEADLINE"
    };

    public String[] slide_descriptions = {
            "This is example of description text below the headline text in my android chat app.This is first text",
            "This is example of description text below the headline text in my android chat app.This is second text",
            "This is example of description text below the headline text in my android chat app.This is third text"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView textViewHead = (TextView) view.findViewById(R.id.slide_head_text);
        TextView textViewDesc = (TextView) view.findViewById(R.id.slide_description_text);

        imageView.setImageResource(slide_images[position]);
        textViewHead.setText(slide_headings[position]);
        textViewDesc.setText(slide_descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
