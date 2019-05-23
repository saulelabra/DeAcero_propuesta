package com.example.proveedoresregistro_da;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AgregarTransportista extends AppCompatActivity {

    EditText nom, tel, correo;
    String nom_str, tel_str, correo_str;
    String url_add_transportista = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/insert.transportista.php?";
    Button savebtn ;
    int id;

    public void fillName(View view){
        nom.setText(recuperarNombreUsuario());
    }

    public void goToMenu(View view){
        Intent toMenu = new Intent(AgregarTransportista.this, Menu.class);
        startActivity(toMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_transportista);

        nom = findViewById(R.id.nombre);
        tel = findViewById(R.id.tel);
        correo = findViewById(R.id.correo);
        savebtn = findViewById(R.id.save_btn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom_str = nom.getText().toString();
                tel_str = tel.getText().toString();
                correo_str = correo.getText().toString();

                guardarTransportista();
            }
        });

    }

    private void guardarTransportista()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarTransportista.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();


        String encoded_nombre = URLEncoder.encode(nom_str);
        String encoded_tel = URLEncoder.encode(tel_str);
        String encoded_correo = URLEncoder.encode(correo_str);
        getId();
        String urlWithParams = url_add_transportista +"nombre=" + encoded_nombre + "&tel=" + encoded_tel + "&correo=" + encoded_correo + "&id="+id;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlWithParams, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                try{
                    JSONObject codeResponse = response.getJSONObject(0);

                    String code = codeResponse.getString("Codigo");

                    if(code.equals("0"))
                    {
                        Toast.makeText(AgregarTransportista.this, "Datos ingresados", Toast.LENGTH_LONG).show();
                        Intent toMenu = new Intent (AgregarTransportista.this, Menu.class);
                        startActivity(toMenu);
                    }else{
                        if(code.equals("06"))
                        {
                            Toast.makeText(AgregarTransportista.this, "Datos incompletos", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch(JSONException e) {
                    Toast.makeText(AgregarTransportista.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarTransportista.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    public String recuperarNombreUsuario()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);

        String nombreProveedor = userData.getString("nombreProveedor", "none");

        return nombreProveedor;
    }

    private void getId() {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = userData.getInt("id", 0);
    }

}
