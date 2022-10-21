package com.example.mealerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ClientRegistration extends AppCompatActivity {

    private Button returnHome;
    private Button register;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private EditText textInputEmail;
    private EditText textInputPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        register = (Button) findViewById(R.id.buttonUserRegister);
        returnHome = (Button) findViewById(R.id.backToMainUserReg);
        textInputEmail = findViewById(R.id.emailUserRegister);
        textInputPassword = findViewById(R.id.passwordUserRegister);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });

       /* register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ADD IF STATEMENTS HERE
                //ADD IF STATEMENTS HERE FOR REGISTRATION AND STUFF, STORE INFO
                validateEmail();
                validatePassword();


                if (validateEmail() && validatePassword()){
                    String a = textInputEmail.getText().toString().trim();
                    String b = textInputPassword.getText().toString().trim();
                    a = a.replace(".",",");
                    System.out.println(a);
                    firebaseDatabase.getReference().child("users").child(a).child("type").setValue("client");
                    firebaseDatabase.getReference().child("users").child(a).child("password").setValue(b);
                    firebaseDatabase.getReference().child("users").child(a).child("username").setValue(a);



                    openHomePage();
                }
            }
        }); */

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();
                validatePassword();

                // replace '.' with ',' in emailhhjhj
                String emailWithCommas = textInputEmail.getText().toString().trim().replace(".", ",");

                // verifying if email exists and if password is correct
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = reference.orderByChild("username").equalTo(emailWithCommas);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // cannot make account
                            textInputEmail.setError("User already exists");
                            String passwordFromDB = snapshot.child(emailWithCommas).child("password").getValue().toString();
                            String userType = snapshot.child(emailWithCommas).child("type").getValue().toString();


                        } else {
                            // create the account
                            if (validateEmail() && validatePassword()){
                                String a = textInputEmail.getText().toString().trim();
                                String b = textInputPassword.getText().toString().trim();
                                a = a.replace(".",",");
                                System.out.println(a);
                                firebaseDatabase.getReference().child("users").child(a).child("type").setValue("cook");
                                firebaseDatabase.getReference().child("users").child(a).child("password").setValue(b);
                                firebaseDatabase.getReference().child("users").child(a).child("username").setValue(a);



                                openHomePage();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    public void openHomePage(){
        Intent intent = new Intent(this, InitialWelcome.class);
        startActivity(intent);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }



    private boolean validatePassword() {
        String passwordInput = textInputPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }

        //DO STUFF HERE TO UPLOAD TO FIREBASE


        //createClientAccount(para2, par2, ....);

    }

    private void createClientAccount(String email, String firstName, String lastName, String password,  String address, String creditCard) {
        // replace '.' with ',' for email
        email = email.replace('.', '.');

        // use email as username
        String userKey = email;

        firebaseDatabase.getReference().child("users").child(userKey).child("type").setValue("client");
        firebaseDatabase.getReference().child("users").child(userKey).child("first name").setValue(firstName);
        firebaseDatabase.getReference().child("users").child(userKey).child("last name").setValue(lastName);
        firebaseDatabase.getReference().child("users").child(userKey).child("passwrod").setValue(password);
        firebaseDatabase.getReference().child("users").child(userKey).child("address").setValue(address);
        firebaseDatabase.getReference().child("users").child(userKey).child("username").setValue(email);


        // not sure of storing credit card info...
        firebaseDatabase.getReference().child("users").child(userKey).child("credit card").setValue(creditCard);

    }



}