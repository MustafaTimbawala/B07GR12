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

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    void invalidUsernamePasswordAlert() {
        Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_LONG).show();
    }

    void usernameTakenAlert() {
        Toast.makeText(this, "Username is taken. Please try another.", Toast.LENGTH_LONG).show();
    }

    void errorAlert() {
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
    }

    void addUser(String username, String password, boolean admin) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        User user = new User(hashedPassword, admin);
        db.child("Users").child(username).setValue(user);
        Toast.makeText(this, "Account successfully created!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void onClickSignUp(View signUpButton) {
        EditText usernameTextBox = findViewById(R.id.usernameTextBox);
        EditText passwordTextBox = findViewById(R.id.passwordTextBox);
        Switch adminSwitch = findViewById(R.id.adminSwitch);
        String username = usernameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        boolean admin = adminSwitch.isChecked();
        if (username.length() > 0 && password.length() > 0) {
            db.child("Users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().getValue(User.class);
                        if (user == null) {
                            addUser(username, password, admin);
                        } else {
                            usernameTakenAlert();
                        }
                    } else {
                        errorAlert();
                    }
                }
            });
        } else {
            invalidUsernamePasswordAlert();
        }
    }

    public void onClickSignIn(View signInButton) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}

