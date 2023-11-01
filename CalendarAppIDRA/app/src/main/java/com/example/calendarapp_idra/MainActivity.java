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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import Fragments.MonthlyFragment;
import Fragments.WeeklyFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar tooolbar;
    NavigationView navigationView;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    //Declaracion de variable para utilizar db
    //private FirebaseFirestore db= FirebaseFirestore.getInstance();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //*variables temporales de elementos para guardar en la base de datos*
    EditText etNombre = findViewById(R.id.tempEt1); //logcat error line 52 ???
    EditText etApellido = findViewById(R.id.tempEt2);
    Button btnSaveToDb = findViewById(R.id.buttonSaveTemp);
    //*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tooolbar = findViewById(R.id.toolbar); //Referenciamos la variable de objeto toolbar al que tenemos en la vista main
        setSupportActionBar(tooolbar);
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



        //Para guardar en db
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



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


    public void saveToDb(View view) {
        String nom = this.etNombre.getText().toString();
        String ap = this.etApellido.getText().toString();

        HashMap <String,Object> hashmap = new HashMap<>();
        hashmap.put("nombre",nom);
        hashmap.put("apellido",ap);
        databaseReference.child("Eventos")
                .child(nom)
                .child(ap)
                .setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText( MainActivity.this,"Operacion Exitosa",Toast.LENGTH_SHORT).show();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Operacion fallida: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    {

        }






    }
}