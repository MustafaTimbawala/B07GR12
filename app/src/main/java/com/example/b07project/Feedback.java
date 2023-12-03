package com.example.b07project;

public class Feedback {
    String comment;
    float rating;

    public Feedback(String comment, float rating )  {

        this.comment = comment;
        this.rating = rating;

    }

    public String getComment(){return this.comment;}

    public float getRating(){return this.rating;}



}
