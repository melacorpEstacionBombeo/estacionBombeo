package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class TiempoReal extends AppCompatActivity {
    private CircleDisplay indicador_corriente;
    private CircleDisplay indicador_potencia;
    private CircleDisplay indicador_temperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempo_real);
        //indicador de corriente
        indicador_corriente = (CircleDisplay) findViewById(R.id.indicador_corriente);
        indicador_corriente.setAnimDuration(3000);
        indicador_corriente.setStartAngle(135f);
        indicador_corriente.setValueWidthPercent(55f);
        indicador_corriente.setTextSize(20f);
        indicador_corriente.setColor(0x00ff00);
        indicador_corriente.setDrawText(true);
        indicador_corriente.setDrawInnerCircle(true);
        indicador_corriente.setFormatDigits(1);
        indicador_corriente.setTouchEnabled(false);
        indicador_corriente.setUnit("[Amps]");

        // cd.setCustomText(...); // sets a custom array of text
        indicador_corriente.showValue(0f, 100f, true);
        //indicador de potencia
        indicador_potencia = (CircleDisplay) findViewById(R.id.indicador_potencia);
        indicador_potencia.setAnimDuration(3000);
        indicador_potencia.setStartAngle(135f);
        indicador_potencia.setValueWidthPercent(55f);
        indicador_potencia.setTextSize(20f);
        indicador_potencia.setColor(0x00ff00);
        indicador_potencia.setDrawText(true);
        indicador_potencia.setDrawInnerCircle(true);
        indicador_potencia.setFormatDigits(1);
        indicador_potencia.setTouchEnabled(false);
        indicador_potencia.setUnit("[kW]");

        // cd.setCustomText(...); // sets a custom array of text
        indicador_potencia.showValue(0f, 100f, true);
        //indicador de temperatura
        indicador_temperatura = (CircleDisplay) findViewById(R.id.indicador_temperatura);
        indicador_temperatura.setAnimDuration(3000);
        indicador_temperatura.setStartAngle(135f);
        indicador_temperatura.setValueWidthPercent(55f);
        indicador_temperatura.setTextSize(20f);
        indicador_temperatura.setColor(0x00ff00);
        indicador_temperatura.setDrawText(true);
        indicador_temperatura.setDrawInnerCircle(true);
        indicador_temperatura.setFormatDigits(1);
        indicador_temperatura.setTouchEnabled(false);
        indicador_temperatura.setUnit("[C]");

        // cd.setCustomText(...); // sets a custom array of text
        indicador_temperatura.showValue(0f, 100f, true);
    }
}
