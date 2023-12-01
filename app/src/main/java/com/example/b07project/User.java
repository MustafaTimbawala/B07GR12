package com.example.b07project;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String password; //HASHED password
    private boolean admin;
    private List<String> events; //list of event names with NO duplicates

    public User() {} //need this to write to database

    public User(String password, boolean admin) {
        this.password = password;
        this.admin = admin;
        this.events = new ArrayList<String>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean addEvent(String eventName) {
        if (!events.contains(eventName)) {
            events.add(eventName);
            return true;
        }
        return false;
    }

    public boolean removeEvent(String eventName) {
        if (events.contains(eventName)) {
            events.remove(eventName);
            return true;
        }
        return false;
    }

    public List<String> getEvents() {
        return events;
    }

}
