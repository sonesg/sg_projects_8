package com.podrzizivot.project.pure_java_simple;

/**
 * Created by Korisnik on 2/28/2018.
 */

public class Help {
    String name;
    String description;
    String number;
    String image;
    String date;

    public Help(String name, String description, String number, String image, String date) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.image = image;
        this.date = date;
    }
    public Help(){

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
