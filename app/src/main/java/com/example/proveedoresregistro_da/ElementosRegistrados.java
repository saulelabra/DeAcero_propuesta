package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElementosRegistrados extends AppCompatActivity implements RecyclerViewAdapterElementos.onClickListenerRecycleItem {

    private static String SERVICIO_TRANSPORTISTAS = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";
    private static String SERVICIO_CHOFERES = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";
    private static String SERVICIO_CAMIONES = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";
    private static String SERVICIO_CONTENEDORES = "http://ubiquitous.csf.itesm.mx/~raulms/do/REST/ArregloJSON.app?count=2";

    ProgressDialog barradeProgreso;
    private static final String TAG = "ElementosRegistrados";
    private Button recuperarButton;

    private JSONArray enviosJSONArr;
    public List<ListItem> listItems;

    private int opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elementos_registrados);

        Bundle extras = getIntent().getExtras();
        opcion = extras.getInt("opcion");

        //Habilita la flecha en el action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listItems = new ArrayList<>();

        barradeProgreso = new ProgressDialog(this);
        recuperarButton = findViewById(R.id.button_recuperar);

        volleyJsonArrayRequest(opcion);

        //Recupera un JSONArray y lo muestra en RecycleView
        recuperarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyJsonArrayRequest(opcion);
            }
        });

    }

    private void volleyJsonArrayRequest(final int opcion) {

        String api_url = new String();

        //Mostrar barra de progreso
        final ProgressDialog barraDeProgreso = new ProgressDialog(ElementosRegistrados.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        switch (opcion)
        {
            case 0:
                api_url = SERVICIO_TRANSPORTISTAS;
                break;
            case 1:
                api_url = SERVICIO_CHOFERES;
                break;
            case 2:
                api_url = SERVICIO_CAMIONES;
                break;
            case 3:
                api_url = SERVICIO_CONTENEDORES;
                break;
        }

        Log.d(TAG,api_url.toString());

        JsonArrayRequest peticion = new JsonArrayRequest(api_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                try {
                    enviosJSONArr = response;

                    for(int i=0; i<enviosJSONArr.length(); i++)
                    {
                        JSONObject o = enviosJSONArr.getJSONObject(i);

                        ArrayList<String> datos = new ArrayList<>();

                        switch (opcion)
                        {
                            case 0:
                                datos.add(o.getString("nombre"));
                                break;
                            case 1:
                                datos.add(o.getString("nombre"));
                                datos.add(o.getString("apellido"));
                                datos.add(o.getString("transportista"));
                                break;
                            case 2:
                                datos.add(o.getString("marca"));
                                datos.add(o.getString("modelo"));
                                datos.add(o.getString("placas"));
                                break;
                            case 3:
                                datos.add(o.getString("placas"));
                                datos.add(o.getString("tipo"));
                        }

                        /*datos.add(o.getString("precio"));
                        datos.add(o.getString("url"));
                        datos.add(o.getString("idFoto"));*/

                        ListItem item = new ListItem(datos);

                        listItems.add(item);
                    }

                    initRecyclerView();

                } catch (JSONException e) {
                    Toast.makeText(ElementosRegistrados.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(ElementosRegistrados.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(peticion);

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.elementos_recycler_view);
        RecyclerViewAdapterElementos adapter = new RecyclerViewAdapterElementos(listItems, this, this, opcion);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //@Override
    public void onClickRecycle(int position) {
        Intent toItemDetails = new Intent(this, DetallesDeEnvio.class);
        startActivity(toItemDetails);
    }
}
