package com.example.b07project;

public class Complaint {
    private String Username;
    private String Date;
    private String Content;
    private String Title;

    public Complaint(){}

    public Complaint(String Username, String Date, String Content, String Title){
        this.Username = Username;
        this.Date = Date;
        this.Content = Content;
        this.Title = Title;
    }

    public String getUsername(){
        return this.Username;
    }

    public String getDate(){
        return this.Date;
    }

    public String getContent(){
        return this.Content;
    }
    public String getTitle(){
        return this.Title;
    }

    public void setUsername(String Username){
        this.Username = Username;
    }

    public void setDate(String Date){
        this.Date = Date;
    }

    public void setContent(String Content){
        this.Content = Content;
    }
    public void setTitle(String Title){
        this.Title = Title;
    }

}
