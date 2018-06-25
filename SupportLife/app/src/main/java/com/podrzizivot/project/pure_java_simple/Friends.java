package com.podrzizivot.project.pure_java_simple;

/**
 * Created by Korisnik on 3/2/2018.
 */

public class Friends {
    public String image;
    public String date;

    public Friends(String image, String date) {
        this.image = image;
        this.date = date;
    }
    public Friends(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
