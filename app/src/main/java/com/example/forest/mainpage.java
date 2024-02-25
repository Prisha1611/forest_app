package com.example.forest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainpage.this, addProduct.class));
            }
        });

        // Create a list of reforestation events
        eventList = new ArrayList<>();
        db.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String event_name = document.getData().get("eventName").toString();
                                String event_location = document.getData().get("eventLocation").toString();
                                String event_date = document.getData().get("eventDate").toString();
                                Uri eventImage = Uri.parse(document.getData().get("eventImage").toString());
                                String eventOrganizer = document.getData().get("eventOrganizer").toString();

                                eventList.add(new ReforestationEvent(event_name, event_date, event_location, eventImage, eventOrganizer));
                            }

                            // Create the adapter with the list of events and pass this activity as the click listener
                            ReforestationEventsAdapter adapter = new ReforestationEventsAdapter(eventList, mainpage.this);

                            // Find the RecyclerView and set the adapter
                            RecyclerView recyclerView = findViewById(R.id.eventsRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(mainpage.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onEventClick(int position) {
        // Handle event clicks if needed
    }
}
