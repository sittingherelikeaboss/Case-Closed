package com.example.total.caseclosed;

import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.media.ToneGenerator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentWeightActivity extends AppCompatActivity {

    private DatabaseReference mDatabase; // Instance for Firebase Database

    private TextView showCurrentWeight, displayThreshold;
    private String currentWeight;
    private double currentThreshold;
    private double weight;
    private Button changeThreshold;
    private ToneGenerator feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weight);

        Intent intentExtras = getIntent(); // This will return intent that started this activity
        Bundle bd = intentExtras.getExtras();
        currentThreshold = Double.parseDouble((String) bd.get("currentThreshold"));

        showCurrentWeight = (TextView) findViewById(R.id.showFirebaseWeight_id);
        displayThreshold = (TextView) findViewById(R.id.thresholdView_id);
        changeThreshold = (Button) findViewById(R.id.changeThreshold_id);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node
        if(feedback == null) {
            feedback = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        }

        displayThreshold.setText("Threshold: " + Double.toString(currentThreshold));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentWeight = dataSnapshot.child("weight").getValue().toString();
                weight = Double.parseDouble(currentWeight);

                if(weight >= currentThreshold) {
                    showCurrentWeight.setText(currentWeight + " kg"); // Show currentThreshold in TextView
                    showCurrentWeight.setTextColor(Color.RED);
                    feedback.startTone(ToneGenerator.TONE_PROP_ACK); // Play tone for 50 ms
                }
                else {
                    showCurrentWeight.setText(currentWeight + " kg"); // Show currentThreshold in TextView
                    showCurrentWeight.setTextColor(Color.GREEN);
                    feedback.stopTone();
                }
                Log.d("Weight updated", currentWeight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });

        changeThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainActivity = new Intent(CurrentWeightActivity.this, MainActivity.class);
                startActivity(backToMainActivity);
            }
        });
    }
}
