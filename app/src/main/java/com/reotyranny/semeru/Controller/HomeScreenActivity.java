package com.reotyranny.semeru.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reotyranny.semeru.Model.Model;
import com.reotyranny.semeru.R;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Model FB = Model.getInstance();
        FirebaseUser user = FB.getUser();

        Button signOutButton = findViewById(R.id.button_SignOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, WelcomeScreenActivity.class));
            }
        });

        Button loadIn = findViewById(R.id.button_LoadIn);
        loadIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, LocationListActivity.class));
            }
        });

        final TextView currentUserText = findViewById(R.id.currentUser_TextView);
        final TextView welcomeUserText = findViewById(R.id.text_Welcome);

        if (user != null && user.getEmail() != null) {
            // User is signed in
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("users").orderByChild("email").equalTo(user.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Database-Error", databaseError.getMessage());
                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // query matches exactly to user.getEmail() -- just use 1 iteration
                        DataSnapshot item = dataSnapshot.getChildren().iterator().next();
                        String name = item.child("name").getValue().toString();
                        String currentUserString = String.format(
                                getResources().getString(R.string.current_user), name);
                        String welcomeString = String.format(
                                getResources().getString(R.string.welcome_user), name);
                        currentUserText.setText(currentUserString);
                        welcomeUserText.setText(welcomeString);
                    }
                }
            });
        }

    }

}
