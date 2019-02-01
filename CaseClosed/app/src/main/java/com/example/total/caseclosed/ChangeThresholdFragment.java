package com.example.total.caseclosed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeThresholdFragment extends Fragment implements View.OnClickListener {

    private EditText editThreshold;
    private Button saveThreshold, chooseKG, chooseLBS;
    private String currentThreshold;
    private DatabaseReference mDatabase; // Instance for Firebase Database


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_threshold, container, false);

        saveThreshold = (Button) view.findViewById(R.id.saveButton_id);
        chooseKG = (Button) view.findViewById(R.id.kg_button_id);
        chooseLBS = (Button) view.findViewById(R.id.lbs_button_id);
        editThreshold = (EditText) view.findViewById(R.id.threshold_id);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node

        // Do this when SAVE button is pressed
        saveThreshold.setOnClickListener(this);

        //return inflater.inflate(R.layout.fragment_change_threshold, container, false);
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
        Intent moveToCurrentWeightActivity = new Intent(getActivity(), CurrentWeightActivity.class);

        currentThreshold = editThreshold.getText().toString(); // Get value in EditText to a String variable

        if(isInteger(currentThreshold)) {
            Toast.makeText(getActivity(), "Written to Firebase: " + currentThreshold, Toast.LENGTH_LONG).show();
            mDatabase.child("threshold").setValue(currentThreshold);

            moveToCurrentWeightActivity.putExtra("currentThreshold", currentThreshold);
            startActivity(moveToCurrentWeightActivity);
        }
        else {
            Toast.makeText(getActivity(), "Please enter something valid", Toast.LENGTH_LONG).show();
        }
    }
}
