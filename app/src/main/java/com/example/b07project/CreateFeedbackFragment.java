package com.example.b07project;

import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateFeedbackFragment extends Fragment{

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private static final String ARG_EVENT_ID = "eventID";
    private String username;
    private boolean admin;
    private int eventID;
    private DatabaseReference db;



    public CreateFeedbackFragment() {} //Required empty public constructor

    public static CreateFeedbackFragment newInstance(String username, boolean admin, int eventID) {
        CreateFeedbackFragment fragment = new CreateFeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putBoolean(ARG_ADMIN, admin);
        args.putInt(ARG_EVENT_ID, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            username = args.getString(ARG_USERNAME);
            admin = args.getBoolean(ARG_ADMIN);
            eventID = args.getInt(ARG_EVENT_ID);
        }
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_feedback, container, false);

        TextView textView = view.findViewById(R.id.commentsID);

        // This is to slightly change the functionality of the ratingbar
        RatingBar feedBackBar = view.findViewById(R.id.feedBackBar);
        // this sets the default rating to 0and if left like this is not counted as a rating
        feedBackBar.setRating(0f);
        feedBackBar.setStepSize(0.5f);


        Button submitbutton = view.findViewById(R.id.submitFeed);
        // This is to handle the submit click
        submitbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View submitbutton) {
                // THese take the values from the rating and the comments
                RatingBar feedBackBar = view.findViewById(R.id.feedBackBar);
                float rating  =  feedBackBar.getRating();
                EditText commented= view.findViewById(R.id.commentBox);
                String comment = commented.getText().toString();
                if (comment.length()==0){comment = null;}

                // This creates an instance of feedback
                Feedback feedback=  new Feedback(comment, rating);

                if (comment.length()==0 && rating ==0){
                    //throws a toast to enter some sort of feedback
                    Toast.makeText( getActivity(),"Please enter a rating and/or a commment", Toast.LENGTH_LONG).show();
                }
                // This writes a query looking for a user in an event to check if the user already made a comment or not
                Query query = db.child("Feedback").child(Integer.toString(eventID)).child(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getActivity(), "You already commented", Toast.LENGTH_LONG).show();
                        } else {
                            // This handles the case where the child does not exist
                            //It makes the path in the database
                            db.child("Feedback").child((Integer.toString(eventID))).child(username).setValue(feedback);
                            Toast.makeText(getActivity(), "Feedback was succeccesful", Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // This method is called in case of an error
                        Toast.makeText(getActivity(), "Something went wrong in the database", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return view;
    }


}