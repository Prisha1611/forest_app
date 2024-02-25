package com.example.forest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class login extends AppCompatActivity {
    private Button registerButton, loginButton;
    private RadioGroup userTypeGroup;
    private FirebaseAuth mAuth;

    private EditText email, password;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = findViewById(R.id.registerButtonLogin);
        loginButton = findViewById(R.id.loginButtonLogin);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        mAuth = FirebaseAuth.getInstance();
        userTypeGroup = findViewById(R.id.userTypeGroup);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                int selectedId = userTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                String userType = navigateUser(selectedRadioButton);

                if (userType != null) {
                    Login(emailString, passwordString, userType);
                } else {
                    // Handle the case where no user type is selected
                    Toast.makeText(login.this, "Please select a user type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Login(String email, String password, String userType) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign Good", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            Log.d("USER ID", userID);

                            DocumentReference docRef = db.collection("users").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("Document Data", "DocumentSnapshot data: " + document.getData().get("Type"));
                                            if(!userType.equals(document.getData().get("Type"))){
                                                Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                                                Log.d("User type", userType);
                                                Log.d("HERE", "HERE");
                                                mAuth.signOut();
                                            }
                                            else{
                                                Intent intent;
                                                if(userType.equals("Volunteer")){
                                                    intent = new Intent(getApplicationContext(), Volunteer_Menu.class);
                                                }
                                                else if(userType.equals("NGO")){
                                                    intent = new Intent(getApplicationContext(), addEvent.class);
                                                }
                                                else {
                                                    intent = new Intent(getApplicationContext(), Business.class);
                                                }
                                                startActivity(intent);
                                            }

                                        } else {
                                            Log.d("NO DA", "No such document");
                                        }
                                    } else {
                                        Log.d("TAG", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign Bad", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private String navigateUser(RadioButton selectedRadioButton) {
        if (selectedRadioButton.getId() == R.id.radioButtonVolunteerLogin) {
            return "Volunteer";
        } else if (selectedRadioButton.getId() == R.id.radioButtonNGOLogin) {
            return "NGO";
        } else if (selectedRadioButton.getId() == R.id.radioButtonBusinessLogin) {
            return "Business";
        } else {
            // Handle the case where neither is selected or an unexpected value is encountered
            Toast.makeText(login.this, "Please select a user type", Toast.LENGTH_SHORT).show();
            return null; // Return null to indicate an error or no selection
        }
    }

}