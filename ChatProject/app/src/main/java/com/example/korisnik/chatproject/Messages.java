package com.example.korisnik.chatproject;

/**
 * Created by Korisnik on 2/2/2018.
 */

public class Messages {
    String message,type;
    Long time;
    Boolean seen;
    String from;

    public Messages(String from){
        this.from = from;
    }
    public Messages(String message, Long time, Boolean seen, String type) {
        this.message = message;
        this.time = time;
        this.seen = seen;
        this.type = type;
    }
    public Messages(){

    }

    public String getMessage() {
        return message;
    }

    public Long getTime() {
        return time;
    }

    public Boolean getSeen() {
        return seen;
    }

    public String getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
