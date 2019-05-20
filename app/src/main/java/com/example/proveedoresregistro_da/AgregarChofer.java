package com.example.proveedoresregistro_da;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgregarChofer extends AppCompatActivity {

    Button btnUpload;
    ImageButton btnChoose;
    ImageView imageUpload;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;
    String url = "http://ubiquitous.csf.itesm.mx/~pddm-1024595/content/proyecto/prueba_img/subir2.php";
    String url_add_chofer = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/insert.chofer.php?";
    ProgressDialog progressDialog;

    int id;
    String url_get_trans = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/transportistas.registrados.php?";
    ArrayList<ListItem> arr_trans;
    ArrayList<String> arr_trans_string;
    ArrayList<String> trans_id;
    private Spinner spinner;
    EditText nombre, apellido;
    String nombre_str, apellido_str, transportista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_chofer);


        arr_trans = new ArrayList<>();
        arr_trans_string = new ArrayList<>();
        trans_id = new ArrayList<>();
        btnChoose = findViewById(R.id.btn_ine);
        imageUpload = findViewById(R.id.imageView2);
        btnUpload = findViewById(R.id.guardar);
        nombre = findViewById(R.id.nom);
        apellido = findViewById(R.id.apellido);
        getId();
        recuperarTransportista();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AgregarChofer.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre_str = nombre.getText().toString();
                apellido_str = apellido.getText().toString();
                transportista = spinner.getSelectedItem().toString();
                transportista = trans_id.get(arr_trans_string.indexOf(transportista));
                //chooseImage();
                guardarDatos();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "select image"), CODE_GALLERY_REQUEST);
            }else
            {
                Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageUpload.setImageBitmap(bitmap);
                imageUpload.setVisibility(View.VISIBLE);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString (Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseImage()
    {
        progressDialog = new ProgressDialog(AgregarChofer.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String imageData = imageToString(bitmap);
                params.put("image", imageData);
                params.put("name", "nombre_imagen");

                return params;
            }

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

        RequestQueue requestQueue = Volley.newRequestQueue(AgregarChofer.this);
        requestQueue.add(stringRequest);
    }

    void recuperarTransportista()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarChofer.this);
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
                    Toast.makeText(AgregarChofer.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarChofer.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarChofer.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();
        String imageData = "http://ubiquitous.csf.itesm.mx/~pddm-1024595/content/proyecto/prueba_img/credencial-actual.jpg";

        url_add_chofer = url_add_chofer + "transportista="+
                transportista+ "&nombre=" + nombre_str + "&apellido=" + apellido_str+"&ine=" + imageData;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url_add_chofer, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarChofer.this, "Datos ingresados", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarChofer.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
