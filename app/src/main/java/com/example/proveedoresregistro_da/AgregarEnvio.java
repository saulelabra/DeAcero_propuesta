package com.example.proveedoresregistro_da;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarEnvio extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView seleccionar_fecha;
    int dia, mes, year, hora, minuto;
    Button add_material;
    TextView materiales, cantidades;
    Spinner lista_mats;
    EditText cantidad;
    ArrayList<Pair<String, String>> mats_cant = new ArrayList<>();

    public void guardarEnvioYReg(View view) {
        Intent goToMenu = new Intent(AgregarEnvio.this, Menu.class);
        startActivity(goToMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_envio);

        add_material = findViewById(R.id.btn_add_mat);
        materiales = findViewById(R.id.Text_Mat);
        lista_mats = findViewById(R.id.list_materiales);
        cantidad = findViewById(R.id.cantidad);
        cantidades = findViewById(R.id.Text_Cantidad);

        String[] arreglo_mats = new String[] {"Acero", "Aluminio"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arreglo_mats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        lista_mats.setAdapter(adapter);

        seleccionar_fecha = findViewById(R.id.seleccionar_fecha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void btn_ingresar_fecha (View view)
    {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarEnvio.this, AgregarEnvio.this, year, mes, dia);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AgregarEnvio.this, AgregarEnvio.this,
                hora, minuto, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        seleccionar_fecha.setText(year+"/"+mes+"/"+dia+" - "+hourOfDay+":"+minute);
    }

    public void btn_add_material (View v)
    {
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
        String mats = "";
        String cants = "";

        for (int i=0;i<mats_cant.size();i++)
        {
            mats += mats_cant.get(i).first + "\n";
            cants += mats_cant.get(i).second + "\n";
        }

        if( mats_cant.size() > 0 )
            mats_cant.remove( mats_cant.size() - 1 );

        materiales.setText("Materiales:\n"+mats);
        cantidades.setText("Cantidad:\n"+cants);
    }
}
