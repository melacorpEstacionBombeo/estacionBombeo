package com.example.estacionbombeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Historicos extends AppCompatActivity {
    private LineChart lineChart;
    private Spinner magnitud;
    private Spinner periodo;
    private String id;
    private String fecha_inicial="";
    private String fecha_final="";
    private String units="";
    private HashMap<Integer, String> valueLableMap;

    private ArrayList<Entry> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos);
        lineChart = (LineChart)findViewById(R.id.chart);
        valueLableMap=new HashMap<>();
        magnitud=(Spinner) findViewById(R.id.spinner);
        periodo=(Spinner) findViewById(R.id.spinner2);

        periodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String periodo_str=String.valueOf(periodo.getSelectedItem());
                if(periodo_str.equals("Personalizado")){
                    DatePickerFragment fechaFinal = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            // +1 because January is zero
                            fecha_final=year+"-"+(month+1)+"-"+day;

                        }
                    });
                    DatePickerFragment fechaInicial = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            // +1 because January is zero
                            fecha_inicial=year+"-"+(month+1)+"-"+day;
                            fecha_final=fecha_inicial;
                            fechaFinal.show(getSupportFragmentManager(), "Fecha final");
                        }
                    });

                    fechaInicial.show(getSupportFragmentManager(), "Fecha Inicial");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        id=getIntent().getStringExtra("id");
        Button boton_actualizar=(Button) findViewById(R.id.button5);
        Button boton_guardar=(Button) findViewById(R.id.button6);
        boton_actualizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actualizar();
            }
        });

        boton_guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                guardarCSV();
            }
        });
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
            return valueLableMap.get((int) value);
            }
        });
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);


    }
    private void getData(String magnitud_str, String periodo_str){
        entries = new ArrayList<>();
        String formato,funcion,parametro,fecha_inicio,fecha_fin;
        int anio=Calendar.getInstance().get(Calendar.YEAR);
        int mes=Calendar.getInstance().get(Calendar.MONTH)+1;
        int dia=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);


        if(magnitud_str.equals("Consumo")){
            units="Kwh";
            parametro="potencia";
            funcion="0.5*sum";
        }else if(magnitud_str.equals("Corriente")){
            units="Amps";
            parametro="corriente";
            funcion="avg";
        }else if(magnitud_str.equals("Temperatura")){
            units="C";
            parametro="temperatura";
            funcion="avg";
        }else if(magnitud_str.equals("Potencia")){
            units="Kw";
            parametro="potencia";
            funcion="avg";
        }else if(magnitud_str.equals("Encendidos")){
            units="Numero";
            parametro="encendidos";
            funcion="max";
        }else{
            units="Kw";
            parametro="potencia";
            funcion="avg";
        }

        if(periodo_str.equals("Ultimo dia")){
            formato="%25Y-%25m-%25d%20%25H";
            fecha_inicio=anio+"-"+mes+"-"+dia+"%2000:00:00";
            fecha_fin=anio+"-"+mes+"-"+dia+"%2023:59:59";
        }else if(periodo_str.equals("Ultima semana")){
            formato="%25Y-%25m-%25d";
            fecha_inicio=((mes==1&&dia-7<0)?(anio-1):anio)+"-"+((dia-7<0)?((mes-1==0)?12:mes-1):mes)+"-"+((dia-7<0)?23+dia:dia-7)+"%2000:00:00";
            fecha_fin=anio+"-"+mes+"-"+dia+"%2023:59:59";
        }else if(periodo_str.equals("Ultimo mes")){
            formato="%25Y-%25m-%25d";
            fecha_inicio=anio+"-"+mes+"-01%2000:00:00";
            fecha_fin=anio+"-"+mes+"-"+dia+"%2023:59:59";
        }else if(periodo_str.equals("Personalizado")){
            formato="%25Y-%25m-%25d";
            if(!(fecha_inicial.equals("")||fecha_final.equals(""))){
                fecha_inicio=fecha_inicial+"%2000:00:00";
                fecha_fin=fecha_final+"%2023:59:59";

            }else{
                fecha_inicio=anio+"-"+mes+"-01%2000:00:00";
                fecha_fin=anio+"-"+mes+"-"+dia+"%2023:59:59";
            }

        }else{
            formato="%25Y-%25m-%25d";
            fecha_inicio=anio+"-"+mes+"-01%2000:00:00";
            fecha_fin=anio+"-"+mes+"-"+dia+"%2023:59:59";
        }
        String url="historico.php?id="+id+"&parametro="+parametro+"&func="+funcion+"&format="+formato+"&fecha_origen=%27"+fecha_inicio+"%27&fecha_fin=%27"+fecha_fin+"%27";
        System.out.println(url);
        try {
            JSONArray jsonArray = (new DbResource()).getResourceURL(url);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String colx=obj.getString("fecha");
                entries.add(new Entry(Float.parseFloat(colx.substring(colx.length()-2,colx.length())), (float) obj.getDouble("valor")));
                valueLableMap.put(Integer.parseInt((colx.substring(colx.length()-2,colx.length()))), colx.substring(0,colx.length()-2));
                //System.out.println(colx+" , "+colx.substring(colx.length()-2,colx.length()-1)+", "+obj.getString("valor"));
            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void guardarCSV(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String magnitud_str=String.valueOf(magnitud.getSelectedItem());

        Context context = getApplicationContext();
        CharSequence text = "Archivo guardado en "+this.getExternalFilesDir(null);
        CharSequence text2="Error al guardar archivo";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        Toast toast2=Toast.makeText(context,text2,duration);
        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            File testFile = new File(this.getExternalFilesDir(null), magnitud_str+year+"-"+month+"-"+day+".csv");
            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false /*append*/));
            writer.write("fecha,"+magnitud_str+" "+units+"\n");
            for (Entry e: entries){
                writer.write(valueLableMap.get(Math.round(e.getX()))+Math.round(e.getX())+","+e.getY()+"\n");
            }

            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{testFile.toString()},
                    null,
                    null);
            toast.show();
        } catch (Exception e) {
            System.out.println(e);
            toast2.show();
        }
    }

    private void actualizar(){
        ActualizarDatosThread hilo=new ActualizarDatosThread(this);
        hilo.start();
    }

    private class ActualizarDatosThread extends Thread {
        Historicos parent;
        public ActualizarDatosThread(Historicos historicos) {
            parent=historicos;
        }

        @Override
        public void run() {
            String magnitud_str=String.valueOf(magnitud.getSelectedItem());
            String periodo_str=String.valueOf(periodo.getSelectedItem());
            getData(magnitud_str,periodo_str);
            LineDataSet lineDataSet = new LineDataSet(entries, magnitud_str);
            lineDataSet.setColor(ContextCompat.getColor(parent, R.color.colorPrimary));
            lineDataSet.setValueTextColor(ContextCompat.getColor(parent, R.color.colorPrimaryDark));
            LineData data = new LineData(lineDataSet);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    lineChart.setData(data);
                    lineChart.animateX(1000);
                    lineChart.invalidate();
                }
            });
        }
    }


}
