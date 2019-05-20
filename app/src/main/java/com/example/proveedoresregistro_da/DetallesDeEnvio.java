package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

public class DetallesDeEnvio extends AppCompatActivity {

    TextView id_envio, fecha_registro, fecha_llegada, patio_destino, direccion_origen, transportista, nombre_chofer, apellido_chofer, ine_chofer, tipo_camion, marca_camion, modelo_camion, placas_camion, rfid_camion, placas_contenedor, tipo_contenedor, rfid_contenedor, material, comentarios, boleta_salida;
    String SERVICIO_DETALLES_ENVIO = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/envio.detalles.php?usuario=2&envio_id=105";
    int id, idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_de_envio);

        id_envio = findViewById(R.id.id_envio);
        fecha_registro = findViewById(R.id.fecha_registro);
        fecha_llegada = findViewById(R.id.fecha_llegada);
        patio_destino = findViewById(R.id.patio_destino);
        direccion_origen = findViewById(R.id.direccion_origen);
        transportista = findViewById(R.id.transportista);
        nombre_chofer = findViewById(R.id.nombre_chofer);
        apellido_chofer = findViewById(R.id.apellido_chofer);
        ine_chofer = findViewById(R.id.ine_chofer);
        tipo_camion = findViewById(R.id.tipo_camion);
        marca_camion = findViewById(R.id.marca_camion);
        modelo_camion = findViewById(R.id.modelo_camion);
        placas_camion = findViewById(R.id.placas_camion);
        rfid_camion = findViewById(R.id.rfid_camion);
        placas_contenedor = findViewById(R.id.placas_contenedor);
        tipo_contenedor = findViewById(R.id.tipo_contenedor);
        rfid_contenedor = findViewById(R.id.rfid_contenedor);
        material = findViewById(R.id.material);
        comentarios = findViewById(R.id.comentarios);
        boleta_salida = findViewById(R.id.boleta_salida);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillDetails();
    }

    public void fillDetails() {
        final ProgressDialog barraDeProgreso = new ProgressDialog(DetallesDeEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        getId();
        getIdUsuario();

        SERVICIO_DETALLES_ENVIO = SERVICIO_DETALLES_ENVIO + "usuario=" + idUsuario + "&" + "envio_id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, SERVICIO_DETALLES_ENVIO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try{
                    JSONArray array = new JSONArray();

                    array = response.getJSONArray("Detalles_envio");
                    JSONObject object = array.getJSONObject(0);

                    id_envio.setText(object.getString("id_envio"));
                    fecha_registro.setText(object.getString("Fecha_Registro"));
                    fecha_llegada.setText(object.getString("Fecha_Entrega"));
                    patio_destino.setText(object.getString("patio_destino"));
                    direccion_origen.setText(object.getString("direccion_origen"));
                    transportista.setText(object.getString("transportista"));
                    nombre_chofer.setText(object.getString("nombre_chofer"));
                    apellido_chofer.setText(object.getString("apellido_chofer"));
                    ine_chofer.setText(object.getString("ine_chofer"));
                    tipo_camion.setText(object.getString("tipo_camion"));
                    marca_camion.setText(object.getString("marca_camion"));
                    modelo_camion.setText(object.getString("modelo_camion"));
                    placas_camion.setText(object.getString("placas_camion"));
                    rfid_camion.setText(object.getString("rfid_camion"));
                    placas_contenedor.setText(object.getString("placas_contenedor"));
                    tipo_contenedor.setText(object.getString("tipo_contenedor"));
                    rfid_contenedor.setText(object.getString("rfid_contenedor"));

                    String material_y_cantidad = object.getString("material") + " " + object.getString("cantidad") + "t";
                    material.setText(material_y_cantidad);

                    comentarios.setText(object.getString("comentarios"));
                    boleta_salida.setText(object.getString("boleta_salida"));

                }catch (JSONException e) {
                    Toast.makeText(DetallesDeEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(DetallesDeEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    private void getIdUsuario()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        idUsuario = userData.getInt("id", 0);
    }
}
