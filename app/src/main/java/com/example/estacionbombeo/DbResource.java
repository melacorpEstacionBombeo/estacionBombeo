package com.example.estacionbombeo;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DbResource {
    public JSONArray getResourceURL(String phpApi) throws Exception {
        URL url = null;
        HttpURLConnection con = null;
        String json=null;
        JSONArray jsonArray=null;

            url=new URL("https://waning-afternoons.000webhostapp.com/"+phpApi);
            con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            json = sb.toString().trim();
            jsonArray= new JSONArray(json);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        return jsonArray;
    }
}
