package com.example.b07project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

        firebase = FirebaseDatabase.getInstance("https://complaints-c256f-default-rtdb.firebaseio.com");
        DatabaseReference announceRef = firebase.getReference("Announcements");
        DatabaseReference eventsRef = firebase.getReference("Events");
        announceDate = (TextView) view.findViewById(R.id.AnnounceDate);
        announceDate.setText(myObj.toString());

        LocalDate finalMyObj = myObj;
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    int id=0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child: snapshot.getChildren()){
                            id++;
                        }
                        String title = announceTitle.getText().toString();
                        String content = announceInput.getText().toString();
                        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch eventSwitch = view.findViewById(R.id.eventSwitch);
                        if (eventSwitch.isChecked()){
                            String eventName = eventNameInput.getText().toString();

                            if(eventName.replaceAll("\\s+", "").equals("") || title.replaceAll("\\s+", "").equals("") || content.replaceAll("\\s+", "").equals("")) {
                                Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.button_second)
                                        .setAction("Action", null).show();
                            }

                            else{
                                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    boolean eventOccur = false;
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        for (DataSnapshot child1: snapshot1.getChildren()){
                                            if (Objects.requireNonNull(child1.child("Name").getValue()).toString().equals(eventName)){
                                                eventOccur = true;
                                            }
                                        }
                                        if (!eventOccur){
                                            Snackbar.make(view, "Event does not exist.", Snackbar.LENGTH_LONG)
                                                    .setAnchorView(R.id.button_second)
                                                    .setAction("Action", null).show();
                                        }
                                        else{
                                            boolean isEvent = true;
                                            announceRef.child("Announcements" + (id+1)).child("Announcer").setValue(username);
                                            announceRef.child("Announcements" + (id+1)).child("Content").setValue(content);
                                            announceRef.child("Announcements" + (id+1)).child("Date").setValue(finalMyObj.toString());
                                            announceRef.child("Announcements" + (id+1)).child("EventName").setValue(eventName);
                                            announceRef.child("Announcements" + (id+1)).child("ID").setValue(id+1);
                                            announceRef.child("Announcements" + (id+1)).child("Title").setValue(title);
                                            announceRef.child("Announcements" + (id+1)).child("isEvent").setValue(isEvent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else{
                            if(title.replaceAll("\\s+", "").equals("") || content.replaceAll("\\s+", "").equals("")) {
                                Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_LONG)
                                        .setAnchorView(R.id.button_second)
                                        .setAction("Action", null).show();
                            }
                            else{
                                boolean isEvent = false;
                                announceRef.child("Announcements" + (id+1)).child("Announcer").setValue(username);
                                announceRef.child("Announcements" + (id+1)).child("Content").setValue(content);
                                announceRef.child("Announcements" + (id+1)).child("Date").setValue(finalMyObj.toString());
                                announceRef.child("Announcements" + (id+1)).child("EventName").setValue("");
                                announceRef.child("Announcements" + (id+1)).child("ID").setValue(id+1);
                                announceRef.child("Announcements" + (id+1)).child("Title").setValue(title);
                                announceRef.child("Announcements" + (id+1)).child("isEvent").setValue(isEvent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
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
}