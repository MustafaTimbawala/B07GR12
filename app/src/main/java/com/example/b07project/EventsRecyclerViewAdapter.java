package com.example.b07project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>{

    private Context context;
    private EventsFragment fragment;
    private String username;
    private boolean admin;
    private List<Integer> eventIDsSortedByDate;
    private Map<Integer, Event> IDToEvent;

    public DatabaseReference db;

    public EventsRecyclerViewAdapter(Context context, EventsFragment fragment,
                                     String username, boolean admin,
                                     List<Integer> eventIDsSortedByDate,
                                     Map<Integer, Event> IDToEvent) {
        this.context = context;
        this.fragment = fragment;
        this.username = username;
        this.admin = admin;
        this.eventIDsSortedByDate = eventIDsSortedByDate;
        this.IDToEvent = IDToEvent;
        db = FirebaseDatabase.getInstance("https://b07project-e501d-default-rtdb.firebaseio.com/").getReference();
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public int eventID;
        public TextView eventTitleText;
        public TextView eventDateText;
        public TextView eventDescriptionText;
        public Button mainButton;
        public Button deleteEventButton;

        public EventViewHolder(@NonNull View itemView, EventsFragment fragment) {
            super(itemView);
            eventTitleText = itemView.findViewById(R.id.eventTitleText);
            eventDateText = itemView.findViewById(R.id.eventDateText);
            eventDescriptionText = itemView.findViewById(R.id.eventDescriptionText);
            mainButton = itemView.findViewById(R.id.mainButton);
            deleteEventButton = itemView.findViewById(R.id.deleteEventButton);
        }

    }

    @NonNull
    @Override
    public EventsRecyclerViewAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.events_recycler_view_row, parent, false);
        return new EventsRecyclerViewAdapter.EventViewHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRecyclerViewAdapter.EventViewHolder holder, int position) {
        int id = eventIDsSortedByDate.get(position);
        Event event = IDToEvent.get(id);
        holder.eventID = id;
        holder.eventTitleText.setText(event.getTitle());
        holder.eventDateText.setText(event.getDate().toString());
        holder.eventDescriptionText.setText(event.getDescription());
        if (admin) {

            if (event.getDate().compareTo(DateTime.now()) > 0) { //event is in the future
                holder.mainButton.setText("Edit");
                holder.mainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainFragment, CreateEventFragment.newInstance(username, admin, holder.eventID));
                        fragmentTransaction.commit();
                    }
                });

                holder.deleteEventButton.setText("Delete");
                holder.deleteEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String eventId = String.valueOf(holder.eventID);

                        db.child("Events").child(eventId).removeValue();

                    }
                });

            } else {
                holder.mainButton.setText("View Feedback");
                holder.mainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.navToViewFeedback(holder.eventID);
                    }
                });

                holder.deleteEventButton.setVisibility(View.GONE);
            }


        } else {
            boolean registered = event.getRegisteredUsers().contains(username);
            if (event.getDate().compareTo(DateTime.now()) > 0) { //event is in the future
                if (!registered) {
                    holder.mainButton.setText("Register now!");
                    holder.mainButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.registerUser(holder.eventID);
                        }
                    });
                } else {
                    holder.mainButton.setText("Unregister");
                    holder.mainButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.unregisterUser(holder.eventID);
                        }
                    });
                }

            } else if (registered) {
                holder.mainButton.setText("Leave feedback!");
                holder.mainButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.navToCreateFeedback(holder.eventID);
                    }
                });
            } else {
                holder.mainButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return eventIDsSortedByDate.size();
    }

}
