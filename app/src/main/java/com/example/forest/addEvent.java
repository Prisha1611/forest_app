package com.example.forest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class addEvent extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView imageView;
    private EditText eventName, eventDate, eventLocation;
    private Button submitEvent;
    private Uri imageUri;
    private FirebaseAuth mAuth;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event1);

        eventName = findViewById(R.id.eventName);
        eventDate = findViewById(R.id.eventDate);
        eventLocation = findViewById(R.id.eventLocation);
        submitEvent = findViewById(R.id.submitEvent);
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imageView = findViewById(R.id.uploaded_image_view);
        Button uploadImageButton = findViewById(R.id.button_upload_image);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        submitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String event_name = eventName.getText().toString().trim();
                String event_date = eventDate.getText().toString().trim();
                String event_location = eventLocation.getText().toString().trim();
                SubmitEvent(event_name,event_location,event_date);
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            //
            // // Get a reference to store file at "photos/<FILENAME>"
            // StorageReference fileRef = storageRef.child("photos/" +
            // System.currentTimeMillis() + "-" + imageUri.getLastPathSegment());
            // UploadTask uploadTask = fileRef.putFile(imageUri);
            //
            // // Register observers to listen for when the upload is done or if it fails
            // uploadTask.addOnFailureListener(new OnFailureListener() {
            // @Override
            // public void onFailure(@NonNull Exception exception) {
            // // Handle unsuccessful uploads
            // Toast.makeText(event1.this, "Upload failed: " + exception.getMessage(),
            // Toast.LENGTH_SHORT).show();
            // }
            // }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            // @Override
            // public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // // When the image has successfully uploaded, we get its download URL
            // fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            // @Override
            // public void onSuccess(Uri uri) {
            // // Here you can use the download URL for further operations
            // Toast.makeText(event1.this, "Upload successful", Toast.LENGTH_SHORT).show();
            // }
            // });
            // }
            // });
        }
    }

    public void SubmitEvent(String name, String location, String date) {
        Map<String, Object> eventDetail = new HashMap<>();
        eventDetail.put("eventName", name);
        eventDetail.put("eventLocation", location);
        eventDetail.put("eventDate", date);
        eventDetail.put("eventOrganizer", mAuth.getCurrentUser().getDisplayName());
        eventDetail.put("eventOrganizerID",mAuth.getCurrentUser().getUid());

        // Get a reference to store file at "photos/<FILENAME>"
        StorageReference fileRef = storageRef
                .child("events/" + System.currentTimeMillis() + "-" + imageUri.getLastPathSegment());
        UploadTask uploadTask = fileRef.putFile(imageUri);

        // Register observers to listen for when the upload is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(addEvent.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // When the image has successfully uploaded, we get its download URL
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Here you can use the download URL for further operations
                        Toast.makeText(addEvent.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        eventDetail.put("eventImage", uri.toString());

                        db.collection("users")
                                .document(mAuth.getCurrentUser().getUid())
                                .collection("Our Events")

                                .add(eventDetail)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Toast.makeText(addEvent.this, "Event adding successful to users document", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//
//                                        Toast.makeText(addEvent.this, "Event adding successful", Toast.LENGTH_SHORT).show();
////                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                    }
//                                })
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        Toast.makeText(addEvent.this, "Event adding successful", Toast.LENGTH_SHORT).show();
//                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                    }
//                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addEvent.this, "Event not added to users document", Toast.LENGTH_SHORT).show();
                                        Log.w("TAG", "Error adding document", e);
                                    }
                                });

                        db.collection("Events")
                                .document()
                                .set(eventDetail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
//
                                        Toast.makeText(addEvent.this, "Event adding successful to events document", Toast.LENGTH_SHORT).show();
//                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(addEvent.this, "Event not added to events document", Toast.LENGTH_SHORT).show();
                                        Log.w("TAG", "Error adding document", e);

                                    }
                                });
                    }
                });
            }
        });


    }
}
