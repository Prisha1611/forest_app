package com.example.forest;

import android.net.Uri;

public class ReforestationEvent {
    private String eventName;
    private String eventDate;
    private String eventLocation;

    private Uri eventImage;
    private String eventOrganizer;

    public ReforestationEvent(String eventName, String eventDate, String eventLocation, Uri eventImage, String eventOrganizer) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventImage = eventImage;
        this.eventOrganizer = eventOrganizer;
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
    public String getEventOrganizer() {return  eventOrganizer;};

    // Setters can be added if needed
}
