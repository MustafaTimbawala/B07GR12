package com.example.b07project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private List<Announcement> announcements;

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView announcerTextView;
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView contentTextView;

        public TextView eventTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            announcerTextView = itemView.findViewById(R.id.announcer);
            titleTextView = itemView.findViewById(R.id.title);
            dateTextView = itemView.findViewById(R.id.date);
            contentTextView = itemView.findViewById(R.id.announcement);
            eventTitleView = itemView.findViewById(R.id.eventTitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.announcerTextView.setText(announcement.getAnnouncer());
        holder.titleTextView.setText(announcement.getTitle());
        holder.dateTextView.setText(announcement.getDate());
        holder.contentTextView.setText(announcement.getContent());
        boolean isEvent = announcements.get(position).getEvent();
        if(isEvent){
            holder.eventTitleView.setVisibility(View.VISIBLE);
            holder.eventTitleView.setText(announcement.getEventTitle());
        }else{
            holder.eventTitleView.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}

