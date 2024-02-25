package com.example.forest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, name, pass1, pass2;
    private Button register;

    private RadioGroup userTypeGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailSignup);
        name = findViewById(R.id.nameSignup);
        pass1 = findViewById(R.id.passwordSignup);
        pass2 = findViewById(R.id.confirmPasswordSignup);
        register = findViewById(R.id.registerButtonSignup);
        userTypeGroup = findViewById(R.id.userTypeGroupSignup);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicking signup", Toast.LENGTH_SHORT).show();
                String emailString = email.getText().toString().trim();
                String nameString = name.getText().toString().trim();
                String pass1String = pass1.getText().toString().trim();
                String pass2String = pass2.getText().toString().trim();
//                String userType;

                int selectedId = userTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
//                if(selectedId == null){
//                    Toast.makeText(getApplicationContext(), "There is no radio button selected", Toast.LENGTH_SHORT).show();
//                    return;
//
//                }
//                Toast.makeText(getApplicationContext(), (CharSequence) selectedRadioButton, Toast.LENGTH_SHORT).show();
                String userType;
                if (selectedRadioButton.getId() == R.id.radioButtonVolunteerSignup) {
                    userType = "Volunteer";
                } else if (selectedRadioButton.getId() == R.id.radioButtonNGOSignup) {
                    userType = "NGO";
                } else if (selectedRadioButton.getId() == R.id.radioButtonBusinessSignup) {
                    userType = "Business";
                } else {
                    // Handle the case where neither is selected or an unexpected value is encountered
                    Toast.makeText(getApplicationContext(), "Please select a user type", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early if there's no selection
                }

                if (!emailString.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass1String.equals(pass2String)) {

                    if (!isValidPassword(pass1String)) {
                        Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                        pass1.setError("nooooooooooooo");
                    } else {
                        signupUser(emailString, pass1String, nameString, userType);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUser(String email, String password, String name, String userType) {
        Map<String, Object> userDetail = new HashMap<>();

        userDetail.put("Email", email);
        userDetail.put("Name", name);
        userDetail.put("Type", userType);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("created user", "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("UPDATING USER PROFILE", "User profile updated.");
                                                Toast.makeText(getApplicationContext(), "Account updated", Toast.LENGTH_SHORT).show();
//                                                val = true;
                                                Toast.makeText(getApplicationContext(), user.getDisplayName().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userDetail)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("TAG", "DocumentSnapshot added with ID: " + user.getUid());

                                            Intent intent = new Intent(getApplicationContext(), login.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error adding document", e);
                                        }
                                    });

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Could not", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);

                        }
                    }
                });

    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }


    private void navigateUser(RadioButton selectedRadioButton) {
        Intent intent;
        if (selectedRadioButton.getId() == R.id.radioButtonVolunteerLogin) {
            // Direct to Volunteer Activity
            // Make sure EventPage is the correct class name of your activity
//            intent = new Intent(login.this, mainpage.class);
        } else if (selectedRadioButton.getId() == R.id.radioButtonNGOLogin) {
            // Direct to NGO Activity
            // Make sure Event1 is the correct class name of your activity
//            intent = new Intent(login.this, event1.class);
        } else {
            // Handle the case where neither is selected or an unexpected value is encountered
//            Toast.makeText(login.this, "Please select a user type", Toast.LENGTH_SHORT).show();
//            return; // Exit the method early if there's no selection
        }
//        startActivity(intent);
//        return false;
    }


}
