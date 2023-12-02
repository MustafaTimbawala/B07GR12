package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewComplaintFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private String username;
    private boolean admin;

    private DatabaseReference db;
    private RecyclerView recyclerView;
    private List<Complaint> complaintList;
    private ComplaintAdapter adapter;
    public ViewComplaintFragment() {} // Required empty public constructor

    public static ViewComplaintFragment newInstance(String username, boolean admin) {
        ViewComplaintFragment fragment = new ViewComplaintFragment();
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
        db = FirebaseDatabase.getInstance(getString(R.string.database_link)).getReference("Complaints");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_complaint, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(complaintList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if (admin) {
            fetchComplaintsAdmin();
        } else{
            fetchComplaintsStudent();
        }
        return view;
    }

    private void fetchComplaintsStudent() {
        db.child("Complaints").orderByChild("Name").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    String usernames = datas.child("Name").getValue().toString();
                    String titles = datas.child("Title").getValue().toString();
                    String contents = datas.child("Content").getValue().toString();
                    String dates = datas.child("Date").getValue().toString();

                    Complaint complaint = new Complaint();
                    complaint.setUsername("Username: " + usernames);
                    complaint.setTitle("Title: " + titles);
                    complaint.setContent("Complaint: " + contents);
                    complaint.setDate("Date: " + dates);

                    complaintList.add(0, complaint);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching complaints: " + error.getMessage());
            }
        });
    }

    private void fetchComplaintsAdmin () {
        db.child("Complaints").orderByChild("Name").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    String usernames = datas.child("Name").getValue().toString();
                    String titles = datas.child("Title").getValue().toString();
                    String contents = datas.child("Content").getValue().toString();
                    String dates = datas.child("Date").getValue().toString();

                    Complaint complaint = new Complaint();
                    complaint.setUsername("Username: " + usernames);
                    complaint.setTitle("Title: " + titles);
                    complaint.setContent("Complaint: " + contents);
                    complaint.setDate("Date: " + dates);

                    complaintList.add(0, complaint);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching complaints: " + error.getMessage());
            }
        });
    }

}