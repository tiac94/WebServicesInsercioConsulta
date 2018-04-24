package org.ieselcaminas.alu53787365w.webservice;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by alu53787365w on 02/02/17.
 */

public class Insercio extends AppCompatActivity {
    private EditText nom_r, desn, desn_acum;
    Button inserir;
    TextView content;
    String nomStr, desnStr, desn_acumStr;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    ArrayList<String> arrRutes;
    //reinicia una Activity
    public static void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insercio);

        nom_r = (EditText) findViewById(R.id.nom_r);
        desn = (EditText) findViewById(R.id.desn);
        desn_acum = (EditText) findViewById(R.id.desn_acum);
        content = (TextView) findViewById(R.id.content);
        inserir = (Button) findViewById(R.id.inserir);

        arrRutes = new ArrayList<String>();
        // Des de la versió 3 d'Android, no es permet obrir una connexió des del thread principal.
// Per tant s'ha de crear un nou.
        sqlThread.start();

// i ara esperem a que finalitze el thread fill unint-lo (join)
        try {
            sqlThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//Initialize HTTPURLConnection class object
        service=new HTTPURLConnection();
        inserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    nomStr = nom_r.getText().toString();
                    desnStr = desn.getText().toString();
                    desn_acumStr = desn_acum.getText().toString();
                    boolean encontrat = false;

                    if (!nomStr.equals("") && !desnStr.equals("") && !desn_acumStr.equals("")) {
                        //Comprovem si hi ha un nom de la ruta repetida
                        for(String ruta: arrRutes){
                            if(ruta.equals(nomStr)){
                                encontrat = true;
                            }
                        }
                        if(encontrat==false) {

                            new Inserir().execute("http://192.168.1.116/webservice/insercio.php?nom_r="+nomStr+"&desn="+desnStr+"&desn_acum="+desn_acumStr);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "El nom de la ruta ja existeix!", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Hi ha informacio per replenar!", Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception ex) {
                    content.setText(" Url Exception!");
                }
            }
        });
        Button activitatConsulta = (Button) findViewById(R.id.activitatConsulta);
        activitatConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConsultaActivity.class);
                startActivity(intent);
            }
        });
    }

    private class Inserir extends AsyncTask<String, Void, Void> {
        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected Void doInBackground(String... urls) {

            postDataParams=new HashMap<String, String>();
            postDataParams.put("nom_r", nomStr);
            postDataParams.put("desn", desnStr);
            postDataParams.put("desn_acum", desn_acumStr);



                //Call ServerData() method to call webservice and store result in response
                response= service.ServerData(urls[0],postDataParams);
                try {
                    json = new JSONObject(response);
                    //Get Values from JSONobject
                    Log.d("SUCCESS","success=" + json.get("success"));
                    success = json.getInt("success");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            return null;
            }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(success==1) {
                Toast.makeText(getApplicationContext(), "Ruta afegida amb exit!", Toast.LENGTH_LONG).show();
                reiniciarActivity(Insercio.this);
            }
        }
        }


    Thread sqlThread = new Thread() {
        public void run() {
            try {
                URL url = new URL("http://192.168.1.116/webservice/rutes_2.php");
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String cadena = "";
                String linia;
                while ((linia = rd.readLine()) != null)
                    cadena += linia;

                JSONArray array = new JSONArray(cadena);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject o = (JSONObject) array.get(i);
                    arrRutes.add(o.get("nom_r").toString());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    


}
