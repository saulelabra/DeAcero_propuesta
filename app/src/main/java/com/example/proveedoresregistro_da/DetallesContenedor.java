package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetallesContenedor extends AppCompatActivity {

    TextView placas, tipo, rfid;
    String SERVICIO_DETALLES_CONTENEDOR = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/contenedor.detalles.php?";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_contenedor);

        tipo = findViewById(R.id.tipo);
        placas = findViewById(R.id.placas);
        rfid = findViewById(R.id.rfid);

        fillDetails();
    }

    private void fillDetails() {
        final ProgressDialog barraDeProgreso = new ProgressDialog(DetallesContenedor.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        getId();

        SERVICIO_DETALLES_CONTENEDOR = SERVICIO_DETALLES_CONTENEDOR + "id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SERVICIO_DETALLES_CONTENEDOR, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try{
                    JSONArray array = new JSONArray();

                    array = response.getJSONArray("Contenedores_registrados");
                    JSONObject object = array.getJSONObject(0);

                    tipo.setText(object.getString("tipo"));
                    placas.setText(object.getString("placas"));
                    rfid.setText(object.getString("rfid"));

                }catch (JSONException e) {
                    Toast.makeText(DetallesContenedor.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(DetallesContenedor.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            //getHeaders() se ejecuta automáticamente en cuanto se ejecuta la actividad
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    private void getId()
    {
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
    }
}
