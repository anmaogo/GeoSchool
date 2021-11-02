package com.example.geoschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Notificaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
    }
    public void irprincipal(View v) {

        Intent intent= new Intent(getApplicationContext(), Principal.class);
        startActivity(intent);

    }
}