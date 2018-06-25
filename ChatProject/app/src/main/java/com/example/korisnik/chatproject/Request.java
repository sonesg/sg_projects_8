package com.example.korisnik.chatproject;

/**
 * Created by Korisnik on 2/9/2018.
 */

public class Request {

    String request_type;

    public Request() {
    }

    public Request(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

}
