package com.example.proveedoresregistro_da;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Menu extends AppCompatActivity {

    LinearLayout envios;

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

        envios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toEnvios = new Intent(Menu.this, EnviosProgramados.class);
                startActivity(toEnvios);
            }
        });
    }
}
