package com.example.geoschool;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Principal extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mAuth= FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuLogOut)
        {
            mAuth.signOut();
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void irruta(View v) {

        Intent intent= new Intent(getApplicationContext(), Ruta.class);
        startActivity(intent);

    }
    public void irnotificacion(View v) {

        Intent intent= new Intent(getApplicationContext(), Notificaciones.class);
        startActivity(intent);

    }
    public void irchat(View v) {

        Intent intent= new Intent(getApplicationContext(), Chat.class);
        startActivity(intent);

    }

    public void ircodifo(View v) {

        Intent intent= new Intent(getApplicationContext(), activity_codigo.class);
        startActivity(intent);

    }

    public void irgestionruta(View v) {

        Intent intent= new Intent(getApplicationContext(), Gestion_Ruta.class);
        startActivity(intent);

    }



}