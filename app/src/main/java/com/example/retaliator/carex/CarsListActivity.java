package com.example.retaliator.carex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CarsListActivity extends AppCompatActivity {

    // Defines para mensajes entre Activities
    private static final int NUEVO_COCHE = 301;
    private static final int EDITAR_COCHE = 302;

    private ListView cochesListView;
    CocheAdapter cocheAdapter;
    ArrayList lista = new ArrayList<Coche>();

    Coche cocheSeleccionado = new Coche();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Intent intent = new Intent(this, NuevoCoche.class);
                Intent intent;
                intent = new Intent(getApplicationContext(), NuevoCoche.class);
                startActivityForResult(intent, NUEVO_COCHE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.cochesListView = (ListView) findViewById(R.id.listCars);

        if (getIntent() != null)
        {
            lista = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
            Log.i("CarsListActivity = ", String.valueOf(lista.size()));
            cocheAdapter = new CocheAdapter(this, lista);
            cochesListView.setAdapter(cocheAdapter);
        }


        // Responder a un click en la lista de coches
        cochesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                abrirDetalleCoche(view, position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to:
        Coche objCoche;// = new Coche();
        if (requestCode == NUEVO_COCHE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Hay que recoger el objeto que vendrá en el Intent y cargarlo en la BD

                objCoche = (Coche) data.getExtras().getSerializable("parametro");
                CarExHelper baseDatos = new CarExHelper(getApplicationContext());
                TablaCochesDB tablaCochesDB = new TablaCochesDB(baseDatos);
                tablaCochesDB.insertarCocheDB(objCoche);
                // Re-leemos la BD y actualizamos la lista de coches
                lista = tablaCochesDB.leerCochesBD();
                cocheAdapter = new CocheAdapter(this, lista);
                cochesListView.setAdapter(cocheAdapter);
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.

            }
        } else if (requestCode == EDITAR_COCHE){
            if (resultCode == RESULT_OK) {
                // Hay que recoger el objeto que vendrá en el Intent y MODIFICARLO en la BD
                objCoche = (Coche)data.getExtras().getSerializable("cocheModificado");
                CarExHelper baseDatos = new CarExHelper(getApplicationContext());
                TablaCochesDB tablaCochesDB = new TablaCochesDB(baseDatos);
                tablaCochesDB.actualizarCocheDB(objCoche, cocheSeleccionado);
                // Re-leemos la BD y actualizamos la lista de coches
                lista = tablaCochesDB.leerCochesBD();
                cocheAdapter = new CocheAdapter(this, lista);
                cochesListView.setAdapter(cocheAdapter);
            } else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.

            }
        }
    }

    public void abrirDetalleCoche(View view, int pos) {
        // Abrir la ventana de Detalle del coche, con los datos del coche que se
        // ha seleccionado
        cocheSeleccionado = (Coche)lista.get(pos);
        Log.i("Coche seleccionado = ", String.valueOf(pos));
        Log.i("Coche seleccionado = ", cocheSeleccionado.getNombre());
        Intent intent;
        intent = new Intent(this, DetalleCoche.class);
        intent.putExtra("cocheEditable",cocheSeleccionado);
        startActivityForResult(intent, EDITAR_COCHE);
    }

}
