package com.example.b07project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewFeedbackFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private static final String ARG_EVENT_ID = "eventID";
    private String username;
    private boolean admin;
    private int eventID;
    private DatabaseReference db;

    public ViewFeedbackFragment() {} //Required empty public constructor

    public static ViewFeedbackFragment newInstance(String username, boolean admin, int eventID) {
        ViewFeedbackFragment fragment = new ViewFeedbackFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_feedback, container, false);

        //Placeholder
        TextView textView = view.findViewById(R.id.eventID);
        textView.setText("View Feedback for Event ID: " + eventID);

        //TO DO: finish this code

        return view;
    }
}