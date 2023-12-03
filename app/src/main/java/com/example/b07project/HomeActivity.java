package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    String username;
    boolean admin;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        username = getIntent().getStringExtra("username");
        admin = getIntent().getBooleanExtra("admin", false);
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();

        //profile button with pop-up menu for POST checker and sign out
        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setText(username.substring(0, 1).toUpperCase());
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        TextView pageTitle = findViewById(R.id.pageTitle);
        //navigate to announcements
        LinearLayout navToHome = findViewById(R.id.navToHome);
        navToHome.setClickable(true);
        navToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, ViewAnnouncementFragment.newInstance(username, admin));
                fragmentTransaction.commit();
                pageTitle.setText("Announcements");
            }
        });

        //navigate to events
        LinearLayout navToEvents = findViewById(R.id.navToEvents);
        navToEvents.setClickable(true);
        navToEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, EventsFragment.newInstance(username, admin));
                fragmentTransaction.commit();
                pageTitle.setText("Events");
            }
        });

        //navigate to complaints
        LinearLayout navToComplaints = findViewById(R.id.navToComplaints);
        navToComplaints.setClickable(true);
        navToComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, ViewComplaintFragment.newInstance(username, admin));
                fragmentTransaction.commit();
                pageTitle.setText("Complaints");
            }
        });

        //announcements page is the home page
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, ViewAnnouncementFragment.newInstance(username, admin));
        fragmentTransaction.commit();
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.postCheckerButton) {
                    // Navigate to PostFragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragment, new PostFragment());
                    fragmentTransaction.addToBackStack(null); // Optional, if you want back button to return to previous fragment
                    fragmentTransaction.commit();
                    return true;
                } else if (itemId == R.id.signOutButton) {
                    // Sign out logic
                    Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish(); // Finish the current activity
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}