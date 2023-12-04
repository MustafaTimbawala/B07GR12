package com.example.b07project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewFeedbackFragment extends Fragment {

    private static final String ARG_EVENT_ID = "eventID";
    private int eventID;
    private DatabaseReference db;
    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Feedback> feedbackList;
    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";

    public ViewFeedbackFragment() {
        // Required empty public constructor
    }

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
            eventID = args.getInt(ARG_EVENT_ID);
        }
        db = FirebaseDatabase.getInstance("https://b07project-e501d-default-rtdb.firebaseio.com/")
                .getReference().child("Feedback").child(String.valueOf(eventID));
        feedbackList = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedbackList.clear();
                float sumRatings = 0;
                int countRatings = 0;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = childSnapshot.getValue(Feedback.class);
                    String username = childSnapshot.getKey(); // Assuming the username is the key
                    if (feedback != null) {
                        feedback.setUsername(username); // Set the username in the feedback object
                        feedbackList.add(feedback);
                        sumRatings += feedback.getRating();
                        countRatings++;
                    }
                }

                float averageRating = countRatings > 0 ? sumRatings / countRatings : 0;
                updateUI(averageRating, countRatings, getView());
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(getActivity(), "Something went wrong in the database", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(float averageRating, int countRatings, View view) {
        TextView averageRatingText = view.findViewById(R.id.averageRatingText);
        TextView numberOfRatingsText = view.findViewById(R.id.numberOfRatingsText);

        averageRatingText.setText(String.format(Locale.US, "Average: %.2f", averageRating));
        numberOfRatingsText.setText(String.format(Locale.US, "# of Ratings: %d", countRatings));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_feedback, container, false);
        recyclerView = view.findViewById(R.id.feedbackRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedbackAdapter = new FeedbackAdapter(feedbackList);
        recyclerView.setAdapter(feedbackAdapter);

        return view;
    }
}