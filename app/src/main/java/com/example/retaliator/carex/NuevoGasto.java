package com.example.retaliator.carex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevoGasto extends AppCompatActivity {
    // Creamos un objeto mantenimiento que es el que se devolverá con los datos del nuevo gasto
    Mantenimiento dlgMantenimiento = new Mantenimiento();
    ArrayList lista = new ArrayList<Coche>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Aqui hay que meter la lista de coches
        List<String> list = new ArrayList<String>();
        final Spinner spinner = (Spinner) findViewById(R.id.carSpinner);
        lista = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
        for (int i = 0; i < lista.size(); i++) {
            Coche c = (Coche)lista.get(i);
            list.add(c.getNombre());
        }
        dlgMantenimiento.setCoche(((Coche)lista.get(0)).getId_coche());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        // Se pone el icono correspondiente
        final ImageView iconCar = (ImageView) findViewById((R.id.iconCar));
        iconCar.setImageResource(((Coche)lista.get(0)).getIcono());
        iconCar.setColorFilter(((Coche)lista.get(0)).getColor());

        // Se pone la fecha actual
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        fecha.setText(dateFormat.format(date));

        // Se marca el ckeck que corresponda
        int tipo = getIntent().getExtras().getInt("tipoGasto");
        actualizarPantalla(tipo);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String texto = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                for (int i = 0; i < lista.size(); i++) {
                    if (texto.equals(((Coche)lista.get(i)).getNombre())){
                        dlgMantenimiento.setCoche(((Coche) lista.get(i)).getId_coche());
                        iconCar.setImageResource(((Coche)lista.get(i)).getIcono());
                        iconCar.setColorFilter(((Coche)lista.get(i)).getColor());
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_repostaje_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_guardar:
                // Acciones a realizar cuando se le da a guardar
                onBtnGuardar(null);
                return true;

            case android.R.id.home:
                // Acciones a realizar cuando se pulsa "flecha atrás"
                //onBackPressed();
                // La función onBackPressed() vuelve a la actividad anterior como
                // si se hubiese hecho un CANCEL, es decir vuelve al método onActivityResult
                // con código "RESULT_CANCELED"

                finish();
                // La función finish() vuelve a la actividad anterior como
                // si se hubiese hecho un CANCEL, es decir vuelve al método onActivityResult
                // con código "RESULT_CANCELED" pero además destruye esta actividad.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBtnGuardar(View view)
    {
        EditText edtFecha = (EditText) findViewById(R.id.edtFecha);

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date mydate = simpledateformat.parse(edtFecha.getText().toString(), pos);

        dlgMantenimiento.setFecha(mydate);

        EditText edtImporte = (EditText) findViewById(R.id.edtImporte);
        if (!edtImporte.getText().toString().isEmpty())
            dlgMantenimiento.setImporte(Float.parseFloat(edtImporte.getText().toString()));

        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);
        if (!edtKmTotales.getText().toString().isEmpty())
            dlgMantenimiento.setKmTotales(Integer.parseInt(edtKmTotales.getText().toString()));

        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        if (!edtLugar.getText().toString().isEmpty())
            dlgMantenimiento.setLugar(edtLugar.getText().toString());

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgMantenimiento);
        setResult(RESULT_OK, data);
        finish();
    }

    public void actualizarPantalla(int tipoGasto){
        TextView tvTitulo = (TextView) findViewById(R.id.tvTituloMantenimiento);
        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);
        TextView tvLugar = (TextView) findViewById(R.id.tvLugar);
        TextView tvKmTotales = (TextView) findViewById(R.id.tvKmTotales);

        // Check which checkbox was clicked
        switch(tipoGasto) {
            case R.id.mITV:
                tvTitulo.setText("ITV");
                //tvKmTotales.setVisibility(View.VISIBLE); // Mostrar el TV de Km
                //edtKmTotales.setVisibility(View.VISIBLE);// Mostrar el edit de Km
                edtKmTotales.setText("0");
                edtLugar.setText("");
                dlgMantenimiento.setTipo_gasto(getResources().getString(R.string.BD_ITV));
                break;
            case R.id.mSeguro:
                tvTitulo.setText("SEGURO");
                tvLugar.setText("Compañía"); // Cambiar el TV de Lugar por Compañía
                tvKmTotales.setVisibility(View.INVISIBLE); // Ocultar el TV de Km
                edtKmTotales.setVisibility(View.INVISIBLE); // Ocultar el edit de Km
                dlgMantenimiento.setTipo_gasto(getResources().getString(R.string.BD_Seguro));
                break;
            case R.id.mImpCir:
                tvTitulo.setText("IMP. CIRCULACION");
                //edtKmTotales.setText("0");
                //tvLugar.setText("Lugar");// Cambiar el TV de por Lugar
                tvKmTotales.setVisibility(View.INVISIBLE); // Ocultar el TV de Km
                edtKmTotales.setVisibility(View.INVISIBLE); // Ocultar el edit de Km
                dlgMantenimiento.setTipo_gasto(getResources().getString(R.string.BD_IVTM));
                break;
            default:
                break;
        }
    }
}
