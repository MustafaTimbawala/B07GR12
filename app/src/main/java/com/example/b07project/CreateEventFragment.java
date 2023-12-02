package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private static final String ARG_EVENT_ID = "eventID";
    private String username;
    private boolean admin;
    private int eventID = -1;
    private DatabaseReference db;

    public CreateEventFragment() {} //Required empty public constructor
    public static CreateEventFragment newInstance(String username, boolean admin, int eventID) {
        CreateEventFragment fragment = new CreateEventFragment();
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
            eventID = args.getInt(ARG_EVENT_ID, -1);
        }
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_event, container, false);

        EditText titleTextBox = view.findViewById(R.id.eventTitleTextBox);
        EditText descriptionTextBox = view.findViewById(R.id.eventDescriptionTextBox);
        EditText dateTextBox = view.findViewById(R.id.eventDateTextBox);
        EditText timeTextBox = view.findViewById(R.id.eventTimeTextBox);
        EditText capacityTextBox = view.findViewById(R.id.eventCapacityTextBox);

        // If eventID is not -1, load the event data and populate the EditTexts
        if(eventID != -1) {
            db.child("Events").child(String.valueOf(eventID)).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if(event != null) {
                        titleTextBox.setText(event.getTitle());
                        descriptionTextBox.setText(event.getDescription());
                        capacityTextBox.setText(String.valueOf(event.getCapacity()));
                        DateTime dateTime = event.getDate();
                        dateTextBox.setText(dateTime.getYear() + "/" + dateTime.getMonth() + "/" + dateTime.getDay());
                        timeTextBox.setText(String.format("%02d:%02d", dateTime.getHour(), dateTime.getMinute()));

                    }
                }
                // handle if attempt to read data from Firebase fails or is interrupted
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("DBError", "loadEvent:onCancelled", databaseError.toException());
                }
            });

        }

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, EventsFragment.newInstance(username, admin));
                fragmentTransaction.commit();
            }
        });
        Button createEventButton = view.findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrEditEvent();
            }
        });

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void putEventInDatabase(Event event) {
        db.child("Events").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    int maxID = 0;
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        maxID = Math.max(maxID, Integer.parseInt(dataSnapshot.getKey()));
                    }
                    db.child("Events").child(String.valueOf(maxID + 1)).setValue(event);
                }
            }
        });
    }

    private void createOrEditEvent() {
        EditText titleTextBox = getView().findViewById(R.id.eventTitleTextBox);
        EditText descriptionTextBox = getView().findViewById(R.id.eventDescriptionTextBox);
        EditText dateTextBox = getView().findViewById(R.id.eventDateTextBox);
        EditText timeTextBox = getView().findViewById(R.id.eventTimeTextBox);
        EditText capacityTextBox = getView().findViewById(R.id.eventCapacityTextBox);

        String title = titleTextBox.getText().toString();
        String description = descriptionTextBox.getText().toString();
        int capacity = Integer.parseInt(capacityTextBox.getText().toString());

        String dateString = dateTextBox.getText().toString();
        String timeString = timeTextBox.getText().toString();
        String[] dateParts = dateString.split("/");
        String[] timeParts = timeString.split(":");

        if (dateParts.length != 3) {
            showToast("Invalid date format");
            return;
        }

        int year, month, day;
        try {
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException e) {
            showToast("Invalid date format");
            return;
        }

        if (timeParts.length != 2) {
            showToast("Invalid time format");
            return;
        }

        int hour, minute;
        try {
            hour = Integer.parseInt(timeParts[0]);
            minute = Integer.parseInt(timeParts[1]);
        } catch (NumberFormatException e) {
            showToast("Invalid time format");
            return;
        }

        // Validate date and time
        if (month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            showToast("Invalid date or time");
            return;
        }
        DateTime dateTime = new DateTime(year, month, day, hour, minute);
        Event event = new Event(title, dateTime, description, capacity);

        if(eventID != -1) {
            db.child("Events").child(String.valueOf(eventID)).setValue(event);
        } else {
            putEventInDatabase(event);
        }
        navToEventsFragment();
    }

    private void navToEventsFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, EventsFragment.newInstance(username, admin));
        fragmentTransaction.commit();
    }
}