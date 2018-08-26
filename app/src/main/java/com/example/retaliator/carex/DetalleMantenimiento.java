package com.example.retaliator.carex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class DetalleMantenimiento extends AppCompatActivity {

    // Creamos un objeto repostaje que es el que mostraremos en la pantalla
    Mantenimiento dlgMantenimiento = new Mantenimiento();
    ArrayList lista = new ArrayList<Coche>();
    List<String> list = new ArrayList<String>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mantenimiento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null){
            // Se recoge la lista de coches
            lista = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
            for (int i = 0; i < lista.size(); i++) {
                Coche c = (Coche)lista.get(i);
                list.add(c.getNombre());
            }

            dlgMantenimiento = (Mantenimiento)getIntent().getExtras().getSerializable("mantenimientoSeleccionado");
            actualizaPantalla();
            // Inhabilitar edición. Solo se puede editar si se pulsa el botón de editar.
            habilitarEdicion(false);
        }

        // Se declara el icono para poderlo modificar al cambiar el spinner
        final ImageView iconCar = (ImageView) findViewById((R.id.iconCar));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String texto = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                for (int i = 0; i < lista.size(); i++) {
                    if (texto == ((Coche)lista.get(i)).getNombre()){
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
        getMenuInflater().inflate(R.menu.activity_editar_repostaje_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar:
                // Habilitar edición.
                habilitarEdicion(true);
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

    public void actualizaPantalla()
    {
        spinner = (Spinner) findViewById(R.id.carSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        ImageView iconCar = (ImageView) findViewById((R.id.iconCar));
        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView kmTotales  = (TextView) findViewById(R.id.edtKmTotales);
        TextView kmParciales  = (TextView) findViewById(R.id.edtKmParciales);
        TextView lugar  = (TextView) findViewById(R.id.edtLugar);
        TextView taller = (TextView) findViewById(R.id.edtTaller);
        TextView descripcion = (TextView) findViewById(R.id.edtDescripcion);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(formatoFecha.format(dlgMantenimiento.getFecha()));

        String nombreCoche = "";
        for (int i = 0; i < lista.size(); i++) {
            if (dlgMantenimiento.getCoche() == ((Coche)lista.get(i)).getId_coche()){
                nombreCoche = ((Coche)lista.get(i)).getNombre();
                // Se pone el icono correspondiente
                iconCar.setImageResource(((Coche)lista.get(i)).getIcono());
                iconCar.setColorFilter(((Coche)lista.get(i)).getColor());
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (nombreCoche == list.get(i))
                spinner.setSelection(i);
        }

        importe.setText(String.valueOf(dlgMantenimiento.getImporte()));
        kmTotales.setText(String.valueOf(dlgMantenimiento.getKmTotales()));
        //kmParciales.setText(String.valueOf(dlgMantenimiento.getKmParciales()));
        lugar.setText(String.valueOf(dlgMantenimiento.getLugar()));
        taller.setText(String.valueOf(dlgMantenimiento.getTaller()));
        descripcion.setText(String.valueOf(dlgMantenimiento.getReparacion()));
    }

    public void onBtnGuardar(View view)
    {
        EditText edtFecha = (EditText) findViewById(R.id.edtFecha);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        Date mydate = new Date();
        try {
            mydate = formatoFecha.parse(edtFecha.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dlgMantenimiento.setFecha(mydate);

        EditText edtImporte = (EditText) findViewById(R.id.edtImporte);
        dlgMantenimiento.setImporte(Float.parseFloat(edtImporte.getText().toString()));

        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);
        dlgMantenimiento.setKmTotales(Integer.parseInt(edtKmTotales.getText().toString()));

        EditText edtKmParciales = (EditText) findViewById(R.id.edtKmParciales);
        dlgMantenimiento.setKmParciales(Integer.parseInt(edtKmParciales.getText().toString()));

        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        dlgMantenimiento.setLugar(edtLugar.getText().toString());

        EditText edtTaller = (EditText) findViewById(R.id.edtTaller);
        dlgMantenimiento.setTaller(edtTaller.getText().toString());

        EditText edtReparacion = (EditText) findViewById(R.id.edtDescripcion);
        dlgMantenimiento.setReparacion(edtReparacion.getText().toString());

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgMantenimiento);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onBtnBorrar(View view)
    {
        dlgMantenimiento.setImporte(110011);
        dlgMantenimiento.setKmTotales(0);
        dlgMantenimiento.setLugar("Borrar Mantenimiento");

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgMantenimiento);
        setResult(RESULT_OK, data);
        finish();
    }

    public void habilitarEdicion(boolean habilitar){

        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView kmTotales  = (TextView) findViewById(R.id.edtKmTotales);
        TextView kmParciales  = (TextView) findViewById(R.id.edtKmParciales);
        TextView lugar  = (TextView) findViewById(R.id.edtLugar);
        TextView taller = (TextView) findViewById(R.id.edtTaller);
        TextView descripcion = (TextView) findViewById(R.id.edtDescripcion);

        Button btnBorrar = (Button) findViewById(R.id.tmpBtnDelete);
        Button btnGuardar = (Button) findViewById(R.id.tmpBtnSave);
        Spinner spinner = (Spinner) findViewById(R.id.carSpinner);

        fecha.setEnabled(habilitar);
        importe.setEnabled(habilitar);
        kmTotales.setEnabled(habilitar);
        kmParciales.setEnabled(habilitar);
        lugar.setEnabled(habilitar);
        taller.setEnabled(habilitar);
        descripcion.setEnabled(habilitar);
        spinner.setEnabled(habilitar);
        btnBorrar.setEnabled(habilitar);
        btnGuardar.setEnabled(habilitar);
    }
}
