package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class EnviosProgramados extends AppCompatActivity {

    //private static final String REQUEST_URL_A_ARREGLO_JSON = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/parcial_3/ejercicios/clase20190405-php/servicio_autos.php";

    private static final String REQUEST_URL_A_ARREGLO_JSON = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";

    ProgressDialog barradeProgreso;
    private static final String TAG = "EnviosProgramados";
    private Button recuperarButton;
    private View muestraDialogo;
    private TextView muestraTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_programados);

        barradeProgreso = new ProgressDialog(this);
        recuperarButton = findViewById(R.id.button_recuperar);

        recuperarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyJsonArrayRequest(REQUEST_URL_A_ARREGLO_JSON);
            }
        });
    }

    public void volleyJsonArrayRequest(String url){
        // REQUEST_TAG es utilizado para cancelar un request
        String  REQUEST_TAG = "mx.itesm.csf.splash.ArrayRequest";
        barradeProgreso.setMessage("Cargando datos...");
        barradeProgreso.show();

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        LayoutInflater li = LayoutInflater.from(EnviosProgramados.this);
                        muestraDialogo = li.inflate(R.layout.envios_elemento, null);
                        muestraTextView = (TextView)muestraDialogo.findViewById(R.id.texto_a_mostrar);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EnviosProgramados.this);
                        alertDialogBuilder.setView(muestraDialogo);
                        alertDialogBuilder
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                                .setCancelable(false)
                                .create();
                        muestraTextView.setText(response.toString());
                        alertDialogBuilder.show();
                        barradeProgreso.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error en: " + error.getMessage());
                barradeProgreso.hide();
            }
        });
        // Anexamos una peticion de tipo JsonArray a la cola
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }
}
