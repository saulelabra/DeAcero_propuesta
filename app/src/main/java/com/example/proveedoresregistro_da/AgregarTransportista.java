package com.example.proveedoresregistro_da;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AgregarTransportista extends AppCompatActivity {

    public void goToMenu(View view){
        Intent toMenu = new Intent(AgregarTransportista.this, Menu.class);
        startActivity(toMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_transportista);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
