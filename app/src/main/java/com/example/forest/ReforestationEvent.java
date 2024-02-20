package com.example.forest;

public class ReforestationEvent {
    private String eventName;
    private String eventDate;
    private String eventLocation;

    public ReforestationEvent(String eventName, String eventDate, String eventLocation) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
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

    // Setters can be added if needed
}
