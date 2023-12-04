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

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignInActivity extends AppCompatActivity {

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    void noSuchUserAlert() {
        Toast.makeText(this, "Username does not exist. Please try again.", Toast.LENGTH_LONG).show();
    }

    void wrongPasswordAlert() {
        Toast.makeText(this, "Incorrect password. Please try again.", Toast.LENGTH_LONG).show();
    }

    void errorAlert() {
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
    }

    void attemptSignIn(String username, String password, User user) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        Log.d("password verified", String.valueOf(result.verified));
        if (result.verified) {
            Toast.makeText(this, "Welcome back " + username + "!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("admin", user.isAdmin());
            startActivity(intent);
        } else {
            wrongPasswordAlert();
        }
    }

    public void onClickSignIn(View signInButton) {
        EditText usernameTextBox = findViewById(R.id.usernameTextBox);
        EditText passwordTextBox = findViewById(R.id.passwordTextBox);
        String username = usernameTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        db.child("Users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        attemptSignIn(username, password, user);
                    } else {
                        noSuchUserAlert();
                    }
                } else {
                    errorAlert();
                }
            }
        });
    }

    public void onClickSignUp(View signUpButton) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}

