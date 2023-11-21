package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    private boolean checkUsernameAndPassword(String username, String password) {
        //TO DO
        return false;
    }

    public void onClickSignIn(View signInButton) {
        EditText usernameTextBox = findViewById(R.id.usernameTextBox);
        EditText passwordTextBox = findViewById(R.id.passwordTextBox);
        String username = usernameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        final boolean[] userExists = {false};
        final boolean[] storedPassword = {false};
        db.child("Users").child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    userExists[0] = false;
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        //Log.d("username", username);
        //Log.d("password", password);
        if (checkUsernameAndPassword(username, password)) {
            Toast.makeText(this, "Welcome back " + username + "!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Wrong username or password. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSignUp(View signUpButton) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}