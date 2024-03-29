package com.example.forest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ReforestationEventsAdapter extends RecyclerView.Adapter<ReforestationEventsAdapter.ViewHolder> {

    private List<ReforestationEvent> eventList;
    private OnEventClickListener onEventClickListener;

//    private String eventID;


    public ReforestationEventsAdapter(List<ReforestationEvent> eventList, OnEventClickListener onEventClickListener) {
        this.eventList = eventList;
        this.onEventClickListener = onEventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReforestationEvent event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventName, eventDate, eventLocation, eventOrganizer;
        private ImageView eventImage;
        private String eventID;
        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameText);
            eventDate = itemView.findViewById(R.id.eventDateText);
            eventLocation = itemView.findViewById(R.id.eventLocationText);
            eventOrganizer = itemView.findViewById(R.id.eventOrganizerText);
            eventImage = (ImageView) itemView.findViewById(R.id.eventImageUrl);
            itemView.setOnClickListener(this);
        }

        public void bind(ReforestationEvent event) {
            eventName.setText(event.getEventName());
            eventDate.setText(event.getEventDate());
            eventLocation.setText(event.getEventLocation());
            eventOrganizer.setText(event.getEventOrganizer());
//            eventImage.setImageURI(event.getEventImage());
            Glide.with(eventImage)
                    .load(event.getEventImage())
                    .into(eventImage);
            eventID = event.getEventID();
        }

        @Override
        public void onClick(View view) {
            if (onEventClickListener != null) {
                onEventClickListener.onEventClick(getAdapterPosition(),eventID);
            }
        }
    }

    public interface OnEventClickListener {
        void onEventClick(int position,String eventID);
    }
}