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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAnnouncementFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private String username;
    private boolean admin;
    private DatabaseReference db;
    private List<Announcement> announcementList;
    private AnnouncementAdapter adapter;

    public ViewAnnouncementFragment() {} //Required empty public constructor

    public static ViewAnnouncementFragment newInstance(String username, boolean admin) {
        ViewAnnouncementFragment fragment = new ViewAnnouncementFragment();
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
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference("Announcements");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_announcement, container, false);

        view.findViewById(R.id.createAnnouncement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, CreateAnnouncementFragment.newInstance(username, admin));
                fragmentTransaction.commit();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter(announcementList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (!admin) {
            view.findViewById(R.id.createAnnouncement).setVisibility(View.GONE);
            fetchAnnouncementsStudent();
        } else{
            fetchAnnouncementsAdmin();
        }
        return view;
    }

    private void fetchAnnouncementsAdmin() {
        db.child("Announcements").orderByChild("Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String announcers = Objects.requireNonNull(data.child("Announcer").getValue()).toString();
                    String titles = Objects.requireNonNull(data.child("Title").getValue()).toString();
                    String announcements = Objects.requireNonNull(data.child("Content").getValue()).toString();
                    String dates = Objects.requireNonNull(data.child("Date").getValue()).toString();
                    String eventTitles = Objects.requireNonNull(data.child("EventTitle").getValue()).toString();
                    boolean isEvent = (boolean) data.child("isEvent").getValue();
                    Announcement announcement = new Announcement();
                    announcement.setAnnouncer("Announcer: " + announcers);
                    announcement.setTitle("Title: " + titles);
                    announcement.setContent("Announcement: " + announcements);
                    announcement.setDate("Date: " + dates);
                    announcement.setEventTitle("Event: " + eventTitles);
                    announcement.setEvent(isEvent);
                    announcementList.add(0, announcement);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching announcements: " + error.getMessage());
            }
        });
    }

    private void fetchAnnouncementsStudent() {
        db.orderByChild("Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String announcers = Objects.requireNonNull(data.child("Announcer").getValue()).toString();
                    String titles = Objects.requireNonNull(data.child("Title").getValue()).toString();
                    String announcements = Objects.requireNonNull(data.child("Content").getValue()).toString();
                    String dates = Objects.requireNonNull(data.child("Date").getValue()).toString();
                    String eventTitles = Objects.requireNonNull(data.child("EventTitle").getValue()).toString();
                    boolean isEvent = (boolean) data.child("isEvent").getValue();
                    Announcement announcement = new Announcement();
                    announcement.setAnnouncer("Announcer: " + announcers);
                    announcement.setTitle("Title: " + titles);
                    announcement.setContent("Announcement: " + announcements);
                    announcement.setDate("Date: " + dates);
                    announcement.setEventTitle("Event: " + eventTitles);
                    announcement.setEvent(isEvent);

                    if (!isEvent) {
                        announcementList.add(0, announcement);
                    }
                    else {
                        checkUserInEvent(announcement);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching announcements: " + error.getMessage());
            }
        });
    }

    private void checkUserInEvent(Announcement announcement) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference("Events");
        String eventTitle = announcement.getEventTitle().trim();
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot info : snapshot.getChildren()) {
                    String eventInfo = Objects.requireNonNull(info.child("title").getValue()).toString().trim();
                    String cleanedEventTitle = eventTitle.substring(7);

                    if (cleanedEventTitle.equals(eventInfo)) {
                        DataSnapshot peopleSnapshot = info.child("registeredUsers");

                        for (DataSnapshot personSnapshot : peopleSnapshot.getChildren()) {
                            String personName = personSnapshot.getValue(String.class);


                            if (username.equals(personName)) {
                                announcementList.add(0, announcement);
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }

                        Log.d("TAG", "User is NOT in event");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking user in event: " + error.getMessage());
            }
        });
    }
}