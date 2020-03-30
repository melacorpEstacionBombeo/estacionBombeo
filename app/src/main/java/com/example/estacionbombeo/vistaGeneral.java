package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

public class vistaGeneral extends AppCompatActivity {
    public static final String extraNombreEstacion="com.example.application.example.extraNombreEstacion";
    public static final String extraTiempo="com.example.application.example.extraTiempo";
    //array con los registros de cada bomba
    private ArrayList<TuplaBomba> registros_bomba=new ArrayList<>();
    private Boolean isCancelled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("---ON CREATE---");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_general);
        //cargar bombas dinamicamente
        isCancelled=false;
        ActualizarDatosThread dbc=new ActualizarDatosThread();
        dbc.start();
    }

    @Override
    public void onDestroy() {
        System.out.println("---ON DESTROY---");
        isCancelled=true;
        super.onDestroy();
    }

    @Override
    public void onStart() {
        System.out.println("---ON START---");
        super.onStart();

    }

    @Override
    public void onResume() {
        System.out.println("---ON RESUME---");
        super.onResume();

    }

    @Override
    public void onPause() {
        System.out.println("---ON PAUSE---");
        super.onPause();

    }

    @Override
    public void onStop() {
        System.out.println("---ON STOP---");
        super.onStop();

    }

    public void registroBomba(String id){
        Intent intent = new Intent(this,registro_bomba.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void cargarBombas(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //recuperar linearlayout contenedor
                LinearLayout contenedor = (LinearLayout) findViewById(R.id.contenedor_bombas);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for(final TuplaBomba tb: registros_bomba){
                    //por cada id de bomba anade un objeto el layout contenedor de vista general
                    View bomba_view = inflater.inflate(R.layout.elemento_bomba, null);
                    tb.v=bomba_view;
                    final TextView tv_bomba_id=(TextView) bomba_view.findViewById(R.id.id_bomba);
                    final Button boton_bomba=(Button) bomba_view.findViewById(R.id.bomba);
                    //agrgar llamada a la activity registro bomba
                    boton_bomba.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Code here executes on main thread after user presses button
                            registroBomba(tb.id);
                        }
                    });
                    tv_bomba_id.setText(tb.id);
                    contenedor.addView(bomba_view);

                }
                View progressBar=findViewById(R.id.progressBar);
                ViewGroup vg = (ViewGroup)(progressBar.getParent());
                vg.removeView(progressBar);
            }
        });

    }

    /*private class GetDBConnection extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            //llamar a base de datos y pedir numero total de ids
            while (true) {
                if ( isCancelled())
                    break;
                if(registros_bomba.isEmpty()) {
                    JSONArray jsonArray = (new DbResource()).getResourceURL("vista_general2.php");
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            registros_bomba.add(new TuplaBomba(obj.getString("id"),null));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    cargarBombas();
                }else {
                    actualizarStatusBomba();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }
    }

    private void actualizarStatusBomba(){
        for(TuplaBomba tb:registros_bomba){
            JSONArray jsonArray = (new DbResource()).getResourceURL("vista_general.php?id="+tb.id);
            try {
                JSONObject obj = jsonArray.getJSONObject(0);
                int status=obj.getInt("status");
                int alarma_corriente=obj.getInt("alarma_corriente");
                int alarma_temperatura=obj.getInt("alarma_temperatura");
                int alarma_fase=obj.getInt("alarma_fase");
                String time=obj.getString("time");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView) tb.v.findViewById(R.id.status_led)).setImageResource(status==1?R.drawable.on:R.drawable.off);
                        ((ImageView) tb.v.findViewById(R.id.temperatura_led)).setImageResource(alarma_temperatura==1?R.drawable.on:R.drawable.off);
                        ((ImageView) tb.v.findViewById(R.id.fase_led)).setImageResource(alarma_fase==1?R.drawable.on:R.drawable.off);
                        ((ImageView) tb.v.findViewById(R.id.corriente_led)).setImageResource(alarma_corriente==1?R.drawable.on:R.drawable.off);
                        ((TextView) tb.v.findViewById(R.id.time)).setText(time);
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }*/

    private class ActualizarDatosThread extends Thread{
        @Override
        public void run(){
            while (true) {
                if ( isCancelled )
                    break;
                if(registros_bomba.isEmpty()) {
                    JSONArray jsonArray = (new DbResource()).getResourceURL("vista_general2.php");
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            registros_bomba.add(new TuplaBomba(obj.getString("id"),null));
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    cargarBombas();
                }else {
                    actualizarStatusBomba();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        private void actualizarStatusBomba(){
            for(TuplaBomba tb:registros_bomba){
                JSONArray jsonArray = (new DbResource()).getResourceURL("vista_general.php?id="+tb.id);
                try {
                    JSONObject obj = jsonArray.getJSONObject(0);
                    int status=obj.getInt("status");
                    int alarma_corriente=obj.getInt("alarma_corriente");
                    int alarma_temperatura=obj.getInt("alarma_temperatura");
                    int alarma_fase=obj.getInt("alarma_fase");
                    String time=obj.getString("time");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) tb.v.findViewById(R.id.status_led)).setImageResource(status==1?R.drawable.on:R.drawable.off);
                            ((ImageView) tb.v.findViewById(R.id.temperatura_led)).setImageResource(alarma_temperatura==1?R.drawable.on:R.drawable.off);
                            ((ImageView) tb.v.findViewById(R.id.fase_led)).setImageResource(alarma_fase==1?R.drawable.on:R.drawable.off);
                            ((ImageView) tb.v.findViewById(R.id.corriente_led)).setImageResource(alarma_corriente==1?R.drawable.on:R.drawable.off);
                            ((TextView) tb.v.findViewById(R.id.time)).setText(time);
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        }
    }

    private class TuplaBomba{
        public String id;
        public View v;
        TuplaBomba(String id, View v){
            this.id=id;
            this.v=v;
        }
    }
}

