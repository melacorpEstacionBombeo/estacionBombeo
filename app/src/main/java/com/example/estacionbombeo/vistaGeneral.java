package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

public class vistaGeneral extends AppCompatActivity {
    public static final String extraNombreEstacion="com.example.application.example.extraNombreEstacion";
    public static final String extraTiempo="com.example.application.example.extraTiempo";
    //array con los registros de cada bomba
    private ArrayList<View> registros_bomba=new ArrayList<>();
    private ArrayList<String> ids=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_general);
        //cargar bombas dinamicamente
        GetDBConnection dbc=new GetDBConnection();
        dbc.execute();
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
                for(final String id: ids){
                    //por cada id de bomba anade un objeto el layout contenedor de vista general
                    View bomba_view = inflater.inflate(R.layout.elemento_bomba, null);
                    registros_bomba.add(bomba_view);
                    final TextView tv_bomba_id=(TextView) bomba_view.findViewById(R.id.id_bomba);
                    final Button boton_bomba=(Button) bomba_view.findViewById(R.id.bomba);
                    //agrgar llamada a la activity registro bomba
                    boton_bomba.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // Code here executes on main thread after user presses button
                            registroBomba(id);
                        }
                    });
                    tv_bomba_id.setText(id);
                    contenedor.addView(bomba_view);

                }

            }
        });

    }

    private class GetDBConnection extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            //llamar a base de datos y pedir numero total de ids
            /*try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con=DriverManager.getConnection("jdbc:mysql://db4free.net:3306/melacorp2020?characterEncoding=utf8","melacorp","melacorp");
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery("select distinct id from bomba");
                while(rs.next())
                    ids.add(rs.getString(1));
                con.close();
            }catch(Exception e){ System.out.println(e);}*/
            try {
                System.out.println("1");
                URL url = new URL("https://waning-afternoons.000webhostapp.com/vista_general2.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.println("2");
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                System.out.println("3");
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                json=sb.toString().trim();
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    ids.add(obj.getString("id") );
                }
                System.out.println("4");
            } catch (Exception e) {
                System.out.println("fallo al conectar con api");;
            }
            cargarBombas();
            return null;
        }
    }
}

