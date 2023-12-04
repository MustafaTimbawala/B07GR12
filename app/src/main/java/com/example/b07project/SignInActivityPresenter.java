package com.example.b07project;

import android.util.Log;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignInActivityPresenter {

    SignInActivityView view;
    SignInActivityModel model;

    public SignInActivityPresenter(SignInActivityView view) {
        this.view = view;
        this.model = new SignInActivityModel(this);
    }

    public void signUpClicked() {
        view.navToSignUp();
    }

    public void signInClicked() {
        String username = view.usernameTextBox.getText().toString();
        String password = view.passwordTextBox.getText().toString();
        model.queryUsernameAndPassword(username, password);
    }

    public void checkUsernameAndPassword(String username, String password, User user) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (result.verified) {
            view.showToast("Welcome back " + username + "!");
            view.navToHome(username, user.isAdmin());
        } else {
            view.showToast("Incorrect password. Please try again.");
        }
    }

    public void noSuchUserAlert() {
        view.showToast("Username does not exist. Please try again.");
    }

}
