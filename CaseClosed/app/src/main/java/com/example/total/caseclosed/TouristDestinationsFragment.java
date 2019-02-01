package com.example.total.caseclosed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TouristDestinationsFragment extends Fragment implements View.OnClickListener {
    private Button saveCityButton;
    private DatabaseReference mDatabase; // Instance for Firebase Database
    private EditText editCityName;
    private String cityName;
    private Animation fade_in, fade_out;
    private ViewFlipper viewFlipper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_tourist_destinations, container, false);

        viewFlipper = (ViewFlipper) myView.findViewById(R.id.backgroundViewFlipper_id);
        fade_in = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000); // miliseconds
        viewFlipper.startFlipping();

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Instance of Firebase Database node

        editCityName = (EditText) myView.findViewById(R.id.id_city_name);
        saveCityButton = (Button) myView.findViewById(R.id.button_save_city_name);
        saveCityButton.setOnClickListener(this);

        //return inflater.inflate(R.layout.fragment_tourist_destinations, container, false);
        return myView;
    }

    @Override
    public void onClick(View v) {
        cityName = editCityName.getText().toString(); // Get value in EditText to a String variable
        if(cityName.isEmpty()) {
            Toast.makeText(getActivity(),"Please enter valid entry", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(getActivity(), "Written to Firebase: " + cityName, Toast.LENGTH_LONG).show();
            mDatabase.child("cityName").setValue(cityName);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String cityNameFirebase = dataSnapshot.child("cityName").getValue().toString(); // Get found address Object
                    //Toast.makeText(getActivity(), "Obtained from Firebase: " + cityNameFirebase, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Intent moveToAttractionsMapActivity = new Intent(getActivity(), AttractionsMapActivity.class);
            startActivity(moveToAttractionsMapActivity);
        }
    }

}
