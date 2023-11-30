package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;

public class HomeActivity extends AppCompatActivity {

    String username;
    boolean isAdmin;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        username = getIntent().getStringExtra("username");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
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
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.postCheckerButton) {
                    //TO DO
                    return true;
                } else if (itemId == R.id.signOutButton) {
                    Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}