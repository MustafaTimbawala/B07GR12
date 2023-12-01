package com.example.b07project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder>{

    Context context;
    List<Event> events;

    public EventsRecyclerViewAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitleText;
        TextView eventDateText;
        TextView eventDescriptionText;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleText = itemView.findViewById(R.id.eventTitleText);
            eventDateText = itemView.findViewById(R.id.eventDateText);
            eventDescriptionText = itemView.findViewById(R.id.eventDescriptionText);
        }

    }

    @NonNull
    @Override
    public EventsRecyclerViewAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.events_recycler_view_row, parent, false);
        return new EventsRecyclerViewAdapter.EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRecyclerViewAdapter.EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitleText.setText(event.getTitle());
        holder.eventDateText.setText(event.getDate().toString());
        holder.eventDescriptionText.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

}
