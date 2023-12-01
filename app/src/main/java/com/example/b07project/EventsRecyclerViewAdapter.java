package com.example.b07project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>{

    private Context context;
    private EventsFragment fragment;
    private String username;
    private boolean admin;
    private List<Integer> eventIDsSortedByDate;
    private Map<Integer, Event> IDToEvent;

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
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public int eventID;
        public TextView eventTitleText;
        public TextView eventDateText;
        public TextView eventDescriptionText;
        public Button mainButton;

        public EventViewHolder(@NonNull View itemView, EventsFragment fragment) {
            super(itemView);
            eventTitleText = itemView.findViewById(R.id.eventTitleText);
            eventDateText = itemView.findViewById(R.id.eventDateText);
            eventDescriptionText = itemView.findViewById(R.id.eventDescriptionText);
            mainButton = itemView.findViewById(R.id.mainButton);
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
            holder.mainButton.setText("View Feedback");
            holder.mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.navToViewFeedback(holder.eventID);
                }
            });
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
