package com.example.calendarapp_idra;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Button buttonSoft, buttonTera, buttonSegur, buttonEdu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSoft = (Button) findViewById(R.id.buttonSoftware);
        buttonTera = (Button) findViewById(R.id.buttonTerapeutico);
        buttonEdu = (Button) findViewById(R.id.buttonEducacion);

        buttonSoft.setOnClickListener(new View.OnClickListener() { //AL TOCAR EL BOTON, SE LLAMA AL METODO openActivity2, que por su nombre, abre la Activity2.
            @Override
            public void onClick(View v) {
            openActivity2();
            }
        });

        buttonTera.setOnClickListener(new View.OnClickListener() { //LO MISMO QUE EL DE ARRIBA, PERO EL METODO openActivity3.
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });

        buttonEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });


    }

    public void openActivity2() { //METODO PARA ABRIR LA SEGUNDA ACTIVIDAD.
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    public void openActivity3() { //METODO PARA ABRIR LA TERCERA ACTIVIDAD.
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    public void openActivity5() { //METODO PARA ABRIR LA QUINTA ACTIVIDAD.
        Intent intent = new Intent(this, Activity5.class);
        startActivity(intent);
    }
}