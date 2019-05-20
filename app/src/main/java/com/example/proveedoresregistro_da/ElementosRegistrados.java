package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ElementosRegistrados extends AppCompatActivity implements RecyclerViewAdapterElementos.onClickListenerRecycleItem {

    private static String SERVICIO_TRANSPORTISTAS = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/transportistas.registrados.php?";
    private static String SERVICIO_CHOFERES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/choferes.registrados.php?";
    private static String SERVICIO_CAMIONES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/camiones.registrados.php?";
    private static String SERVICIO_CONTENEDORES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/contenedores.registrados.php?";

    ProgressDialog barradeProgreso;
    private static final String TAG = "ElementosRegistrados";
    private Button recuperarButton;
    private TextView title;

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
        title = findViewById(R.id.title);

        final int id_usuario = getIdUser();

        fillData(opcion, id_usuario);

        //Recupera un JSONArray y lo muestra en RecycleView
        recuperarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillData(opcion, id_usuario);
            }
        });

    }

    private void fillData(final int opcion, int id_usuario) {

        String api_url = new String();

        //Mostrar barra de progreso
        final ProgressDialog barraDeProgreso = new ProgressDialog(ElementosRegistrados.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        switch (opcion)
        {
            case 0:
                title.setText("Transportistas");
                api_url = SERVICIO_TRANSPORTISTAS + "usuario=" + id_usuario;
                break;
            case 1:
                title.setText("Choferes");
                api_url = SERVICIO_CHOFERES + "usuario=" + id_usuario;
                break;
            case 2:
                title.setText("Camiones");
                api_url = SERVICIO_CAMIONES + "usuario=" + id_usuario;
                break;
            case 3:
                title.setText("Contenedores");
                api_url = SERVICIO_CONTENEDORES + "usuario=" + id_usuario;
                break;
        }

        Log.d(TAG,api_url.toString());

        JsonObjectRequest peticion = new JsonObjectRequest(Request.Method.GET, api_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray array = new JSONArray();

                    switch (opcion)
                    {
                        case 0:
                            array = response.getJSONArray("Transportistas_Registrados");
                            break;
                        case 1:
                            array = response.getJSONArray("Choferes_registrados");
                            break;
                        case 2:
                            array = response.getJSONArray("Camiones_registrados");
                            break;
                        case 3:
                            array = response.getJSONArray("Contenedores_registrados");
                            break;
                    }

                    listItems.clear();

                    for(int i=0; i<array.length(); i++)
                    {
                        JSONObject o = array.getJSONObject(i);

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

    private int getIdUser()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        int idUser = userData.getInt("id", 0);

        return idUser;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.elementos_recycler_view);
        RecyclerViewAdapterElementos adapter = new RecyclerViewAdapterElementos(listItems, this, this, opcion);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickRecycle(int position) {

        switch(opcion)
        {
            case 0:
                Intent toDetallesTransportista = new Intent(this, DetallesTransportista.class);
                startActivity(toDetallesTransportista);
                break;
            case 1:
                Intent toDetallesChofer = new Intent(this, DetallesChofer.class);
                startActivity(toDetallesChofer);
                break;
            case 2:
                Intent toDetallesCamion = new Intent(this, DetallesCamion.class);
                startActivity(toDetallesCamion);
                break;
            case 3:
                Intent toDetallesContenedor = new Intent(this, DetallesContenedor.class);
                startActivity(toDetallesContenedor);
                break;
        }
    }
}
