package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class registro_bomba extends AppCompatActivity {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_bomba);
        id=getIntent().getStringExtra("id");
        Intent intent=getIntent();
        //String nombreEstacion=intent.getStringExtra(vistaGeneral.extraNombreEstacion);
        String tiempo=intent.getStringExtra("horometro");

        TextView textView1=(TextView) findViewById(R.id.textView8);
        TextView textView2=(TextView) findViewById(R.id.textView9);

        textView1.setText("Bomba "+id);
        textView2.setText(tiempo);
        Button boton_tiempo_real=(Button) findViewById(R.id.button3);
        Button boton_historicos=(Button) findViewById(R.id.button4);
        boton_tiempo_real.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), TiempoReal.class);
                activity2Intent.putExtra("id",id);
                startActivity(activity2Intent);
            }
        });

        boton_historicos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), Historicos.class);
                activity2Intent.putExtra("id",id);
                startActivity(activity2Intent);
            }
        });
    }
}
