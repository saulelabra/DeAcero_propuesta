package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity {

    LinearLayout envios, transportistas, choferes, camiones, contenedores;
    Map<String, String> userData = new HashMap<String, String>();

    TextView proveedor_TextView, direccion_TextView, num_envios_tv, num_transportistas_tv, num_choferes_tv, num_camiones_tv, num_contenedores_tv;

    public String COUNT_ENVIOS, COUNT_TRANSPORTISTAS, COUNT_CHOFERES, COUNT_CAMIONES, COUNT_CONTENEDORES;

    ProgressDialog barraDeProgreso;

    public void goToDetails (View view) {
        Intent toDetails = new Intent(Menu.this, detalles_proveedor.class);
        startActivity(toDetails);
    }

    public void regChofer(View view) {
        Intent toRegChofer = new Intent(Menu.this, AgregarChofer.class);
        startActivity(toRegChofer);
    }

    public void regCamion(View view) {
        Intent toRegCamion = new Intent(Menu.this, AgregarCamion.class);
        startActivity(toRegCamion);
    }

    public void regContenedor(View view) {
        Intent toRegContenedor = new Intent(Menu.this, AgregarContenedor.class);
        startActivity(toRegContenedor);
    }

    public void regTransportista(View view) {
        Intent toRegTransportista = new Intent (Menu.this, AgregarTransportista.class);
        startActivity(toRegTransportista);
    }

    public void regEnvio (View view) {
        Intent toRegEnvio = new Intent (Menu.this, AgregarEnvio.class);
        startActivity(toRegEnvio);
    }

    public void logout (View view) {

        wipeUserData();

        Intent toLogScreen = new Intent (Menu.this, Login.class);
        startActivity(toLogScreen);
    }

    public void help(View view) {
        Intent toHelp = new Intent(Menu.this, Contact_info.class);
        startActivity(toHelp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        envios = findViewById(R.id.envios);
        transportistas = findViewById(R.id.transportistas);
        choferes = findViewById(R.id.choferes);
        camiones = findViewById(R.id.camiones);
        contenedores = findViewById(R.id.contenedores);

        proveedor_TextView = findViewById(R.id.proveedor_tv);
        direccion_TextView = findViewById(R.id.direccion_tv);
        num_envios_tv = findViewById(R.id.cantidad_envios);
        num_transportistas_tv = findViewById(R.id.cantidad_transportistas);
        num_choferes_tv = findViewById(R.id.cantidad_choferes);
        num_camiones_tv = findViewById(R.id.cantidad_camiones);
        num_contenedores_tv = findViewById(R.id.cantidad_contenedores);

        barraDeProgreso = new ProgressDialog(Menu.this);

        envios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEnvios = new Intent(Menu.this, EnviosProgramados.class);
                startActivity(toEnvios);
            }
        });

        transportistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toElementosReg = new Intent(Menu.this, ElementosRegistrados.class);
                toElementosReg.putExtra("opcion", 0);
                startActivity(toElementosReg);
            }
        });

        choferes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toElementosReg = new Intent(Menu.this, ElementosRegistrados.class);
                toElementosReg.putExtra("opcion", 1);
                startActivity(toElementosReg);
            }
        });

        camiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toElementosReg = new Intent(Menu.this, ElementosRegistrados.class);
                toElementosReg.putExtra("opcion", 2);
                startActivity(toElementosReg);
            }
        });

        contenedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toElementosReg = new Intent(Menu.this, ElementosRegistrados.class);
                toElementosReg.putExtra("opcion", 3);
                startActivity(toElementosReg);
            }
        });

        userData = recuperarDatosUsuario();

        proveedor_TextView.setText(userData.get("nombreProveedor"));
        direccion_TextView.setText(userData.get("direccion"));

        fillDashboard();
    }

    public void fillDashboard()
    {
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        COUNT_ENVIOS = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/count.envio.php?";
        COUNT_TRANSPORTISTAS = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/count.transportista.php?";
        COUNT_CHOFERES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/count.chofer.php?";
        COUNT_CAMIONES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/count.camion.php?";
        COUNT_CONTENEDORES = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/count.contenedor.php?";

        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        int idUsuario = userData.getInt("id", 0);

        Log.d("debug_volley", "id usuario " + idUsuario);

        String id = "usuario";

        COUNT_ENVIOS = COUNT_ENVIOS + id + "=" + Integer.toString(idUsuario);
        COUNT_TRANSPORTISTAS = COUNT_TRANSPORTISTAS + id + "=" + Integer.toString(idUsuario);
        COUNT_CHOFERES = COUNT_CHOFERES + id + "=" + Integer.toString(idUsuario);
        COUNT_CAMIONES = COUNT_CAMIONES + id + "=" + Integer.toString(idUsuario);
        COUNT_CONTENEDORES = COUNT_CONTENEDORES + id + "=" + Integer.toString(idUsuario);

        Log.d("debug_volley", "guardo direcciones");

        count_envios(COUNT_ENVIOS);
        count_transportistas(COUNT_TRANSPORTISTAS);
        count_choferes(COUNT_CHOFERES);
        count_camiones(COUNT_CAMIONES);
        count_contenedores(COUNT_CONTENEDORES);
    }

    public void count_envios(String COUNT_ENVIOS)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COUNT_ENVIOS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug_volley", "obtuvo respuesta");
                barraDeProgreso.hide();
                try{
                    JSONArray arr_cantidad_envios = response.getJSONArray("Cantidad_envios");
                    int cantidad_envios_int = arr_cantidad_envios.getJSONObject(0).getInt("cantidad");

                    num_envios_tv.setText(Integer.toString(cantidad_envios_int));

                }catch (JSONException e) {
                    Toast.makeText(Menu.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Menu.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public void count_transportistas(String COUNT_TRANSPORTISTAS)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COUNT_TRANSPORTISTAS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug_volley", "obtuvo respuesta");
                barraDeProgreso.hide();
                try{
                    JSONArray arr_cantidad_transportistas = response.getJSONArray("Transportista");
                    int cantidad_transportistas_int = arr_cantidad_transportistas.getJSONObject(0).getInt("numero_transportistas");

                    num_transportistas_tv.setText(Integer.toString(cantidad_transportistas_int));

                }catch (JSONException e) {
                    Toast.makeText(Menu.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Menu.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public void count_choferes(String COUNT_CHOFERES)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COUNT_CHOFERES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug_volley", "obtuvo respuesta");
                barraDeProgreso.hide();
                try{
                    JSONArray arr_cantidad_choferes = response.getJSONArray("Cantidad_choferes");
                    int cantidad_choferes_int = arr_cantidad_choferes.getJSONObject(0).getInt("numero_choferes");

                    num_choferes_tv.setText(Integer.toString(cantidad_choferes_int));

                }catch (JSONException e) {
                    Toast.makeText(Menu.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Menu.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public void count_camiones(String COUNT_CAMIONES)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COUNT_CAMIONES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug_volley", "obtuvo respuesta");
                barraDeProgreso.hide();
                try{
                    JSONArray arr_cantidad_camiones = response.getJSONArray("Cantidad_camiones");
                    int cantidad_camiones_int = arr_cantidad_camiones.getJSONObject(0).getInt("numero_camiones");

                    num_camiones_tv.setText(Integer.toString(cantidad_camiones_int));

                }catch (JSONException e) {
                    Toast.makeText(Menu.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Menu.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public void count_contenedores(String COUNT_CONTENEDORES)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COUNT_CONTENEDORES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("debug_volley", "obtuvo respuesta");
                barraDeProgreso.hide();
                try{
                    JSONArray arr_cantidad_contenedores = response.getJSONArray("Cantidad_contenedores");
                    int cantidad_contenedores_int = arr_cantidad_contenedores.getJSONObject(0).getInt("numero_contenedores");

                    num_contenedores_tv.setText(Integer.toString(cantidad_contenedores_int));

                }catch (JSONException e) {
                    Toast.makeText(Menu.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Menu.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public Map<String, String> recuperarDatosUsuario()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        Map<String, String> userDataMap = new HashMap<String, String>();

        userDataMap.put("usuario", userData.getString("usuario", "none"));
        userDataMap.put("password", userData.getString("password", "none"));
        userDataMap.put("nombreProveedor", userData.getString("nombreProveedor", "none"));
        userDataMap.put("direccion", userData.getString("direccion", "none"));

        return userDataMap;
    }

    public void wipeUserData()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        editor.clear();
        editor.commit();
    }
}
