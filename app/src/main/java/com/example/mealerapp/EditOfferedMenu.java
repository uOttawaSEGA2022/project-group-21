package com.example.mealerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Spinner;
import android.widget.Toast;


public class EditOfferedMenu extends AppCompatActivity {
    Spinner spinner;
    DatabaseReference databaseReference;

    ArrayList<String> menu = new ArrayList<>();
    ArrayList<String> menuIndex = new ArrayList<>();

    String selection;


    private Button addMeal;
    private Button removeMeal;

//List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offered_menu);

        // MainLogin e = new MainLogin();
        String email =  MainLogin.emailWithCommas;
        TextView textView = (TextView)findViewById(R.id.textView5);
        // MainLogin.email

        spinner = (Spinner) findViewById (R.id.spinner);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(email).child("menu").addValueEventListener(new ValueEventListener() {



            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                menu.clear();
                menuIndex.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String spinnerComplaint = item.getKey().toString();

                    if (item.child("offered").getValue()!= null){

                        String index = item.getValue().toString();

                        menu.add(spinnerComplaint);
                        menuIndex.add(index);

                    }



                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditOfferedMenu.this, android.R.layout.simple_spinner_dropdown_item, menu);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);



            }


            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }

        });


        addMeal = (Button) findViewById(R.id.button4);

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMeal.setError(null); // clear error

                if (spinner.getCount()!=0) {



                        selection = spinner.getSelectedItem().toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                        ref.child(email).child("menu").child(selection).child("offered").setValue(true);

                        textView.setText("You added  '" + selection + "'  to the offered menu");



                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * remove a meal from the offered menu
         *
         * */
        removeMeal = (Button) findViewById(R.id.button5);

        removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMeal.setError(null); // clear error

                if (spinner.getCount()!=0) {
                selection = spinner.getSelectedItem().toString();
                int indexOfMeal = menu.indexOf(selection);
                String exist = menuIndex.get(indexOfMeal);


                    if (exist.contains("false")){
                        removeMeal.setError("meal does not exist in offered menu.");

                    }
                    else {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                        ref.child(email).child("menu").child(selection).child("offered").setValue(false);
                        textView.setText("You removed  '" + selection + "'  from the offered menu");
                        exist = "false";
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Dropdown is empty",Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*
        * home button
        * */
        Button returnHome = (Button) findViewById(R.id.goHomePage);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });



    }


    public void openHomePage(){
        Intent intent = new Intent(this, CookSuccessfulLogin.class);
        startActivity(intent);
    }
}


