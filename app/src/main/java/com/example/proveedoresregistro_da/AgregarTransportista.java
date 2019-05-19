package com.example.proveedoresregistro_da;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class AgregarTransportista extends AppCompatActivity {

    public void fillName(View view){
        EditText nombre = findViewById(R.id.nombre);
        nombre.setText(recuperarNombreUsuario());
    }

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

    public String recuperarNombreUsuario()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);

        String nombreProveedor = userData.getString("nombreProveedor", "none");

        return nombreProveedor;
    }
}
