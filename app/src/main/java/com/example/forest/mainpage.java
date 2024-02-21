package com.example.forest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.forest.ReforestationEvent;
import com.example.forest.ReforestationEventsAdapter;

public class mainpage extends AppCompatActivity implements ReforestationEventsAdapter.OnEventClickListener {

    private List<ReforestationEvent> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // Create a list of reforestation events
        eventList = new ArrayList<>();
        eventList.add(new ReforestationEvent("City Park Tree Planting", "April 22, 2024", "City Park"));
        eventList.add(new ReforestationEvent("Neighborhood Reforestation", "May 5, 2024", "Greenwood District"));

        // Create the adapter with the list of events and pass this activity as the click listener
        ReforestationEventsAdapter adapter = new ReforestationEventsAdapter(eventList, this);

        // Find the RecyclerView and set the adapter
        RecyclerView recyclerView = findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEventClick(int position) {
        Intent intent;
        switch (position) {
            case 0: // For "City Park Tree Planting"
                intent = new Intent(getApplicationContext(), event1.class);
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
