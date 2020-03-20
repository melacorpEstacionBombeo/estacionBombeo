package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class vistaGeneral extends AppCompatActivity {
    public static final String extraNombreEstacion="com.example.application.example.extraNombreEstacion";
    public static final String extraTiempo="com.example.application.example.extraTiempo";
    //array con los registros de cada bomba
    private ArrayList<View> registros_bomba=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_general);
        //cargar bombas dinamicamente
        cargarBombas();
    }

    public void registroBomba(String id){
        Intent intent = new Intent(this,registro_bomba.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void cargarBombas(){
        //llamar a base de datos y pedir numero total de ids
        ArrayList<String> ids=new ArrayList<>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://db4free.net:3306/melacorp202","melacorp","melacorp");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select distinct id from bomba");
            while(rs.next())
                ids.add(rs.getString(1));
            con.close();
        }catch(Exception e){ System.out.println(e);}

    //recuperar linearlayout contenedor
        LinearLayout contenedor = (LinearLayout) findViewById(R.id.contenedor_bombas);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(final String id: ids){
            //por cada id de bomba anade un objeto el layout contenedor de vista general
            View bomba_view = inflater.inflate(R.layout.elemento_bomba, null);
            registros_bomba.add(bomba_view);
            final Button boton_bomba=(Button) bomba_view.findViewById(R.id.bomba);
            //agrgar llamada a la activity registro bomba
            boton_bomba.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Code here executes on main thread after user presses button
                    registroBomba(id);
                }
            });
            contenedor.addView(bomba_view);
        }
    }
}
