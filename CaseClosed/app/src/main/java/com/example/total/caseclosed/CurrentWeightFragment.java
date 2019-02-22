package com.example.total.caseclosed;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentWeightFragment extends Fragment implements View.OnClickListener {

    private EditText editThreshold;
    private Switch weightSwitch;
    private DatabaseReference mDatabase; // Instance for Firebase Database
    private CharSequence currentSwitchState;
    private TextView showCurrentWeight, displayThreshold;
    private String currentWeight;
    private double weight;
    private ToneGenerator feedback;
    private Button changeThreshold;
    private String currentThreshold;
    private double threshold;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weight, container, false);
        showCurrentWeight = (TextView) view.findViewById(R.id.showFirebaseWeight_id);
        displayThreshold = (TextView) view.findViewById(R.id.thresholdView_id);

       mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node
       if(feedback == null) {
            feedback = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        }

        // threshold read
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentThreshold = dataSnapshot.child("threshold").getValue().toString();

                displayThreshold.setText(currentThreshold ); // Show currentThreshold in TextView

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });


        // weight read
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentWeight = dataSnapshot.child("weight").getValue().toString();
                weight = Double.parseDouble(currentWeight);
                threshold = Double.parseDouble(currentThreshold);

                if(weight >= threshold) {
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

        // Do this when SAVE button is pressed
        //saveThreshold.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
