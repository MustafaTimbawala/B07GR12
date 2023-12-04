package com.example.b07project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivityView extends AppCompatActivity {

    EditText usernameTextBox;
    EditText passwordTextBox;
    SignInActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        usernameTextBox = findViewById(R.id.usernameTextBox);
        passwordTextBox = findViewById(R.id.passwordTextBox);
        presenter = new SignInActivityPresenter(this);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void navToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void navToHome(String username, boolean admin) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("admin", admin);
        startActivity(intent);
    }

    public void onClickSignIn(View signInButton) {
        presenter.signInClicked();
    }

    public void onClickSignUp(View signUpButton) {
        presenter.signUpClicked();
    }

}

