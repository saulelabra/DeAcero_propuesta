package com.example.proveedoresregistro_da;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Terminos extends AppCompatActivity {

    public void acceptTerms(View view) {
        Intent toMenu = new Intent(Terminos.this, Menu.class);
        startActivity(toMenu);
    }

    public void declineTerms(View view) {
        Intent toMain = new Intent(Terminos.this, Login.class);
        startActivity(toMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);
    }
}
