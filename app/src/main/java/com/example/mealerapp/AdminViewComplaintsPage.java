package com.example.mealerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AdminViewComplaintsPage extends AppCompatActivity {

    Button showbutton, dismiss, tempSuspend, fire, homeviewcompgo;
    EditText date;
    Spinner spinner;
    DatabaseReference databaseReference;
    ArrayList<String> complaints = new ArrayList<>();
    //List<String> names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_complaints_page);


        showbutton = (Button)findViewById(R.id.show);

        dismiss = (Button)findViewById(R.id.dismiss);
        tempSuspend = (Button)findViewById(R.id.tmpsus);
        fire = (Button)findViewById(R.id.fire);
        homeviewcompgo = (Button)findViewById(R.id.homeviewcomp);


        spinner = (Spinner) findViewById (R.id.spinner);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        homeviewcompgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });


        showbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner.getCount()!=0) {
                    String item = spinner.getSelectedItem().toString();
                    Toast.makeText(AdminViewComplaintsPage.this, item, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });



        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner.getCount()!=0) {
                    String item = spinner.getSelectedItem().toString();
                    String[] listOfString = item.split(",");
                    String[] listOfString2 = listOfString[0].split("=");
                    String username = listOfString2[1] + "," + listOfString[1];
                    databaseReference.child("complaints").child(username).removeValue();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tempSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner.getCount()!=0) {
                    String item = spinner.getSelectedItem().toString();

                    date = (EditText) findViewById(R.id.editTextDate);
                    String dateString = date.getText().toString().trim();

                    if (dateString.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {

                        String[] listOfString = item.split(",");
                        String[] listOfString2 = listOfString[0].split("=");
                        String username = listOfString2[1] + "," + listOfString[1];
                        databaseReference.child("users").child(username).child("suspensionStatus").setValue(dateString);
                        databaseReference.child("complaints").child(username).removeValue();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please follow the format YYYY-MM-DD exactly",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }




            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinner.getCount()!=0) {

                    String item = spinner.getSelectedItem().toString();
                    String[] listOfString = item.split(",");
                    String[] listOfString2 = listOfString[0].split("=");
                    String username = listOfString2[1] + "," + listOfString[1];

                    databaseReference.child("users").child(username).child("suspensionStatus").setValue("p");
                    databaseReference.child("complaints").child(username).removeValue();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });


        databaseReference.child("complaints").addValueEventListener(new ValueEventListener() {

            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                complaints.clear();


                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String spinnerComplaint = item.getValue().toString();

                    complaints.add(spinnerComplaint);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdminViewComplaintsPage.this, android.R.layout.simple_spinner_dropdown_item, complaints);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);

            }


            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }



        });
    }
    public void openHomePage(){
        Intent intent = new Intent(this, AdminSuccessfulLogin.class);
        startActivity(intent);
    }
}