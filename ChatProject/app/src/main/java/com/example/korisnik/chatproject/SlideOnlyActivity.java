package com.example.korisnik.chatproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.korisnik.chatproject.registration.StartActivity;

public class SlideOnlyActivity extends AppCompatActivity {

    private Button nextbtn,backbtn;
    private ViewPager viewPager;
    private LinearLayout linearLayout;

    private TextView[] dots;

    private SliderAdapter sliderAdapter;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_only);

        viewPager = (ViewPager)findViewById(R.id.slide_pager_only_one_layout);
        linearLayout = (LinearLayout)findViewById(R.id.dotsLayout);
        backbtn = (Button) findViewById(R.id.slide_btn1);
        nextbtn = (Button) findViewById(R.id.slide_btn2);

        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(onPageChangeListener);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage == dots.length-1){
                    Intent intent = new Intent(SlideOnlyActivity.this, StartActivity.class);
                    intent.putExtra("first_launch", false);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    viewPager.setCurrentItem(currentPage + 1);
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });
    }
    public void addDotsIndicator(int position){
        dots = new TextView[3];
        linearLayout.removeAllViews();

        for(int i = 0; i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.TransparentWhite));
            linearLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;
            if(position == 0){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(false);
                backbtn.setVisibility(View.INVISIBLE);
                nextbtn.setText("Next");
                backbtn.setText("");

            }else if(dots.length - 1 == position){
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);
                nextbtn.setText("Finish");
                backbtn.setText("Back");
            }else{
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
                backbtn.setVisibility(View.VISIBLE);
                nextbtn.setText("Next");
                backbtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
