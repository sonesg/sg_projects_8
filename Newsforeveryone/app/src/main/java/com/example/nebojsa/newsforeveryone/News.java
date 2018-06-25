package com.example.nebojsa.newsforeveryone;


public class News {


    private String mAuthor;


    private String mTitle;

    private String mDescription;


    private String mTime;

    private String mUrl;

    private String mUrltoImage;


    News(String author, String title, String time, String urlToImage, String description, String url)
    {
        mAuthor = author;
        mTitle = title;
        mUrltoImage = urlToImage;
        mDescription = description;
        mTime = time;
        mUrl = url;
    }

//    public News(String author, String title,String UrltoImage, String time, String description)
//    {
//        mAuthor = author;
//        mTitle = title;
//        mUrltoImage = UrltoImage;
//        mDescription = description;
//        mTime = time;
//    }


    String getAuthor() {
        return mAuthor;
    }


    public String getTitle() {
        return mTitle;
    }


    public String getTime() {
        return mTime;
    }

    String getUrl(){
        return mUrl;
    }

    String getUrlToImage(){
        return mUrltoImage;
    }

    String getDescription(){
        return mDescription;
    }


}