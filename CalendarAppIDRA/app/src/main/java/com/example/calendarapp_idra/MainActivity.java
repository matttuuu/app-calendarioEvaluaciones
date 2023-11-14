package com.example.calendarapp_idra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import Fragments.MonthlyFragment;
import Fragments.WeeklyFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    CustomCalendarView customCalendarView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar tooolbar;
    NavigationView navigationView;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tooolbar = findViewById(R.id.toolbar); //Referenciamos la variable de objeto toolbar al que tenemos en la vista main
        setSupportActionBar(tooolbar);
        customCalendarView = (CustomCalendarView)findViewById(R.id.custom_calendar_view); //CALENDAR VIEW
        drawerLayout = findViewById(R.id.drawer); //Realizamos lo mismo :)
        navigationView = findViewById(R.id.navigationView);
        //onclick al navigationView
        navigationView.setNavigationItemSelectedListener(this);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,tooolbar,R.string.open,R.string.close); ///*BORRAR: Explicado" en minuto 17:35
        drawerLayout.addDrawerListener(actionBarDrawerToggle); //Le añadimos un listener al drawerLayout
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        //Hacemos cargar el fragmento principal
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,new MonthlyFragment());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.view_fragment1){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new MonthlyFragment());
            fragmentTransaction.commit();
        }
        if (item.getItemId() == R.id.view_fragment2){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new WeeklyFragment());
            fragmentTransaction.commit();
        }

        return false;
    }

    public void PreviousMonth(View view) {
    }

    public void NextMonth(View view) {
    }
}