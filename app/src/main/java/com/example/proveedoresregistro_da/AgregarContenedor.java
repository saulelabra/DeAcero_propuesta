package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Map;

public class AgregarContenedor extends AppCompatActivity {

    int id;
    String url_get_trans = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/transportistas.registrados.php?";
    String url_add_contenedor = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/insert.contenedor.php?";
    ArrayList<ListItem> arr_trans;
    ArrayList<String> arr_trans_string;
    ArrayList<String> trans_id;
    private Spinner spinner;
    EditText modelo, tipo, rfid, placas;
    String modelo_str, tipo_str, rfid_str, placas_str, transportista;
    Button btn_guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contenedor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arr_trans = new ArrayList<>();
        arr_trans_string = new ArrayList<>();
        trans_id = new ArrayList<>();
        getId();
        tipo = findViewById(R.id.tipo);
        rfid = findViewById(R.id.rfid);
        placas = findViewById(R.id.marca);
        btn_guardar = findViewById(R.id.btn_guardar);

        recuperarTransportista();

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipo_str = tipo.getText().toString();
                rfid_str = rfid.getText().toString();
                placas_str = placas.getText().toString();
                transportista = spinner.getSelectedItem().toString();
                transportista = trans_id.get(arr_trans_string.indexOf(transportista));
                guardarDatos();

                Intent toMenu = new Intent (AgregarContenedor.this, Menu.class);
                startActivity(toMenu);
            }
        });
    }

    void recuperarTransportista()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarContenedor.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_get_trans = url_get_trans + "usuario=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_get_trans, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray json_arr_trans = new JSONArray();

                    json_arr_trans = response.getJSONArray("Transportistas_Registrados");

                    for(int i=0; i<json_arr_trans.length(); i++)
                    {
                        JSONObject json_obj = json_arr_trans.getJSONObject(i);
                        arr_trans_string.add(json_obj.getString("nombre"));
                        trans_id.add(json_obj.getString("id"));
                    }

                    fillSpinner();

                } catch (JSONException e) {
                    Toast.makeText(AgregarContenedor.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarContenedor.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    private void getId() {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = userData.getInt("id", 0);
    }

    private void fillSpinner() {
        //Llenando spinner
        spinner = findViewById(R.id.spinner_trans);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_trans_string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void guardarDatos()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarContenedor.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_add_contenedor = url_add_contenedor +
                "placas=" + placas_str + "&transportista=" + transportista + "&rfid=" +rfid_str + "&tipo=" + tipo_str;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url_add_contenedor, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarContenedor.this, "Datos ingresados", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarContenedor.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
}
