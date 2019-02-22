package com.example.total.caseclosed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ChangeThresholdFragment extends Fragment implements View.OnClickListener {

    private EditText editThreshold;
    private Button saveThreshold, chooseKG, chooseLBS;
    private Switch weightSwitch;
    private String currentThreshold;
    private DatabaseReference mDatabase; // Instance for Firebase Database
    private CharSequence currentSwitchState;
    private TextView switchStatus;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_threshold, container, false);

        saveThreshold = (Button) view.findViewById(R.id.saveButton_id);

        // --------------------- SWITCH ------------------------------------

        Switch weightSwitch= (Switch)view.findViewById(R.id.weight_switch);
        switchStatus = (TextView) view.findViewById(R.id.weight_switch);
        // initially when app is launched always set to kg
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node
        mDatabase.child("metric").setValue(1);
        weightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    mDatabase.child("metric").setValue(1);
                }else{
                    mDatabase.child("metric").setValue(0);
                }

            }
        });


        editThreshold = (EditText) view.findViewById(R.id.threshold_id);

        // Do this when SAVE button is pressed
        saveThreshold.setOnClickListener(this);

        return view;
    }

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        if(s.isEmpty())
            return isValidInteger;

        try {
            // s is a valid integer
            Integer.parseInt(s);
            isValidInteger = true;
        }
        catch (NumberFormatException ex) {
            // s is not an integer
        }
        return isValidInteger;
    }


    @Override
    public void onClick(View v) {

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentWeightFragment ()).commit();


        currentThreshold = editThreshold.getText().toString(); // Get value in EditText to a String variable
        if(isInteger(currentThreshold)) {
            Toast.makeText(getActivity(), "Written to Firebase: " + currentThreshold, Toast.LENGTH_LONG).show();
            mDatabase.child("threshold").setValue(currentThreshold);
            Toast.makeText(getActivity(), "my threshold is"+currentThreshold,Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(getActivity(), "Please enter something valid", Toast.LENGTH_LONG).show();
        }

    }
}
