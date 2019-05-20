package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class AgregarCamion extends AppCompatActivity {

    int id;
    String url_get_trans = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/transportistas.registrados.php?";
    String url_add_camion = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/insert.camion.php?";
    ArrayList<ListItem> arr_trans;
    ArrayList<String> arr_trans_string;
    private Spinner spinner;
    EditText marca, modelo, tipo, color, rfid, placas;
    String marca_str, modelo_str, tipo_str, color_str, rfid_str, placas_str, transportista;
    Button btn_guardar;

    public void guardarYReg(View view)
    {
        Intent toMenu = new Intent(AgregarCamion.this, Menu.class);
        startActivity(toMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_camion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arr_trans = new ArrayList<>();
        arr_trans_string = new ArrayList<>();
        getId();
        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        tipo = findViewById(R.id.tipo);
        color = findViewById(R.id.color);
        rfid = findViewById(R.id.rfid);
        placas = findViewById(R.id.placas);
        btn_guardar = findViewById(R.id.btn_guardar);
        recuperarTransportista();


        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marca_str = marca.getText().toString();
                modelo_str = modelo.getText().toString();
                tipo_str = tipo.getText().toString();
                color_str = color.getText().toString();
                rfid_str = rfid.getText().toString();
                placas_str = placas.getText().toString();
            }
        });
    }

    void recuperarTransportista()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarCamion.this);
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
                        JSONObject fecha = json_arr_trans.getJSONObject(i);
                        arr_trans_string.add(fecha.getString("nombre"));
                    }

                    fillSpinner();

                } catch (JSONException e) {
                    Toast.makeText(AgregarCamion.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarCamion.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
}
