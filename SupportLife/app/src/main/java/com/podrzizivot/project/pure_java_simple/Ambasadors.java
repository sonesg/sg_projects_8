package com.podrzizivot.project.pure_java_simple;



public class Ambasadors {
    public String name;
    public String image;
    public String date;

    public Ambasadors(String name, String image, String date) {
        this.name = name;
        this.image = image;
        this.date = date;
    }
    public Ambasadors(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
