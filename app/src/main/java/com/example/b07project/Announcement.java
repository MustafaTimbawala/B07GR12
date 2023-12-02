package com.example.b07project;
public class Announcement {
    private String Announcer;
    private String Date;
    private String Content;
    private String Title;

    private String EventTitle;
    private boolean isEvent;

    public Announcement(){}

    public Announcement(String Announcer, String Date, String Content, String Title, String EventTitle, boolean isEvent){
        this.Announcer = Announcer;
        this.Date = Date;
        this.Content = Content;
        this.Title = Title;
        this.EventTitle = EventTitle;
        this.isEvent = isEvent;

    }

    public String getAnnouncer(){
        return this.Announcer;
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

    public String getEventTitle(){return this.EventTitle;}

    public boolean getEvent(){return this.isEvent;}
    public void setAnnouncer(String Username){
        this.Announcer = Username;
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

    public void setEventTitle(String EventTitle){this.EventTitle = EventTitle;}

    public void setEvent(boolean isEvent){this.isEvent = isEvent;}
}
