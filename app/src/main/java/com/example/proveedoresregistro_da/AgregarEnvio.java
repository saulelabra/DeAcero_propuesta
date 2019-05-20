package com.example.proveedoresregistro_da;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.text.format.DateFormat;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ImageButton bol_sal_btn;
    ImageView bol_sal_img;
    Bitmap bol_sal_bit;
    final int CODE_GALLERY_REQUEST = 999;


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
        bol_sal_btn = findViewById(R.id.btn_bol);
        bol_sal_img = findViewById(R.id.image_bol);

        String[] arreglo_mats = new String[] {"Acero", "Aluminio"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arreglo_mats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        lista_mats.setAdapter(adapter);

        seleccionar_fecha = findViewById(R.id.seleccionar_fecha);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bol_sal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        AgregarEnvio.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "select image"), CODE_GALLERY_REQUEST);
            }else
            {
                Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri filepath = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bol_sal_bit = BitmapFactory.decodeStream(inputStream);
                bol_sal_img.setImageBitmap(bol_sal_bit);
                bol_sal_img.setVisibility(View.VISIBLE);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
