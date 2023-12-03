package com.example.b07project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

        //Placeholder
        TextView textView = view.findViewById(R.id.commentsID);

        // This is to slightly change the functionality of the ratingbar
        RatingBar feedBackBar = view.findViewById(R.id.feedBackBar);
        feedBackBar.setRating(0f);// this is the deafult and if left like this is not counted as a rating
        feedBackBar.setStepSize(0.5f);



        Button submitbutton = view.findViewById(R.id.submitFeed);
        submitbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View submitbutton) {
                RatingBar feedBackBar = view.findViewById(R.id.feedBackBar);
                float rating  =  feedBackBar.getRating();
                EditText commented= view.findViewById(R.id.commentBox);
                String comment = commented.getText().toString();
                Feedback feedback=  new Feedback(comment, rating);
                if (comment.length()==0 && rating ==0){
                    //throw an error that the need to enter somesort of feedbackv
                    Toast.makeText( getActivity(),"Please enter a rating and/or a commment", Toast.LENGTH_LONG).show();
                }
                Query query = db.child("Feedback").child(Integer.toString(eventID)).child(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getActivity(), "You already commented", Toast.LENGTH_LONG).show();
                        } else {
                            // The child at the specified path does not exist
                            // Handle the case where the child does not exist
                            db.child("Feedback").child((Integer.toString(eventID))).child(username).setValue(feedback);
                            Toast.makeText(getActivity(), "Feedback was succeccesful", Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // This method is called in case of an error
                        // Handle the error here
                        Toast.makeText(getActivity(), "Something went wrong in the database", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        return view;
    }


}