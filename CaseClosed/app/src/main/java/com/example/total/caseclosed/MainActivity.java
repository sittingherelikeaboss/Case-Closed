package com.example.total.caseclosed;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        Toolbar menuToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(menuToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, menuToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // This will show our change threshold fragment when we actually want it
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentWeightFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_current_weight);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_current_weight:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CurrentWeightFragment ()).commit();
                Toast.makeText(this,"Selected 'Home'", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_change_threshold:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeThresholdFragment()).commit();
                Toast.makeText(this,"Selected 'Change Threshold'", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_tourist_destinations:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TouristDestinationsFragment()).commit();
                Toast.makeText(this, "Selected 'Google Places API'", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed(); // if you press back button, it will stay in this activity
        }
    }
}
