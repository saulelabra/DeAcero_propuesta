package com.example.proveedoresregistro_da;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.text.format.DateFormat;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AgregarEnvio extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    int id;
    String url_get_trans = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/transportistas.registrados.php?";
    String url_get_choferes = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/choferes.registrados.php?";
    String url_get_camion = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/camiones.registrados.php?";
    String url_get_contenedor = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/contenedores.registrados.php?";
    String url_get_patio = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/patios.lista.php?";
    String url_add_envio = "http://ubiquitous.csf.itesm.mx/~pddm-1020725/content/DeAcero_API/queries/insert.envio.php?";
    ArrayList<String> arr_trans_string;
    ArrayList<String> arr_chof_nom;
    ArrayList<String> chofer_apellido;
    ArrayList<String> arr_camion;
    ArrayList<String> arr_cont;
    ArrayList<String> arr_patio;

    ArrayList<String> arr_trans_id;
    ArrayList<String> arr_chof_id;
    ArrayList<String> arr_camion_id;
    ArrayList<String> arr_cont_id;
    ArrayList<String> arr_patio_id;


    private Spinner spinner_t, spinner_c, spinner_cam, spinner_cont, spinner_patio;
    TextView seleccionar_fecha;
    int dia_f, mes_f, year_f, hora_f, minuto_f;
    Button add_material;
    TextView materiales, cantidades;
    Spinner lista_mats;
    EditText cantidad;
    ArrayList<Pair<String, String>> mats_cant = new ArrayList<>();
    ImageButton bol_sal_btn;
    ImageView bol_sal_img;
    Bitmap bol_sal_bit;
    EditText edit_com, edit_or;
    Button guardar;
    final int CODE_GALLERY_REQUEST = 999;
    String camion_str, chofer_str, transportista_str, contenedor_str, fecha_envio_str, dir_origen_str, comentario_str, patio_dest;


    public void guardarEnvioYReg(View view) {
        Intent goToMenu = new Intent(AgregarEnvio.this, Menu.class);
        startActivity(goToMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_envio);

        arr_trans_string = new ArrayList<>();
        chofer_apellido = new ArrayList<>();
        arr_chof_nom = new ArrayList<>();
        arr_camion = new ArrayList<>();
        arr_cont = new ArrayList<>();
        arr_patio = new ArrayList<>();

        arr_trans_id = new ArrayList<>();
        arr_chof_id = new ArrayList<>();
        arr_camion_id = new ArrayList<>();
        arr_cont_id = new ArrayList<>();
        arr_patio_id = new ArrayList<>();

        getId();
        recuperarTransportista();
        recuperarChofer();
        recuperarCamion();
        recuperarContenedor();
        recuperarPatio();

        add_material = findViewById(R.id.btn_add_mat);
        materiales = findViewById(R.id.Text_Mat);
        lista_mats = findViewById(R.id.list_materiales);
        cantidad = findViewById(R.id.cantidad);
        cantidades = findViewById(R.id.Text_Cantidad);
        bol_sal_btn = findViewById(R.id.btn_bol);
        bol_sal_img = findViewById(R.id.image_bol);
        edit_com = findViewById(R.id.editText_com);
        edit_or = findViewById(R.id.editText_or);
        guardar = findViewById(R.id.save_btn);

        String[] arreglo_mats = new String[] {"Acero", "Aluminio", "Cobre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arreglo_mats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        lista_mats.setAdapter(adapter);

        seleccionar_fecha = findViewById(R.id.seleccionar_fecha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camion_str = arr_camion_id.get(arr_camion.indexOf(spinner_cam.getSelectedItem().toString()));
                chofer_str = arr_chof_id.get(arr_chof_nom.indexOf(spinner_c.getSelectedItem().toString()));
                transportista_str = arr_trans_id.get(arr_trans_string.indexOf(spinner_t.getSelectedItem().toString()));
                contenedor_str = arr_cont_id.get(arr_cont.indexOf(spinner_cont.getSelectedItem().toString()));
                patio_dest = spinner_patio.getSelectedItem().toString();
                dir_origen_str = edit_or.getText().toString();
                comentario_str = edit_com.getText().toString();
                //Toast.makeText(AgregarEnvio.this, "hola: "+id, Toast.LENGTH_LONG).show();
                Log.d("Values", "Camion: "+camion_str+
                                "\nChofer: "+chofer_str+
                                "\npatio_dest: "+patio_dest+
                                "\nDir_origen: "+dir_origen_str+
                                "\ntransportista: "+transportista_str+
                                "\nComentarios: "+comentario_str+
                                "\nContenedor: "+contenedor_str+
                                "\nFecha: "+fecha_envio_str
                        );
                guardarEnvio();
            }
        });

        bol_sal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AgregarEnvio.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
    }

    public void btn_ingresar_fecha (View view)
    {
        Calendar c = Calendar.getInstance();
        year_f = c.get(Calendar.YEAR);
        mes_f = c.get(Calendar.MONTH);
        dia_f = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarEnvio.this, AgregarEnvio.this, year_f, mes_f, dia_f);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        hora_f = c.get(Calendar.HOUR_OF_DAY);
        minuto_f = c.get(Calendar.MINUTE);

        year_f = year;
        mes_f = month;
        dia_f = dayOfMonth;

        TimePickerDialog timePickerDialog = new TimePickerDialog(AgregarEnvio.this, AgregarEnvio.this,
                hora_f, minuto_f, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setSQLDate(hourOfDay, minute);

    }

    public void btn_add_material (View v)
    {
        //Toast.makeText(AgregarEnvio.this, "Material añadido", Toast.LENGTH_SHORT).show();
        String mats = "";
        String cants = "";
        mats_cant.add(new Pair<String, String>(lista_mats.getSelectedItem().toString(), cantidad.getText().toString()));
        for (int i=0;i<mats_cant.size();i++)
        {
            mats += mats_cant.get(i).first + "\n";
            cants += mats_cant.get(i).second + "\n";
        }
        materiales.setText("Materiales:\n"+mats);
        cantidades.setText("Cantidad:\n"+cants);

    }

    public void btn_del_material(View v)
    {
        //Toast.makeText(AgregarEnvio.this, "Material eliminado", Toast.LENGTH_SHORT).show();
        String mats = "";
        String cants = "";

        if( mats_cant.size() > 0 )
            mats_cant.remove( mats_cant.size() - 1 );

        for (int i=0;i<mats_cant.size();i++)
        {
            mats += mats_cant.get(i).first + "\n";
            cants += mats_cant.get(i).second + "\n";
        }

        materiales.setText("Materiales:\n"+mats);
        cantidades.setText("Cantidad:\n"+cants);
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
                bol_sal_bit = BitmapFactory.decodeStream(inputStream);
                bol_sal_img.setImageBitmap(bol_sal_bit);
                bol_sal_img.setVisibility(View.VISIBLE);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    void recuperarTransportista()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
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
                        arr_trans_id.add(json_obj.getString("id"));
                    }

                    fillSpinner();

                } catch (JSONException e) {
                    Toast.makeText(AgregarEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    void recuperarChofer()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_get_choferes = url_get_choferes + "usuario=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_get_choferes, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray json_arr_choferes = new JSONArray();

                    json_arr_choferes = response.getJSONArray("Choferes_registrados");

                    for(int i=0; i<json_arr_choferes.length(); i++)
                    {
                        JSONObject json_obj = json_arr_choferes.getJSONObject(i);

                        String nombreyapellido = json_obj.getString("nombre") + " " + json_obj.getString("apellido");

                        arr_chof_nom.add(nombreyapellido);
                        arr_chof_id.add(json_obj.getString("id"));
                    }

                    fillSpinnerChof();

                } catch (JSONException e) {
                    Toast.makeText(AgregarEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    void recuperarCamion()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_get_camion = url_get_camion + "usuario=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_get_camion, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray json_arr_trans = new JSONArray();

                    json_arr_trans = response.getJSONArray("Camiones_registrados");

                    for(int i=0; i<json_arr_trans.length(); i++)
                    {
                        JSONObject json_obj = json_arr_trans.getJSONObject(i);

                        String camionyplacas = json_obj.getString("marca") + " " + json_obj.getString("modelo") + " " + json_obj.getString("placas");

                        arr_camion.add(camionyplacas);
                        arr_camion_id.add(json_obj.getString("id"));
                    }

                    fillSpinnerCamion();

                } catch (JSONException e) {
                    Toast.makeText(AgregarEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    void recuperarContenedor()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_get_contenedor = url_get_contenedor + "usuario=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_get_contenedor, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray json_arr_trans = new JSONArray();

                    json_arr_trans = response.getJSONArray("Contenedores_registrados");

                    for(int i=0; i<json_arr_trans.length(); i++)
                    {
                        JSONObject json_obj = json_arr_trans.getJSONObject(i);
                        arr_cont.add(json_obj.getString("placas"));
                        arr_cont_id.add(json_obj.getString("id"));
                    }

                    fillSpinnerContenedor();

                } catch (JSONException e) {
                    Toast.makeText(AgregarEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    void recuperarPatio()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();

        url_get_patio = url_get_patio + "usuario=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_get_patio, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                barraDeProgreso.hide();
                try {
                    JSONArray json_arr_trans = new JSONArray();

                    json_arr_trans = response.getJSONArray("Patios");

                    for(int i=0; i<json_arr_trans.length(); i++)
                    {
                        JSONObject json_obj = json_arr_trans.getJSONObject(i);
                        arr_patio.add(json_obj.getString("nombre"));
                        //arr_patio_id.add(json_obj.getString("id"));
                    }

                    fillSpinnerPatio();

                } catch (JSONException e) {
                    Toast.makeText(AgregarEnvio.this, "Problema en: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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
        spinner_t = findViewById(R.id.spinner_trans);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_trans_string);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_t.setAdapter(adapter);
    }

    private void fillSpinnerChof() {
        //Llenando spinner
        spinner_c = findViewById(R.id.spinner_c);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_chof_nom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_c.setAdapter(adapter);
    }

    private void fillSpinnerCamion() {
        //Llenando spinner
        spinner_cam = findViewById(R.id.spinner_cam);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_camion);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cam.setAdapter(adapter);
    }

    private void fillSpinnerContenedor() {
        //Llenando spinner
        spinner_cont = findViewById(R.id.spinner_cont);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_cont);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cont.setAdapter(adapter);
    }

    private void fillSpinnerPatio() {
        //Llenando spinner
        spinner_patio = findViewById(R.id.spinner_patio);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr_patio);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_patio.setAdapter(adapter);
    }

    private void guardarEnvio()
    {
        final ProgressDialog barraDeProgreso = new ProgressDialog(AgregarEnvio.this);
        barraDeProgreso.setMessage("Cargando");
        barraDeProgreso.show();
        String imageData = "http://ubiquitous.csf.itesm.mx/~pddm-1024595/content/proyecto/prueba_img/boleta_salida.jpg";
        //http://ubiquitous.csf.itesm.mx/~pddm-1024595/content/proyecto/prueba_img/boleta_salida.jpg

        String urlWithParams = url_add_envio +
                "id_camion="+ camion_str +
                "&id_chofer=" + chofer_str +
                "&fecha_reg=2019-05-21" +
                "&patio_dest=" + patio_dest +
                "&dir_origen="+ dir_origen_str +
                "&url_boleta="+ imageData +
                "&fecha_llegada="+fecha_envio_str +
                "&estado=-1" +
                "&id_policia=0" +
                "&comentario=" + comentario_str +
                "&id_usuario="+ id +
                "&comentarios_check=-1" +
                "&id_transportista="+ transportista_str +
                "&id_contenedor="+ contenedor_str +
                "&cantidad=10" +
                "&id_material=6";

        Log.d("Envio", urlWithParams);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlWithParams, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Datos ingresados ", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barraDeProgreso.hide();
                Toast.makeText(AgregarEnvio.this, "Error en: " + error.toString(), Toast.LENGTH_LONG).show();
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

    private void setSQLDate(int hourOfDay, int minute)
    {
        String hora = String.valueOf(hourOfDay), minuto = String.valueOf(minute), mes = String.valueOf(mes_f), dia = String.valueOf(dia_f);
        if (hourOfDay < 10)
            hora = "0"+hourOfDay;
        if (minute < 10)
            minuto = "0"+minute;
        if (mes_f < 10)
            mes = "0"+mes_f;
        if (dia_f < 10)
            dia = "0"+dia_f;
        seleccionar_fecha.setText(year_f+"/"+mes+"/"+dia+" - "+hora+":"+minuto);
        fecha_envio_str = year_f+"-"+mes+"-"+dia+" "+hora+":"+minuto+":00";
    }
}
