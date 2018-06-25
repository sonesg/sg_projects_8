package com.example.korisnik.chatproject;

/**
 * Created by Korisnik on 12/24/2017.
 */

public class Users {

    private String name;
    private String image;
    private String status;
    private String thumbImage;

    //without this empty constructor app will be maybe crash
    public Users(){

    }

    public Users(String name, String image, String status, String thumbImage) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumbImage = thumbImage;
    }

    public String getName() {return name;}

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getThumb_image(){
        return thumbImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setThumb_image(String thumbImage){
        this.thumbImage = thumbImage;
    }
}
