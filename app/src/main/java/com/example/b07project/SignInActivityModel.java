package com.example.b07project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivityModel {

    final static String databaseLink = "https://b07project-e501d-default-rtdb.firebaseio.com/";
    DatabaseReference db;
    SignInActivityPresenter presenter;
    public SignInActivityModel(SignInActivityPresenter presenter) {
        this.db = FirebaseDatabase.getInstance(databaseLink).getReference();
        this.presenter = presenter;
    }

    public void queryUsernameAndPassword(String username, String password) {
        db.child("Users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().getValue(User.class);
                    if (user != null) {
                        presenter.checkUsernameAndPassword(username, password, user);
                    } else {
                        presenter.noSuchUserAlert();
                    }
                }
            }
        });
    }


}
