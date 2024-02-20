package com.example.forest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReforestationEventsAdapter extends RecyclerView.Adapter<ReforestationEventsAdapter.ViewHolder> {

    private List<ReforestationEvent> eventList;
    private OnEventClickListener onEventClickListener;

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
        TextView eventName, eventDate, eventLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            itemView.setOnClickListener(this);
        }

        public void bind(ReforestationEvent event) {
            eventName.setText(event.getEventName());
            eventDate.setText(event.getEventDate());
            eventLocation.setText(event.getEventLocation());
        }

        @Override
        public void onClick(View view) {
            if (onEventClickListener != null) {
                onEventClickListener.onEventClick(getAdapterPosition());
            }
        }
    }

    public interface OnEventClickListener {
        void onEventClick(int position);
    }
}
