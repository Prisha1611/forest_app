package com.example.forest;

import android.net.Uri;

public class ReforestationEvent {
    private String eventName;
    private String eventDate;
    private String eventLocation;

    private Uri eventImage;

    public ReforestationEvent(String eventName, String eventDate, String eventLocation, Uri eventImage) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventImage = eventImage;
    }

    // Getters
    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public Uri getEventImage() { return eventImage;}

    // Setters can be added if needed
}
