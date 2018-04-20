package org.ieselcaminas.alu53787365w.webservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by santi on 19/04/18.
 */

public class ConsultaActivity extends AppCompatActivity {
    ArrayList<String> cont = null;
    String IPExtra;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        cont = new ArrayList<String>();

        text = (TextView) findViewById(R.id.text);
// Des de la versió 3 d'Android, no es permet obrir una connexió des del thread principal.
// Per tant s'ha de crear un nou.
        sqlThread.start();

// i ara esperem a que finalitze el thread fill unint-lo (join)
        try {
            sqlThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<cont.size();i++)
            text.append(cont.get(i) + " ");

        Button inserir = (Button) findViewById(R.id.activitatInsercio);
        //Activitat per inserir rutes
        inserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Insercio.class);
                startActivity(intent);
            }
        });

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
                    cont.add("Ruta " + o.get("num_r") + ": " + o.get("nom_r")
                            + ". Desnivell: " + o.get("desn")
                            + ". Desnivell Acumulat: " + o.get("desn_acum") + "\n");
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
