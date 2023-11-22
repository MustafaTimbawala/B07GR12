package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    }

    public void onClickHello(View helloButton) {
        DateTime dt = new DateTime(2023, 11, 21, 22, 9);
        db.child("Time").setValue(dt);
        db.child("Time").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DateTime dt = task.getResult().getValue(DateTime.class);
                    Log.d("testDateTime", dt.getYear() + " " + dt.getMonth() + " " + dt.getDay() + " " + dt.getHour() + " " + dt.getMinute());
                }
            }
        });
    }

}