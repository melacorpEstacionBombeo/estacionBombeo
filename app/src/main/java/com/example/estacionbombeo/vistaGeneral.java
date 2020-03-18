package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class vistaGeneral extends AppCompatActivity {
    public static final String extraNombreEstacion="com.example.application.example.extraNombreEstacion";
    public static final String extraTiempo="com.example.application.example.extraTiempo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_general);
    }

    public void registroBomba(){
        String nombreEstacion="holaaa";
        Integer tiempo=12354;

        Intent intent = new Intent(this,registro_bomba.class);
        intent.putExtra(extraNombreEstacion,nombreEstacion);
        intent.putExtra(extraTiempo,tiempo);
        startActivity(intent);
    }
}
