package com.example.retaliator.carex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetalleGasto extends AppCompatActivity {
    // Creamos un objeto gasto (de tipo Mantenimiento) que es el que mostraremos en la pantalla
    Mantenimiento dlgGasto = new Mantenimiento();
    ArrayList lista = new ArrayList<Coche>();
    List<String> list = new ArrayList<String>();
    Spinner spinner;

    boolean modoEditar = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_gasto);

        if (getIntent() != null){
            // Se recoge la lista de coches
            lista = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
            for (int i = 0; i < lista.size(); i++) {
                Coche c = (Coche)lista.get(i);
                list.add(c.getNombre());
            }

            dlgGasto = (Mantenimiento)getIntent().getExtras().getSerializable("mantenimientoSeleccionado");
            // Se marca el ckeck que corresponda
            actualizarPantalla(dlgGasto.getTipo_gasto());

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
                        dlgGasto.setCoche(((Coche) lista.get(i)).getId_coche());
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

        // Asociar el menú contextual de borrar
        ImageView imagenPapelera = (ImageView) findViewById(R.id.imageTrash);
        registerForContextMenu(imagenPapelera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_editar_repostaje_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_guardar);
        item.setVisible(modoEditar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editar:
                // Habilitar edición.
                modoEditar = true;
                this.invalidateOptionsMenu();
                habilitarEdicion(true);
                return true;
            case R.id.menu_guardar:
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto_borrar, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mCancel:
                break;
            case R.id.mDelete:
                onBtnBorrar(null);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void onBtnBorrar(View view)
    {
        dlgGasto.setImporte(110011);
        dlgGasto.setKmTotales(0);
        dlgGasto.setLugar("Borrar Mantenimiento");

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgGasto);
        setResult(RESULT_OK, data);
        finish();
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

        if (!edtFecha.getText().toString().isEmpty())
            dlgGasto.setFecha(mydate);

        EditText edtImporte = (EditText) findViewById(R.id.edtImporte);
        if (!edtImporte.getText().toString().isEmpty())
            dlgGasto.setImporte(Float.parseFloat(edtImporte.getText().toString()));

        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);
        if (!edtKmTotales.getText().toString().isEmpty())
            dlgGasto.setKmTotales(Integer.parseInt(edtKmTotales.getText().toString()));

        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        if (!edtLugar.getText().toString().isEmpty())
            dlgGasto.setLugar(edtLugar.getText().toString());

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgGasto);
        setResult(RESULT_OK, data);
        finish();
    }

    public void actualizarPantalla(String  tipoGasto){
        TextView tvTitulo = (TextView) findViewById(R.id.tvTituloMantenimiento);
        spinner = (Spinner) findViewById(R.id.carSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        ImageView iconCar = (ImageView) findViewById((R.id.iconCar));
        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView tvLugar = (TextView) findViewById(R.id.tvLugar);
        TextView tvKmTotales = (TextView) findViewById(R.id.tvKmTotales);
        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);

        String nombreCoche = "";
        for (int i = 0; i < lista.size(); i++) {
            if (dlgGasto.getCoche() == ((Coche)lista.get(i)).getId_coche()){
                nombreCoche = ((Coche)lista.get(i)).getNombre();
                // Se pone el icono correspondiente
                iconCar.setImageResource(((Coche)lista.get(i)).getIcono());
                iconCar.setColorFilter(((Coche)lista.get(i)).getColor());
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (nombreCoche.equals(list.get(i)))
                spinner.setSelection(i);
        }

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(formatoFecha.format(dlgGasto.getFecha()));
        importe.setText(String.valueOf(dlgGasto.getImporte()));

        // Check which checkbox was clicked
        switch(tipoGasto) {
            case "ITV":
                tvTitulo.setText(R.string.BD_ITV);
                //tvKmTotales.setVisibility(View.VISIBLE); // Mostrar el TV de Km
                //edtKmTotales.setVisibility(View.VISIBLE);// Mostrar el edit de Km
                edtKmTotales.setText(String.valueOf(dlgGasto.getKmTotales()));
                //tvLugar.setText("Lugar");
                edtLugar.setText(dlgGasto.getLugar());
                break;
            case "Seguro":
                tvTitulo.setText(R.string.BD_Seguro);
                //edtKmTotales.setText("0"); // Este diría que sobra
                tvLugar.setText("Compañía"); // Cambiar el TV de Lugar por Compañía
                edtLugar.setText(dlgGasto.getLugar());
                tvKmTotales.setVisibility(View.INVISIBLE); // Ocultar el TV de Km
                edtKmTotales.setVisibility(View.INVISIBLE); // Ocultar el edit de Km
                break;
            case "Imp. Circulacion":
                tvTitulo.setText(R.string.BD_IVTM);
                //edtKmTotales.setText("0"); // Este diría que sobra
                //tvLugar.setText("Lugar");// Cambiar el TV de por Lugar
                edtLugar.setText(dlgGasto.getLugar());
                tvKmTotales.setVisibility(View.INVISIBLE); // Ocultar el TV de Km
                edtKmTotales.setVisibility(View.INVISIBLE); // Ocultar el edit de Km
                break;
            default:
                break;
        }
    }

    public void habilitarEdicion(boolean habilitar){

        Spinner spinner = (Spinner) findViewById(R.id.carSpinner);

        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView kmTotales  = (TextView) findViewById(R.id.edtKmTotales);
        TextView lugar  = (TextView) findViewById(R.id.edtLugar);

        ImageView btnImagenBorrar = (ImageView) findViewById(R.id.imageTrash);
        btnImagenBorrar.setEnabled(habilitar);
        if (habilitar)
            btnImagenBorrar.setColorFilter(Color.RED);
        else
            btnImagenBorrar.setColorFilter(Color.GRAY);

        spinner.setEnabled(habilitar);

        fecha.setEnabled(habilitar);
        importe.setEnabled(habilitar);
        kmTotales.setEnabled(habilitar);
        lugar.setEnabled(habilitar);

    }
}
