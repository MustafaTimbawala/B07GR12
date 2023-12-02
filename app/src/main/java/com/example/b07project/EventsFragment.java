package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private String username;
    private boolean admin;
    private List<Integer> eventIDsSortedByDate;
    private Map<Integer, Event> IDToEvent;
    private DatabaseReference db;
    private EventsRecyclerViewAdapter adapter;

    public EventsFragment() {} // Required empty public constructor

    public static EventsFragment newInstance(String username, boolean admin) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putBoolean(ARG_ADMIN, admin);
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
        }
        eventIDsSortedByDate = new ArrayList<Integer>();
        IDToEvent = new HashMap<Integer, Event>();
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference();
    }

    private class compareEventsByDate implements Comparator<Integer> {
        @Override
        public int compare(Integer id1, Integer id2) {
            return IDToEvent.get(id1).getDate().compareTo(IDToEvent.get(id2).getDate());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        Button createEventButton = view.findViewById(R.id.navToCreateEvent);
        if (!admin) {
            createEventButton.setVisibility(View.GONE);
        } else {
            createEventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.mainFragment, CreateEventFragment.newInstance(username, admin, -1));
                    fragmentTransaction.commit();
                }
            });


        }
        RecyclerView eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView);
        adapter = new EventsRecyclerViewAdapter(getContext(), this, username, admin, eventIDsSortedByDate, IDToEvent);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db.child("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventIDsSortedByDate.clear();
                IDToEvent.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = Integer.parseInt(dataSnapshot.getKey());
                    Event event = dataSnapshot.getValue(Event.class);
                    eventIDsSortedByDate.add(id);
                    IDToEvent.put(id, event);
                }
                eventIDsSortedByDate.sort(new compareEventsByDate());
                Collections.reverse(eventIDsSortedByDate);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return view;
    }

    public void registerUser(int eventID) {
        Event event = IDToEvent.get(eventID);
        if (event.registerUser(username)) {
            db.child("Events").child(String.valueOf(eventID)).setValue(event);
        }
    }

    public void unregisterUser(int eventID) {
        Event event = IDToEvent.get(eventID);
        event.unregisterUser(username);
        db.child("Events").child(String.valueOf(eventID)).setValue(event);
    }

    public void navToCreateFeedback(int eventID) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, CreateFeedbackFragment.newInstance(username, admin, eventID));
        fragmentTransaction.commit();
    }

    public void navToViewFeedback(int eventID) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, ViewFeedbackFragment.newInstance(username, admin, eventID));
        fragmentTransaction.commit();
    }
}

