package com.example.b07project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Objects;

public class CreateAnnouncementFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private String username;
    private boolean admin;
    EditText announceTitle;
    EditText announceInput;
    EditText eventNameInput;
    TextView announceHead;
    TextView announceDate;
    FirebaseDatabase firebase;

    public CreateAnnouncementFragment() {} // Required empty public constructor

    public static CreateAnnouncementFragment newInstance(String username, boolean admin) {
        CreateAnnouncementFragment fragment = new CreateAnnouncementFragment();
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
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_announcement, container, false);

        announceInput = (EditText) view.findViewById(R.id.Announcement);
        announceHead = (TextView) view.findViewById(R.id.AnnounceHead);
        announceTitle = (EditText) view.findViewById(R.id.AnnounceTitle);
        eventNameInput = (EditText) view.findViewById(R.id.eventName);

        eventNameInput.setFocusable(false);

        LocalDate myObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myObj = LocalDate.now();
        }

        firebase = FirebaseDatabase.getInstance(getString(R.string.database_link));
        DatabaseReference announceRef = firebase.getReference("Announcements");
        DatabaseReference eventsRef = firebase.getReference("Events");
        announceDate = (TextView) view.findViewById(R.id.AnnounceDate);
        announceDate.setText(myObj.toString());
        Switch eventSwitch = view.findViewById(R.id.eventSwitch);
        Handler handler = new Handler(Looper.getMainLooper());

        LocalDate finalMyObj = myObj;
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = announceTitle.getText().toString();
                String content = announceInput.getText().toString();
                if (eventSwitch.isChecked()) {
                    String eventName = eventNameInput.getText().toString();
                    if (eventName.replaceAll("\\s+", "").equals("") || title.replaceAll("\\s+", "").equals("") || content.replaceAll("\\s+", "").equals("")) {
                        Toast.makeText(getContext(), "Fields cannot be empty.", Toast.LENGTH_LONG).show();
                    } else {
                        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            boolean eventOccur = false;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child1 : snapshot.getChildren()) {
                                    if (Objects.requireNonNull(child1.child("title").getValue()).toString().equals(eventName)) {
                                        eventOccur = true;
                                    }
                                }
                                if (!eventOccur){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateUIWithData(view, "Event does not exist.");
                                        }
                                    });
                                }
                                else {
                                    announceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        int id = 0;

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot child : snapshot.getChildren()) {
                                                id++;
                                            }

                                            boolean isEvent = true;
                                            String id1 = Integer.toString(id + 1);
                                            announceRef.child(id1).child("Announcer").setValue(username);
                                            announceRef.child(id1).child("Content").setValue(content);
                                            announceRef.child(id1).child("Date").setValue(finalMyObj.toString());
                                            announceRef.child(id1).child("EventTitle").setValue(eventName);
                                            announceRef.child(id1).child("ID").setValue(id + 1);
                                            announceRef.child(id1).child("Title").setValue(title);
                                            announceRef.child(id1).child("isEvent").setValue(isEvent);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }});
                    }
                }
                else{
                    if(title.replaceAll("\\s+", "").equals("") || content.replaceAll("\\s+", "").equals("")) {
                        Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    }
                    else{
                        announceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            int id=0;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot child: snapshot.getChildren()){
                                    id++;
                                }
                                boolean isEvent = false;
                                String id1 = Integer.toString(id + 1);

                                announceRef.child(id1).child("Announcer").setValue(username);
                                announceRef.child(id1).child("Content").setValue(content);
                                announceRef.child(id1).child("Date").setValue(finalMyObj.toString());
                                announceRef.child(id1).child("EventTitle").setValue("");
                                announceRef.child(id1).child("ID").setValue(id+1);
                                announceRef.child(id1).child("Title").setValue(title);
                                announceRef.child(id1).child("isEvent").setValue(isEvent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, ViewAnnouncementFragment.newInstance(username, admin));
                fragmentTransaction.commit();
            }
        });

        ((Switch)view.findViewById(R.id.eventSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            EditText eventName = (EditText) view.findViewById(R.id.eventName);
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    eventName.setFocusableInTouchMode(true);
                }
                else {
                    eventName.setFocusable(false);
                    eventName.setText("");
                }
            }

        });

        return view;
    }
    private void updateUIWithData(View view, final String data) {
        Toast.makeText(view.getContext(), "Event does not exist.", Toast.LENGTH_LONG).show();
    }

}
