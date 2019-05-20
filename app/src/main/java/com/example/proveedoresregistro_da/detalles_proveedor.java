package com.example.proveedoresregistro_da;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class detalles_proveedor extends AppCompatActivity {

    TextView nombreProveedor, rfc, num_cuenta, direccion;
    Map<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_proveedor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombreProveedor = findViewById(R.id.nombreProveedor);
        rfc = findViewById(R.id.rfc);
        num_cuenta = findViewById(R.id.num_cuenta);
        direccion = findViewById(R.id.direccion);

        userData = recuperarDatosUsuario();

        String textoRFC = "RFC: " + userData.get("rfc");
        String textoCuenta = "Cuenta de deposito: " + userData.get("cuenta_a_depositar");
        String textoDireccion = "Dirección: " + userData.get("direccion");

        nombreProveedor.setText(userData.get("nombreProveedor"));
        rfc.setText(textoRFC);
        num_cuenta.setText(textoCuenta);
        direccion.setText(textoDireccion);
    }

    public Map<String, String> recuperarDatosUsuario()
    {
        SharedPreferences userData = getSharedPreferences("userData", Context.MODE_PRIVATE);
        Map<String, String> userDataMap = new HashMap<String, String>();

        userDataMap.put("usuario", userData.getString("usuario", "none"));
        userDataMap.put("password", userData.getString("password", "none"));
        userDataMap.put("nombreProveedor", userData.getString("nombreProveedor", "none"));
        userDataMap.put("direccion", userData.getString("direccion", "none"));
        userDataMap.put("rfc", userData.getString("rfc", "none"));
        userDataMap.put("cuenta_a_depositar", Integer.toString(userData.getInt("cuenta_a_depositar", 0)));

        return userDataMap;
    }
}
