package com.example.forest;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.example.forest.ReforestationEvent;
import com.example.forest.ReforestationEventsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mainpage extends AppCompatActivity implements ReforestationEventsAdapter.OnEventClickListener {

    private List<ReforestationEvent> eventList;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(),user.getDisplayName().toString(),Toast.LENGTH_SHORT).show();



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
        // Handle event selection here
        Toast.makeText(this, "Event selected: " + eventList.get(position).getEventName(), Toast.LENGTH_SHORT).show();
    }
}
