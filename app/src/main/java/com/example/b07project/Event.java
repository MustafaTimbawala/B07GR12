package com.example.b07project;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String title;
    private DateTime date;
    private String description;
    private int capacity;
    private List<String> registeredUsers;

    public Event() { //need this to write to database
        this.registeredUsers = new ArrayList<String>();
    }

    public Event(String title, DateTime date, String description, int capacity) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.capacity = capacity;
        this.registeredUsers = new ArrayList();
    }

    public String getTitle() {
        return title;
    }

    public DateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getRegisteredUsers() {
        return registeredUsers;
    }

    public boolean registerUser(String username) {
        if (registeredUsers.size() < capacity) {
            registeredUsers.add(username);
            return true;
        }
        return false;
    }

    public void unregisterUser(String username) {
        registeredUsers.remove(username);
    }

}
