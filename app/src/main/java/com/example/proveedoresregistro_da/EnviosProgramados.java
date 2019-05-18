package com.example.proveedoresregistro_da;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnviosProgramados extends AppCompatActivity implements RecyclerViewAdapter.onClickListenerRecycleItem {

    private static final String REQUEST_URL_A_ARREGLO_JSON = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";
    private static String SERVICIO_ENVIOS = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";

    ProgressDialog barradeProgreso;
    private static final String TAG = "EnviosProgramados";
    private Button recuperarButton;

    private JSONArray enviosJSONArr;
    public List<ListItem> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_programados);

        //Habilita la flecha en el action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();

        barradeProgreso = new ProgressDialog(this);
        recuperarButton = findViewById(R.id.button_recuperar);

        volleyJsonArrayRequest();

        //Recupera un JSONArray y lo muestra en RecycleView
        recuperarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyJsonArrayRequest();
            }
        });
    }

    private void volleyJsonArrayRequest() {

        //Mostrar barra de progreso
        final ProgressDialog barraDeProgreso = new ProgressDialog(EnviosProgramados.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        Log.d(TAG,SERVICIO_ENVIOS.toString());

        JsonArrayRequest peticion = new JsonArrayRequest(SERVICIO_ENVIOS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                try {
                    enviosJSONArr = response;

                    for(int i=0; i<enviosJSONArr.length(); i++)
                    {
                        JSONObject o = enviosJSONArr.getJSONObject(i);
                        ListItem item = new ListItem(
                                o.getString("precio"),
                                o.getString("url"),
                                o.getString("idFoto")
                        );

                        listItems.add(item);
                    }

                    initRecyclerView();

                } catch (JSONException e) {
                    Toast.makeText(EnviosProgramados.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(EnviosProgramados.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(peticion);

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.envios_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(listItems, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClickRecycle(int position) {
        Intent toItemDetails = new Intent(this, DetallesDeEnvio.class);
        startActivity(toItemDetails);
    }
}
