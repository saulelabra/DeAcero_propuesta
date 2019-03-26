package com.example.proveedoresregistro_da;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
}
