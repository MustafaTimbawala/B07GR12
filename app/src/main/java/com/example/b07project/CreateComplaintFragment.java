package com.example.b07project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class CreateComplaintFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_ADMIN = "admin";
    private String username;
    private boolean admin;
    EditText compTitle;
    EditText compInput;
    TextView compHead;
    TextView date;
    FirebaseDatabase firebase;

    public CreateComplaintFragment() {} // Required empty public constructor

    public static CreateComplaintFragment newInstance(String username, boolean admin) {
        CreateComplaintFragment fragment = new CreateComplaintFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_complaint, container, false);
        compInput = (EditText) view.findViewById(R.id.compInput);
        compHead = (TextView) view.findViewById(R.id.compHead);
        compTitle = (EditText) view.findViewById(R.id.compTitle);

        LocalDate myObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myObj = LocalDate.now();
        }

        firebase = FirebaseDatabase.getInstance(getString(R.string.database_link));
        DatabaseReference myRef = firebase.getReference("Complaints");


        date = (TextView) view.findViewById(R.id.date);
        date.setText(myObj.toString());

        LocalDate finalMyObj = myObj;
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    int id=0;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            id++;
                        }
                        String title = compTitle.getText().toString();
                        String content = compInput.getText().toString();

                        if(content.replaceAll("\\s+", "").equals("") || title.replaceAll("\\s+", "").equals("")){
                            Snackbar.make(view, "Fields cannot be empty", Snackbar.LENGTH_LONG)
                                    .setAnchorView(R.id.button_first)
                                    .setAction("Action", null).show();
                        }
                        else {
                            myRef.child("Complaint" + (id + 1)).child("Title").setValue(title);
                            myRef.child("Complaint" + (id + 1)).child("Content").setValue(content);
                            myRef.child("Complaint" + (id + 1)).child("Date").setValue(finalMyObj.toString());
                            myRef.child("Complaint" + (id + 1)).child("ID").setValue(id + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //nav back to complaints page
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragment, ViewComplaintFragment.newInstance(username, admin));
                fragmentTransaction.commit();
            }

        });
        return view;
    }

}