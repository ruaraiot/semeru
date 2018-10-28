package com.reotyranny.semeru.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reotyranny.semeru.Model.Donation;
import com.reotyranny.semeru.Model.Model;
import com.reotyranny.semeru.R;
import java.util.Objects;


public class SpecificItemActivity extends AppCompatActivity {

    private final Model model = Model.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_item);

        final String itemKey = (String) getIntent().getSerializableExtra("itemKey");
        final int locationKey = (int) getIntent().getSerializableExtra("locationKey");

        Query query = model.getRef().child(Model.DONATIONS).child("" + itemKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Database-Error", databaseError.getMessage());
            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Donation donation = dataSnapshot.getValue(Donation.class);
                    populateFields(Objects.requireNonNull(donation));
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecificItemActivity.this, ItemListActivity.class);
                intent.putExtra("locationKey", locationKey);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void populateFields(Donation d) {
        TextView shortDesView = findViewById(R.id.text_Short);
        shortDesView.setText("Short description: " + d.getShortDes());
        TextView time = findViewById(R.id.text_Full);
        time.setText("Time: " + d.getTimestamp());
        TextView valueView = findViewById(R.id.text_Value);
        valueView.setText("Value: " + d.getValue());
        TextView loc = findViewById(R.id.text_Location);
        loc.setText("Location: " + d.getPlace());
        TextView categoryView = findViewById(R.id.text_Category);
        categoryView.setText("Category: " + d.getCategory());
        TextView commentsView = findViewById(R.id.text_Comments);
        commentsView.setText("Comments: " + d.getComments());
//        TextView Timestamp = findViewById(R.id.text_Timestamp);
//        Timestamp.setText(d.getTimestamp());
    }
}
