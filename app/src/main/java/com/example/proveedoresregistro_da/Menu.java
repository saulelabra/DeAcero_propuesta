package com.example.proveedoresregistro_da;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity {

    LinearLayout envios;
    Map<String, String> userData = new HashMap<String, String>();

    TextView proveedor_TextView, direccion_TextView;

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
        proveedor_TextView = findViewById(R.id.proveedor_tv);
        direccion_TextView = findViewById(R.id.direccion_tv);

        envios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEnvios = new Intent(Menu.this, EnviosProgramados.class);
                startActivity(toEnvios);
            }
        });

        userData = recuperarDatosUsuario();

        proveedor_TextView.setText(userData.get("nombreProveedor"));
        direccion_TextView.setText(userData.get("direccion"));
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
}
