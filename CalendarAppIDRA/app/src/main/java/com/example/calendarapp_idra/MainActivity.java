package com.example.calendarapp_idra;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar tooolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tooolbar = findViewById(R.id.toolbar); //Referenciamos la variable de objeto toolbar al que tenemos en la vista main
        setSupportActionBar(tooolbar);
        drawerLayout = findViewById(R.id.drawer); //Realizamos lo mismo :)
        navigationView = findViewById(R.id.navigationView);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,tooolbar,R.string.open,R.string.close); ///*BORRAR: Explicado" en minuto 17:35
        drawerLayout.addDrawerListener(actionBarDrawerToggle); //Le añadimos un listener al drawerLayout
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

    }
}