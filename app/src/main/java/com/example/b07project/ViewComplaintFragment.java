package com.example.b07project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        view.findViewById(R.id.createComplaint).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, CreateComplaintFragment.newInstance(username, admin));
                fragmentTransaction.commit();
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(complaintList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if (admin) {
            view.findViewById(R.id.createComplaint).setVisibility(View.GONE);
            fetchComplaintsAdmin();
        } else{
            fetchComplaintsStudent();
        }
        return view;
    }

    private void fetchComplaintsStudent() {
        db.orderByChild("Name").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot datas : snapshot.getChildren()) {
                    String titles = Objects.requireNonNull(datas.child("Title").getValue()).toString();
                    String contents = Objects.requireNonNull(datas.child("Content").getValue()).toString();
                    String dates = Objects.requireNonNull(datas.child("Date").getValue()).toString();

                    Complaint complaint = new Complaint();
                    complaint.setTitle("Title: " + titles);
                    complaint.setContent("Complaint: " + contents);
                    complaint.setDate("Date: " + dates);
                    complaint.setUsername("Name: " + username);
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
        db.orderByChild("Date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String contents = Objects.requireNonNull(data.child("Content").getValue()).toString();
                    String dates = Objects.requireNonNull(data.child("Date").getValue()).toString();
                    String names = Objects.requireNonNull(data.child("Name").getValue()).toString();
                    String titles = Objects.requireNonNull(data.child("Title").getValue()).toString();
                    Complaint complaint = new Complaint();
                    complaint.setUsername("Username: " + names);
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