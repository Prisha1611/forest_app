package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Volunteer_Menu extends AppCompatActivity {
    private Button events,products;
    private TextView username;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_menu);
        events = findViewById(R.id.gotoEventVolunteer);
        products = findViewById(R.id.gotoProductVolunteer);
        username = findViewById(R.id.volunteer_name);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
//        Toast.makeText(getApplicationContext(),user.getDisplayName().toString(),Toast.LENGTH_SHORT).show();

        username.setText("Welcome back, " + user.getDisplayName().toString());

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), addEvent.class);
                startActivity(new Intent(getApplicationContext(), mainpage.class));
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), addEvent.class);
                startActivity(new Intent(getApplicationContext(), Business.class));
            }
        });

    }
}