package com.example.forest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class mainpage extends AppCompatActivity implements ReforestationEventsAdapter.OnEventClickListener {

    private List<ReforestationEvent> eventList;

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(),user.getDisplayName().toString(),Toast.LENGTH_SHORT).show();



        // Create a list of reforestation events
        eventList = new ArrayList<>();
        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String event_name = document.getData().get("eventName").toString();
                                String event_location = document.getData().get("eventLocation").toString();
                                String event_date = document.getData().get("eventDate").toString();
                                Uri eventImage = Uri.parse(document.getData().get("eventImage").toString());
                                String eventOrganizer = document.getData().get("eventOrganizer").toString();
                                Log.d("name", event_name, task.getException());
                                Log.d("date", event_date, task.getException());
                                Log.d("location", event_location, task.getException());
                                Log.d("uri", String.valueOf(eventImage), task.getException());

                                eventList.add(new ReforestationEvent(event_name,event_date,event_location,eventImage,eventOrganizer));

//                                eventList.add(new ReforestationEvent(document.getData().get("eventName").toString(),document.getData().get("eventDate").toString(),document.getData().get("eventLocation").toString(), Uri.parse( document.getData().get("eventImage").toString())));
                            }

                            // Create the adapter with the list of events and pass this activity as the click listener
                            ReforestationEventsAdapter adapter = new ReforestationEventsAdapter(eventList, mainpage.this);

                            // Find the RecyclerView and set the adapter
                            RecyclerView recyclerView = findViewById(R.id.eventsRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
//        eventList.add(new ReforestationEvent("City Park Tree Planting", "April 22, 2024", "City Park", Uri.parse("https://firebasestorage.googleapis.com/v0/b/forest-3c437.appspot.com/o/events%2F1708723063681-primary%3ADownload%2Fbeluga.jpg?alt=media&token=2376fc1d-008e-40bb-bf0f-9c0cb2d7573c")));
//        eventList.add(new ReforestationEvent("Neighborhood Reforestation", "May 5, 2024", "Greenwood District",Uri.parse("https://firebasestorage.googleapis.com/v0/b/forest-3c437.appspot.com/o/events%2F1708722899473-1000000033?alt=media&token=e98864f5-c495-45ce-82a0-a55bc6b65a5f")));
//        eventList.add(new ReforestationEvent("City Park Tree Planting", "April 22, 2024", "City Park"));
//        eventList.add(new ReforestationEvent("Neighborhood Reforestation", "May 5, 2024", "Greenwood District"));
//
//        // Create the adapter with the list of events and pass this activity as the click listener
//        ReforestationEventsAdapter adapter = new ReforestationEventsAdapter(eventList, this);
//
//        // Find the RecyclerView and set the adapter
//        RecyclerView recyclerView = findViewById(R.id.eventsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEventClick(int position) {
        Intent intent;
        switch (position) {
            case 0: // For "City Park Tree Planting"
                intent = new Intent(getApplicationContext(), addEvent.class);
                break;
            case 1: // For "Neighborhood Reforestation"
                intent = new Intent(getApplicationContext(), event2.class);
                break;
            default:
                // Default case, can redirect to a general activity or handle error
                intent = new Intent(getApplicationContext(), eventpage.class);
                break;
        }

        startActivity(intent);
    }
}
