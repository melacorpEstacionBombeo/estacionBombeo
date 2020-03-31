package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class TiempoReal extends AppCompatActivity {
    private CircleDisplay indicador_corriente;
    private CircleDisplay indicador_potencia;
    private CircleDisplay indicador_temperatura;
    private String id;
    private TextView hora_actual;
    private Boolean isCancelled;
    private Boolean paused;
    private ActualizarDatosThread dbc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempo_real);
        id=getIntent().getStringExtra("id");
        TextView tv=findViewById(R.id.id_bomba_actual);
        tv.setText("Bomba "+id);
        hora_actual=findViewById(R.id.hora_bomba_actual);
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
        indicador_corriente.showValue(0f,0f, 100f, true);
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
        indicador_potencia.showValue(0f,0f, 100f, true);
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
        indicador_temperatura.showValue(0f,0f, 100f, true);
        isCancelled=false;
        paused=false;
        dbc=new ActualizarDatosThread();
        dbc.start();

    }
    @Override
    public void onDestroy() {
        System.out.println("---ON DESTROY---");
        isCancelled=true;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        System.out.println("---ON RESUME---");
        super.onResume();
        synchronized (dbc){
            dbc.notifyAll();
        }
        paused=false;
    }

    @Override
    public void onPause() {
        paused=true;
        System.out.println("---ON PAUSE---");
        super.onPause();

    }

    @Override
    public void onStop() {
        paused=true;
        System.out.println("---ON STOP---");
        super.onStop();

    }

    private class ActualizarDatosThread extends Thread{
        @Override
        public void run(){
            while (true) {
                if ( isCancelled )
                    break;
                if(paused){
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


                    actualizarStatusBomba();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
        }

        private void actualizarStatusBomba(){

                JSONArray jsonArray = (new DbResource()).getResourceURL("tiempo_real.php?id="+id);
                try {
                    JSONObject obj = jsonArray.getJSONObject(0);

                    double corriente=obj.getDouble("corriente");
                    double temperatura=obj.getDouble("temperatura");
                    double potencia=obj.getDouble("potencia");
                    String time=obj.getString("time");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            indicador_corriente.showValue((float)corriente,(corriente>=0f)?(corriente<=400f?(float)corriente:400f):0f,400*(360.0f/270.0f),true);
                            indicador_temperatura.showValue((float) temperatura,(temperatura>=0f)?(temperatura<=70f?(float)temperatura:70f):0f,70*(360.0f/270.0f),true);
                            indicador_potencia.showValue((float)potencia,(potencia>=0f)?(potencia<=10f?(float)potencia:10f):0f,10*(360.0f/270.0f),true);
                            hora_actual.setText(time);
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }



        }
    }

}
