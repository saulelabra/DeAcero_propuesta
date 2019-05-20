package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.app.slice.Slice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private long tiempodeEspera = 3000; //milisegundos
    private boolean logged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        ImageView logo = findViewById(R.id.logo);
        Picasso.get().load("http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/res/deacero_splash.png").into(logo);

        checkStoredCredentials();

        TimerTask tarea = new TimerTask() {

            public void run() {
                finish();

                if(logged)
                {
                    Intent toMenu = new Intent(Splash.this, Menu.class);
                    startActivity(toMenu);
                }else{
                    Intent toLogin = new Intent(Splash.this, Login.class);
                    startActivity(toLogin);
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(tarea, tiempodeEspera);
    }

    private void checkStoredCredentials()
    {
        SharedPreferences datosUsuario = getSharedPreferences("userData", Context.MODE_PRIVATE);

        String user = datosUsuario.getString("usuario", "none");
        String password = datosUsuario.getString("password", "none");

        usuarioLogin(user, password);
    }

    private void usuarioLogin(final String usuario, final String password) {

        String SERVICIO_LOGIN = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/servicio.login.php?";
        final String USUARIO = "usuario";
        final String PASSWORD = "password";
        final String TAG = "Datos";

        final Usuario datosUsuario = new Usuario();

        SERVICIO_LOGIN = SERVICIO_LOGIN + USUARIO + "=" + usuario + "&" + PASSWORD + "=" + password;
        Log.d(TAG,SERVICIO_LOGIN.toString());

        JsonArrayRequest peticion = new JsonArrayRequest(SERVICIO_LOGIN, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject autenticacion = (JSONObject) response.get(0);
                    String codigo_autenticacion = autenticacion.getString("Codigo").toString();
                    //Log.d(TAG,response.toString());

                    if (codigo_autenticacion.equals("01")) {
                        JSONObject username = (JSONObject) response.get(2);

                        datosUsuario.getInstance().setId(username.getInt("id_usuario"));
                        datosUsuario.getInstance().setNombreProveedor(username.getString("nombreProveedor").toString());
                        datosUsuario.getInstance().setDireccion(username.getString("direccion").toString());
                        datosUsuario.getInstance().setRfc(username.getString("rfc"));
                        datosUsuario.getInstance().setCuenta_a_depositar(username.getInt("cuenta_a_depositar"));

                        datosUsuario.getInstance().setUsuario(usuario);
                        datosUsuario.getInstance().setPassword(password);

                        saveUserPreferences(username.getInt("id_usuario"), usuario, password, username.getString("direccion").toString(), username.getString("nombreProveedor").toString(), username.getString("rfc").toString(), username.getInt("cuenta_a_depositar"));

                        Log.d(TAG,response.toString());

                        logged = true;

                    } else if (codigo_autenticacion.equals("04")) {
                        Log.d(TAG,"Usuario o password incorrecto");

                        logged = false;
                    }
                } catch (JSONException e) {
                    Toast.makeText(Splash.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();

                if(checkIfLogged())
                {
                    logged = true;
                }
            }
        }) {
            @Override protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(USUARIO, usuario);
                map.put(PASSWORD, password);
                return map;
            }

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

    public void saveUserPreferences(int id, String usuario, String password, String direccion, String nombreProveedor, String rfc, int num_cuenta)
    {
        SharedPreferences datosUsuario = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor userDataEditor = datosUsuario.edit();

        userDataEditor.putInt("id", id);
        userDataEditor.putString("usuario", usuario);
        userDataEditor.putString("password", password);
        userDataEditor.putString("direccion", direccion);
        userDataEditor.putString("nombreProveedor", nombreProveedor);
        userDataEditor.putString("rfc", rfc);
        userDataEditor.putInt("cuenta_a_depositar", num_cuenta);

        userDataEditor.putBoolean("logged", true);

        userDataEditor.commit();
    }

    public boolean checkIfLogged()
    {
        SharedPreferences datosUsuario = getSharedPreferences("userData", Context.MODE_PRIVATE);

        boolean logged = datosUsuario.getBoolean("logged", false);

        return logged;
    }
}
