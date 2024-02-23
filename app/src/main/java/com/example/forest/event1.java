package com.example.forest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class event1 extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event1);

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
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);

            // Get a reference to store file at "photos/<FILENAME>"
            StorageReference fileRef = storageRef.child("photos/" + System.currentTimeMillis() + "-" + imageUri.getLastPathSegment());
            UploadTask uploadTask = fileRef.putFile(imageUri);

            // Register observers to listen for when the upload is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(event1.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // When the image has successfully uploaded, we get its download URL
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Here you can use the download URL for further operations
                            Toast.makeText(event1.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
