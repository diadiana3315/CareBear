package com.example.carebear.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carebear.R;
import com.example.carebear.models.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcements;

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.titleTextView.setText(announcement.getTitle());
        holder.descriptionTextView.setText(announcement.getDescription());
        holder.dateTextView.setText(announcement.getDate());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, descriptionTextView, dateTextView;

        public AnnouncementViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.title_text);
            descriptionTextView = view.findViewById(R.id.description_text);
            dateTextView = view.findViewById(R.id.date_text);
        }
    }
}
