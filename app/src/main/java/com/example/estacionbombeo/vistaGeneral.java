package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.content.Intent;
import android.os.Bundle;

public class vistaGeneral extends AppCompatActivity {
    public static final String extraNombreEstacion="com.example.application.example.extraNombreEstacion";
    public static final String extraTiempo="com.example.application.example.extraTiempo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarBombas();
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

    public void cargarBombas(){
        //llamar a base de datos y pedir numero total de ids
        String[] ids;
        for(String id: ids){
            //por cada id de bomba anade un objeto el layout contenedor de vista general

        }
    }
}
