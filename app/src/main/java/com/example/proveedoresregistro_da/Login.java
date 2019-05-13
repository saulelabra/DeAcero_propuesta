package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public static String SERVICIO_LOGIN;

    public static final String USUARIO = "usuario";
    public static final String PASSWORD = "password";

    private final String TAG = "Datos";
    private EditText editTextUsuario;
    private EditText editTextPassword;

    private Button botonAcceder;

    private Usuario datosUsuario;

    private String usuario;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsuario = findViewById(R.id.user_et);
        editTextPassword = findViewById(R.id.pswd_et);

        botonAcceder = (Button) findViewById(R.id.login_btn);
        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuarioLogin();
            }
        });
    }

    private void usuarioLogin() {
        final ProgressDialog barraDeProgreso = new ProgressDialog(Login.this);
        barraDeProgreso.setMessage("Iniciando sesion...");
        barraDeProgreso.show();

        SERVICIO_LOGIN = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/login/servicio.login.php?";

        usuario = editTextUsuario.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        SERVICIO_LOGIN = SERVICIO_LOGIN + USUARIO + "=" + usuario + "&" + PASSWORD + "=" + password;
        Log.d(TAG,SERVICIO_LOGIN.toString());

        JsonArrayRequest peticion = new JsonArrayRequest(SERVICIO_LOGIN, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                try {
                    JSONObject autenticacion = (JSONObject) response.get(0);
                    String codigo_autenticacion = autenticacion.getString("Codigo").toString();
                    //Log.d(TAG,response.toString());

                    if (codigo_autenticacion.equals("01")) {
                        JSONObject username = (JSONObject) response.get(2);

                        datosUsuario.getInstance().setId(username.getString("id_usuario").toString());
                        datosUsuario.getInstance().setNombreProveedor(username.getString("nombreProveedor").toString());
                        datosUsuario.getInstance().setDireccion(username.getString("direccion").toString());

                        datosUsuario.getInstance().setUsuario(usuario);
                        datosUsuario.getInstance().setPassword(password);

                        Toast.makeText(Login.this,
                                "Bienvenido " + username.getString("nombreProveedor").toString(), Toast.LENGTH_LONG).show();
                        Log.d(TAG,response.toString());
                        Intent intent = new Intent(getBaseContext(), Terminos.class);
                        startActivity(intent);
                    } else if (codigo_autenticacion.equals("04")) {
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                        Log.d(TAG,"Usuario o password incorrecto");
                    }
                } catch (JSONException e) {
                    Toast.makeText(Login.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(Login.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

                //Hasheando desde la aplicación
                //String credentiales = usuario + ":" + password;
                //String autenticacion = "Basic " + Base64.encodeToString(credentiales.getBytes(), Base64.NO_WRAP);
                //headers.put("Content-Type", "application/json");
                //headers.put("Authorization", authentication);

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
}
