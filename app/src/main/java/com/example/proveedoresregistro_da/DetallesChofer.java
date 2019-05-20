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

public class DetallesChofer extends AppCompatActivity {

    TextView nombre, apellido, transportista;
    String SERVICIO_DETALLES_CHOFER = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/chofer.detalles.php?";
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_chofer);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        transportista = findViewById(R.id.transportista);

        fillDetails();
    }

    private void fillDetails() {
        final ProgressDialog barraDeProgreso = new ProgressDialog(DetallesChofer.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        getId();

        SERVICIO_DETALLES_CHOFER = SERVICIO_DETALLES_CHOFER + "id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SERVICIO_DETALLES_CHOFER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try{
                    JSONArray array = new JSONArray();

                    array = response.getJSONArray("Chofer_detalles");
                    JSONObject object = array.getJSONObject(0);

                    nombre.setText(object.getString("nombre"));
                    apellido.setText(object.getString("apellido"));
                    transportista.setText(object.getString("transportista"));

                }catch (JSONException e) {
                    Toast.makeText(DetallesChofer.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(DetallesChofer.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
