package com.example.proveedoresregistro_da;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AgregarEnvio extends AppCompatActivity {

    public void guardarEnvioYReg(View view) {
        Intent goToMenu = new Intent(AgregarEnvio.this, Menu.class);
        startActivity(goToMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_envio);
    }
}
