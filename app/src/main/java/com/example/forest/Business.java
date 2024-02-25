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

public class Business extends AppCompatActivity implements RecycledProductsAdapter.OnProductClickListener {

    private List<RecycledProduct> productList;

    private FirebaseAuth mAuth;

    private Button addProduct;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(getApplicationContext(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
        addProduct = findViewById(R.id.addProductPageButton);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addProduct.class));

            }
        });

        // Initialize the list of recycled products
        productList = new ArrayList<>();

        // Retrieve recycled products from Firestore
        db.collection("RecycledProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String productName = document.getString("productName");
                                String productQuantity = document.getString("productQuantity");
                                String productPrice = document.getString("productPrice");
                                String location = document.getString("location");
                                Uri productImage = Uri.parse(document.getString("productImage"));

                                productList.add(new RecycledProduct(productName, productQuantity, productPrice, location, productImage));
                            }

                            // Create the adapter with the list of products and set it to the RecyclerView
                            RecycledProductsAdapter adapter = new RecycledProductsAdapter(productList, Business.this);
                            RecyclerView recyclerView = findViewById(R.id.productsRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onProductClick(int position) {
        // Handle item click here if needed
    }
}
