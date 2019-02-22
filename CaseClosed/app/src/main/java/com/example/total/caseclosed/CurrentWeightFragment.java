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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
//public final class Math;

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
    private ProgressBar weightProgress;
    private double weightPercentage;
    private String currentUnit;
    private String unit;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_weight, container, false);
        showCurrentWeight = (TextView) view.findViewById(R.id.showFirebaseWeight_id);
        displayThreshold = (TextView) view.findViewById(R.id.thresholdView_id);
        weightProgress= (ProgressBar) view.findViewById(R.id.weightProgressBar);

       mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node
       if(feedback == null) {
            feedback = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        }
        //-------------------------------------------- Set kg/lb
        //initialize to kg
        unit="kg";
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUnit= dataSnapshot.child("metric").getValue().toString();
                double cu = Double.parseDouble(currentUnit);
                if(cu==1){
                    unit="kg";
                }
                else{
                    unit="lb";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });


        // ----------------------- threshold read
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentThreshold = dataSnapshot.child("threshold").getValue().toString();
                displayThreshold.setText(currentThreshold+" "+unit); // Show currentThreshold in TextView
                displayThreshold.setTextColor(Color.parseColor("#060606"));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });


        // ------------------------ weight read
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentWeight = dataSnapshot.child("weight").getValue().toString();
                // to show one decimal place only
                weight = Double.parseDouble(currentWeight);
                double CutcurrentWeight =Double.parseDouble(new DecimalFormat("##.#").format(weight));
                threshold = Double.parseDouble(currentThreshold);

                if(weight >= threshold) {
                    showCurrentWeight.setText(CutcurrentWeight + " "+unit); // Show currentThreshold in TextView
                    showCurrentWeight.setTextColor(Color.parseColor("#D43229"));
                    feedback.startTone(ToneGenerator.TONE_PROP_ACK); // Play tone for 50 ms
                }
                else {
                    showCurrentWeight.setText(CutcurrentWeight + " "+unit); // Show currentThreshold in TextView
                    showCurrentWeight.setTextColor(Color.parseColor("#21B30C"));
                    feedback.stopTone();
                }
                Log.d("Weight updated", currentWeight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });


        //-------------------------------------------- Weight Progress bar
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weightPercentage=100*((float)weight/(float)threshold);
                double fwp=java.lang.Math.ceil(weightPercentage);
                int wp = (int) fwp ;
                weightProgress.setProgress(wp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Add something here when something bad happens
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
