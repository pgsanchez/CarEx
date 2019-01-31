package com.example.retaliator.carex;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetalleRepostaje extends AppCompatActivity {

    // Creamos un objeto repostaje que es el que mostraremos en la pantalla
    Repostaje dlgRepostaje = new Repostaje();
    ArrayList lista = new ArrayList<Coche>();
    List<String> list = new ArrayList<String>();
    Spinner spinner;

    float importe = 0;
    float precio = 0;
    float litros = 0;
    boolean modoEditar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_repostaje);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null){
            // Se recoge la lista de coches
            lista = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
            for (int i = 0; i < lista.size(); i++) {
                Coche c = (Coche)lista.get(i);
                list.add(c.getNombre());
            }

            dlgRepostaje = (Repostaje)getIntent().getExtras().getSerializable("repostajeSeleccionado");
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
                        dlgRepostaje.setCoche(((Coche) lista.get(i)).getId_coche());
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

        /*final EditText edtImporte = (EditText) findViewById(R.id.edtImporte);
        edtImporte.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    dlgRepostaje.setImporte(Float.parseFloat(edtImporte.getText().toString()));
                    dlgRepostaje.setLitros(dlgRepostaje.getImporte()/dlgRepostaje.getPrecio());
                    actualizaPantalla();
                }
            }
        });*/

        // Escuchar cambios en el EditText de Importe
        final EditText edtImporte = (EditText)findViewById(R.id.edtImporte);
        edtImporte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText edtPrecio = (EditText) findViewById(R.id.edtPrecio);
                EditText edtLitros = (EditText) findViewById(R.id.edtLitros);

                // Si se ha detectado un cambio en Importe que viene del usuario:
                // Recogemos el nuevo valor que pone el usuario (puede ser un valor, o 0 si borra el valor del "editImporte")
                if (edtImporte.getText().toString().isEmpty())
                    importe = 0;
                else {
                    // Si se ha detectado un cambio en Importe, pero no ha sido por el usuario, entonces el "editImporte" tiene que tener el mismo valor que la variable "importe". Y en este caso, no hacemos nada.
                    if (Float.parseFloat(edtImporte.getText().toString()) == importe)
                        return;
                    else
                        importe = Float.parseFloat(edtImporte.getText().toString());
                }

                if (!edtPrecio.getText().toString().isEmpty())
                    precio = Float.parseFloat(edtPrecio.getText().toString());
                if (!edtLitros.getText().toString().isEmpty())
                    litros = Float.parseFloat(edtLitros.getText().toString());


                if (litros == 0)
                    precio = 0;
                else {
                    precio = importe / litros;
                    precio = (float) formatearDecimales(precio, 3);
                }
                edtPrecio.setText(String.valueOf(precio));
            }
        });

        // Escuchar cambios en el EditText de Precio
        final EditText edtPrecio = (EditText) findViewById(R.id.edtPrecio);
        edtPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText edtImporte = (EditText)findViewById(R.id.edtImporte);
                EditText edtLitros = (EditText) findViewById(R.id.edtLitros);

                // Si se ha detectado un cambio en Precio que viene del usuario:
                // Recogemos el nuevo valor que pone el usuario (puede ser un valor, o 0 si borra el valor del "edtPrecio")
                if (edtPrecio.getText().toString().isEmpty())
                    precio = 0;
                else{
                    // Si se ha detectado un cambio en Precio, pero no ha sido por el usuario, entonces el "edtPrecio" tiene que tener el mismo valor que la variable "precio". Y en este caso, no hacemos nada.
                    if (Float.parseFloat(edtPrecio.getText().toString()) == precio)
                        return;
                    else
                        precio = Float.parseFloat(edtPrecio.getText().toString());
                }


                if (!edtImporte.getText().toString().isEmpty())
                    importe = Float.parseFloat(edtImporte.getText().toString());
                if (!edtLitros.getText().toString().isEmpty())
                    litros = Float.parseFloat(edtLitros.getText().toString());

                if (precio == 0)
                    litros = 0;
                else {
                    litros = importe / precio;
                    litros = (float) formatearDecimales(litros, 2);
                }
                edtLitros.setText(String.valueOf(litros));
            }
        });

        // Escuchar cambios en el EditText de Litros
        final EditText edtLitros = (EditText) findViewById(R.id.edtLitros);
        edtLitros.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText edtImporte = (EditText)findViewById(R.id.edtImporte);
                EditText edtPrecio = (EditText) findViewById(R.id.edtPrecio);



                // Si se ha detectado un cambio en Litros que viene del usuario:
                // Recogemos el nuevo valor que pone el usuario (puede ser un valor, o 0 si borra el valor del "edtLitros")
                if (edtLitros.getText().toString().isEmpty())
                    litros = 0;
                else {
                    // Si se ha detectado un cambio en Litros, pero no ha sido por el usuario, entonces el "edtLitros" tiene que tener el mismo valor que la variable "litros". Y en este caso, no hacemos nada.
                    if (Float.parseFloat(edtLitros.getText().toString()) == litros)
                        return;
                    else
                        litros = Float.parseFloat(edtLitros.getText().toString());
                }

                if (!edtImporte.getText().toString().isEmpty())
                    importe = Float.parseFloat(edtImporte.getText().toString());
                if (!edtPrecio.getText().toString().isEmpty())
                    precio = Float.parseFloat(edtPrecio.getText().toString());


                importe = precio*litros;
                importe = (float)formatearDecimales(importe, 2);
                edtImporte.setText(String.valueOf(importe));
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

    public void actualizaPantalla()
    {
        spinner = (Spinner) findViewById(R.id.carSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        ImageView iconCar = (ImageView) findViewById((R.id.iconCar));
        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView precio  = (TextView) findViewById(R.id.edtPrecio);
        TextView litros  = (TextView) findViewById(R.id.edtLitros);
        TextView kmTotales  = (TextView) findViewById(R.id.edtKmTotales);
        TextView kmParciales  = (TextView) findViewById(R.id.edtKmParciales);
        TextView lugar  = (TextView) findViewById(R.id.edtLugar);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(formatoFecha.format(dlgRepostaje.getFecha()));

        String nombreCoche = "";
        for (int i = 0; i < lista.size(); i++) {
            if (dlgRepostaje.getCoche() == ((Coche)lista.get(i)).getId_coche()){
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

        importe.setText(String.valueOf(dlgRepostaje.getImporte()));
        precio.setText(String.valueOf(dlgRepostaje.getPrecio()));
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        litros.setText(String.valueOf(dlgRepostaje.getLitros()));
        //Float x = Float.valueOf(twoDForm.format(dlgRepostaje.getLitros()));
        //litros.setText(String.valueOf(x));
        kmTotales.setText(String.valueOf(dlgRepostaje.getKmTotales()));
        kmParciales.setText(String.valueOf((dlgRepostaje.getKmParciales())));
        lugar.setText(String.valueOf(dlgRepostaje.getLugar()));

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
            dlgRepostaje.setFecha(mydate);


        EditText edtImporte = (EditText) findViewById(R.id.edtImporte);
        if (!edtImporte.getText().toString().isEmpty())
            dlgRepostaje.setImporte(Float.parseFloat(edtImporte.getText().toString()));

        EditText edtPrecio = (EditText) findViewById(R.id.edtPrecio);
        dlgRepostaje.setPrecio(Float.parseFloat(edtPrecio.getText().toString()));

        EditText edtLitros = (EditText) findViewById(R.id.edtLitros);
        dlgRepostaje.setLitros(Float.parseFloat(edtLitros.getText().toString()));

        EditText edtKmTotales = (EditText) findViewById(R.id.edtKmTotales);
        dlgRepostaje.setKmTotales(Integer.parseInt(edtKmTotales.getText().toString()));

        EditText edtKmParciales = (EditText) findViewById(R.id.edtKmParciales);
        if (!edtKmParciales.getText().toString().isEmpty())
            dlgRepostaje.setKmParciales(Integer.parseInt(edtKmParciales.getText().toString()));

        EditText edtLugar = (EditText) findViewById(R.id.edtLugar);
        dlgRepostaje.setLugar(edtLugar.getText().toString());

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgRepostaje);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onBtnBorrar(View view)
    {
        dlgRepostaje.setImporte(110011);
        dlgRepostaje.setKmTotales(0);
        dlgRepostaje.setLugar("Borrar Repostaje");

        // Cerrar la Activity y devolver el objeto Repostaje con los datos
        Intent data = new Intent();
        data.putExtra("parametro",dlgRepostaje);
        setResult(RESULT_OK, data);
        finish();
    }

    public void habilitarEdicion(boolean habilitar){

        TextView fecha  = (TextView) findViewById(R.id.edtFecha);
        TextView importe  = (TextView) findViewById(R.id.edtImporte);
        TextView precio  = (TextView) findViewById(R.id.edtPrecio);
        TextView litros  = (TextView) findViewById(R.id.edtLitros);
        TextView kmTotales  = (TextView) findViewById(R.id.edtKmTotales);
        TextView kmParciales  = (TextView) findViewById(R.id.edtKmParciales);
        TextView lugar  = (TextView) findViewById(R.id.edtLugar);

        Spinner spinner = (Spinner) findViewById(R.id.carSpinner);

        ImageView btnImagenBorrar = (ImageView) findViewById(R.id.imageTrash);
        btnImagenBorrar.setEnabled(habilitar);
        if (habilitar == true)
            btnImagenBorrar.setColorFilter(Color.RED);
        else
            btnImagenBorrar.setColorFilter(Color.GRAY);


        fecha.setEnabled(habilitar);
        importe.setEnabled(habilitar);
        precio.setEnabled(habilitar);
        litros.setEnabled(habilitar);
        kmTotales.setEnabled(habilitar);
        kmParciales.setEnabled(habilitar);
        lugar.setEnabled(habilitar);
        spinner.setEnabled(habilitar);
    }

    public static double formatearDecimales(float numero, Integer numeroDecimales) {

        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }
}
