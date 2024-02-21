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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class login extends AppCompatActivity {
    Button registerButton, loginButton;
    private RadioGroup userTypeGroup;
    private FirebaseAuth mAuth;

    private EditText email, password;
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
                Login(emailString,passwordString);
            }
        });
    }

    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign Good", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                            Intent intent = new Intent(getApplicationContext(), mainpage.class);
//                            startActivity(intent);
                            int selectedId = userTypeGroup.getCheckedRadioButtonId();
                            RadioButton selectedRadioButton = findViewById(selectedId);
                            navigateUser(selectedRadioButton);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign Bad", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

    }
    private void navigateUser(RadioButton selectedRadioButton) {
        Intent intent;
        if (selectedRadioButton.getId() == R.id.radioButtonVolunteer) {
            // Direct to Volunteer Activity
            // Make sure EventPage is the correct class name of your activity
            intent = new Intent(login.this, mainpage.class);
        } else if (selectedRadioButton.getId() == R.id.radioButtonNGO) {
            // Direct to NGO Activity
            // Make sure Event1 is the correct class name of your activity
            intent = new Intent(login.this, event1.class);
        } else {
            // Handle the case where neither is selected or an unexpected value is encountered
            Toast.makeText(login.this, "Please select a user type", Toast.LENGTH_SHORT).show();
            return; // Exit the method early if there's no selection
        }
        startActivity(intent);
    }

}