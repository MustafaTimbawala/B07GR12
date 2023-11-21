package com.example.b07project;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    String password;
    boolean isAdmin;
    List<String> events;

    public UserData() {}

    public UserData(String password, boolean isAdmin) {
        this.password = password;
        this.isAdmin = isAdmin;
        this.events = new ArrayList<String>();
    }

    


}
