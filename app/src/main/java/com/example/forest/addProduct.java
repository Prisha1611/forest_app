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
 public class addProduct extends AppCompatActivity{

     private static final int PICK_IMAGE_REQUEST = 1;
     private FirebaseStorage storage;
     private StorageReference storageRef;
     private ImageView imageView;
     private EditText productName, productQuantity, productPrice, productLocation;
     private Button submitProduct;
     private Uri imageUri;
     private FirebaseAuth mAuth;
     private FirebaseFirestore db;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_productpage);

         // Initialize Firebase components
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReference();
         mAuth = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();

         // Find views
         productName = findViewById(R.id.productName);
         productQuantity = findViewById(R.id.productQuantity);
         productPrice = findViewById(R.id.productPrice);
         productLocation = findViewById(R.id.productLocation);
         imageView = findViewById(R.id.uploaded_image_view);
         submitProduct = findViewById(R.id.submitProduct);

         // Open image chooser when the image view is clicked
         imageView = findViewById(R.id.uploaded_image_view);
         Button uploadImageButton = findViewById(R.id.button_upload_image);
         uploadImageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openImageChooser();
             }
         });

         // Submit button click listener
         submitProduct.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String name = productName.getText().toString().trim();
                 String quantity = productQuantity.getText().toString().trim();
                 String price = productPrice.getText().toString().trim();
                 String location = productLocation.getText().toString().trim();
                 if (name.isEmpty() || quantity.isEmpty() || price.isEmpty() || location.isEmpty() || imageUri == null) {
                     Toast.makeText(addProduct.this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                 } else {
                     submitProduct(name, quantity, price, location);
                 }
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
         }
     }

     private void submitProduct(String name, String quantity, String price, String location) {
         // Upload image to Firebase Storage
         StorageReference fileRef = storageRef.child("products/" + System.currentTimeMillis() + "-" + imageUri.getLastPathSegment());
         UploadTask uploadTask = fileRef.putFile(imageUri);

         uploadTask.addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                 Toast.makeText(addProduct.this, "Image upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
             }
         }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 // When the image has successfully uploaded, get its download URL
                 fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         // Image uploaded successfully, now add product details to Firestore
                         Map<String, Object> productDetail = new HashMap<>();
                         productDetail.put("productName", name);
                         productDetail.put("productQuantity", quantity);
                         productDetail.put("productPrice", price);
                         productDetail.put("productLocation", location);
                         productDetail.put("productImage", uri.toString());

                         // Add product details to Firestore
                         db.collection("RecycledProducts")
                                 .add(productDetail)
                                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {
                                         Toast.makeText(addProduct.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                         finish(); // Finish the activity after adding the product
                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(addProduct.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                     }
                                 });
                     }
                 });
             }
         });
     }
 }

