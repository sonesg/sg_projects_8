package com.podrzizivot.project.pure_java_simple;

/**
 * Created by Korisnik on 2/16/2018.
 */

public class News {
    String name;
    String description;
    String image;
    String date;

    public News(String name, String description, String image, String date) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.date = date;
    }
    public News(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
