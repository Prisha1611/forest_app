package com.example.forest;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class viewEvent extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView viewEventName, viewEventDate, viewEventOrganizer, viewEventLocation;
    private ImageView viewEventImage;

    private Button registerToEventButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        viewEventImage = findViewById(R.id.viewEventImage);
        viewEventName = findViewById(R.id.viewEventName);
        viewEventDate = findViewById(R.id.viewEventDate);
        viewEventOrganizer = findViewById(R.id.viewEventOrganizer);
        viewEventLocation = findViewById(R.id.viewEventOrganizer);
        registerToEventButton = findViewById(R.id.registerToEventButton);
        Intent intent = getIntent();
        String eventID = intent.getExtras().getString("eventID");
        Log.d("Event ID received", eventID);
        Toast.makeText(getApplicationContext(), eventID, Toast.LENGTH_SHORT).show();
        db.collection("Events").document(eventID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        viewEventImage.setImageURI(Uri.parse(documentSnapshot.get("eventImage").toString()));
                        Glide.with(viewEventImage)
                                .load(Uri.parse(documentSnapshot.get("eventImage").toString()))
                                .into(viewEventImage);
                        viewEventName.setText("Name " + documentSnapshot.get("eventName").toString());
                        viewEventDate.setText("On " + documentSnapshot.get("eventDate").toString());
                        viewEventOrganizer.setText("By " + documentSnapshot.get("eventOrganizer").toString());
                        viewEventLocation.setText("At " + documentSnapshot.get("eventLocation").toString());
//                        viewEventDate.setText();
                        Toast.makeText(getApplicationContext(), documentSnapshot.get("eventLocation").toString(), Toast.LENGTH_SHORT).show();

                    }
                });

        registerToEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(viewEvent.this)
                        .setTitle("Event registration")
                        .setMessage("Would you like to register for " + viewEventName.getText().toString())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Map<String, Object> userDetail = new HashMap<>();
                                userDetail.put("name", user.getDisplayName());
                                userDetail.put("email", user.getEmail());
//                                Toast.makeText(viewEvent.this, "Yaay", Toast.LENGTH_SHORT).show();
                                db.collection("Events").document(eventID)
                                        .collection("Attendants")
                                        .add(userDetail)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(viewEvent.this, "Successfully register for the event", Toast.LENGTH_SHORT).show();
                                                //scheduleNotification(getNotification("5 second delay"),5000);
                                            }

                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(viewEvent.this, "Error registering for event", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    //private void scheduleNotification (Notification notification , int delay) {
    //Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
    //notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
    //notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
    //PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
    //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
    //AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
    //assert alarmManager != null;
    //alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    //}
}