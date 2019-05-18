package com.example.proveedoresregistro_da;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class detalles_proveedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_proveedor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
