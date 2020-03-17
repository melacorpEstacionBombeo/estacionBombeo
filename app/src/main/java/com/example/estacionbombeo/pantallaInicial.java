package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class pantallaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pantalla_inicial);



        Button iniciar_sesion = findViewById(R.id.button2);
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText usuario1=(EditText) findViewById(R.id.editText2);
                final String usuario=usuario1.getText().toString();
                EditText contrasenia1=(EditText) findViewById(R.id.editText);
                final String contrasenia=contrasenia1.getText().toString();

                if ((usuario.equals("melacorp")) &(contrasenia.equals("1234"))) {
                    Intent activityIntent = new Intent(getApplicationContext(), vistaGeneral.class);
                    startActivity(activityIntent);
                } else {
                    System.out.println("Datos ingresados incorrectamente");
                }
            }
        });

        Button acerca_de = findViewById(R.id.button);
        acerca_de.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), about.class);
                startActivity(activity2Intent);
            }
        });
    }
}
