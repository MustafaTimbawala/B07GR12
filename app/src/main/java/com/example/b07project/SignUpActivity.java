package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    private boolean checkUsername(String username) {
        //TO DO
        return true;
    }
    public void onClickSignUp(View signUpButton) {
        EditText usernameTextBox = findViewById(R.id.usernameTextBox);
        EditText passwordTextBox = findViewById(R.id.passwordTextBox);
        Switch adminSwitch = findViewById(R.id.adminSwitch);
        String username = usernameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        boolean admin = adminSwitch.isChecked();
        final boolean[] userExists = {false};
        db.child("Users").child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    userExists[0] = true;
                }
            }
        });
        //Log.d("username", username);
        //Log.d("password", password);
        if (!userExists[0]) {
            Toast.makeText(this, "Account successfully created!", Toast.LENGTH_LONG).show();
            db.child("User");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Username is taken. Please try another.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSignIn(View signInButton) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}