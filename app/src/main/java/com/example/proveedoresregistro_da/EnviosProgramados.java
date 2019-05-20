package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnviosProgramados extends AppCompatActivity implements RecyclerViewAdapterEnvios.onClickListenerRecycleItem {

    private static String SERVICIO_ENVIOS = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/envios.programados.por.fecha.php?";
    private static String SERVICIO_FECHAS = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/envios.programados.fecha.php?";

    private int id;
    String selected_date;

    ProgressDialog barradeProgreso;
    private static final String TAG = "EnviosProgramados";
    private Button recuperarButton;
    private Spinner spinner;

    private JSONArray enviosJSONArr;
    public List<ListItem> listItems;
    public List<String> listDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_programados);

        //Habilita la flecha en el action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();
        listDates = new ArrayList<String>();

        getId();
        getDates();

        barradeProgreso = new ProgressDialog(this);
        recuperarButton = findViewById(R.id.button_recuperar);

        //Recupera un JSONArray y lo muestra en RecycleView
        recuperarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarEnvios();
            }
        });
    }

    private void fillSpinner() {
        //Llenando spinner
        spinner = findViewById(R.id.date_selector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_date = parent.getItemAtPosition(position).toString();
                recuperarEnvios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDates() {
        final ProgressDialog barraDeProgreso = new ProgressDialog(EnviosProgramados.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        SERVICIO_FECHAS = SERVICIO_FECHAS + "id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SERVICIO_FECHAS, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray arr_fechas = new JSONArray();

                    arr_fechas = response.getJSONArray("Fecha_entrega");

                    for(int i=0; i<arr_fechas.length(); i++)
                    {
                        JSONObject fecha = arr_fechas.getJSONObject(i);
                        listDates.add(fecha.getString("fecha_entrega"));
                    }

                    fillSpinner();

                } catch (JSONException e) {
                    Toast.makeText(EnviosProgramados.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(EnviosProgramados.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            //getHeaders() se ejecuta automáticamente en cuanto se ejecuta la actividad
            @Override public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                //Hasheando desde el servidor (php) cifrado para el acceso de la carpeta
                String credenciales = "Basic YTAxMDIwNzI1OjAwMDA=";
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", credenciales);

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void recuperarEnvios() {

        //Mostrar barra de progreso
        final ProgressDialog barraDeProgreso = new ProgressDialog(EnviosProgramados.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        Log.d(TAG,SERVICIO_ENVIOS.toString());

        SERVICIO_ENVIOS = SERVICIO_ENVIOS + "id=" + id + "&" + "fecha='" + selected_date + "'";

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, SERVICIO_ENVIOS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray arr_envios = response.getJSONArray("Envios_programados");

                    listItems.clear();

                    for(int i=0; i<arr_envios.length(); i++)
                    {
                        JSONObject o = arr_envios.getJSONObject(i);

                        ArrayList<String> datos = new ArrayList<>();

                        datos.add(o.getString("no_envio"));
                        datos.add(o.getString("patio"));
                        datos.add(o.getString("entrega"));

                        ListItem item = new ListItem(datos);
                        item.setId(o.getInt("no_envio"));

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
        }) {
            //getHeaders() se ejecuta automáticamente en cuanto se ejecuta la actividad
            @Override public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                //Hasheando desde el servidor (php) cifrado para el acceso de la carpeta
                String credenciales = "Basic YTAxMDIwNzI1OjAwMDA=";
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", credenciales);

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(peticion);
    }

    private void getId() {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = userData.getInt("id", 0);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.envios_recycler_view);
        RecyclerViewAdapterEnvios adapter = new RecyclerViewAdapterEnvios(listItems, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClickRecycle(int position) {
        Intent toItemDetails = new Intent(this, DetallesDeEnvio.class);
        toItemDetails.putExtra("id", listItems.get(position).getId());
        startActivity(toItemDetails);
    }
}
