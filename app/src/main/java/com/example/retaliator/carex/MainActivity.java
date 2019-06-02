package com.example.retaliator.carex;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.opencsv.CSVWriter;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static MainActivity myContext;

    CarExHelper baseDatos;
    // Defines para mensajes entre Activities
    private static final int NUEVO_REPOSTAJE = 101;
    private static final int EDITAR_REPOSTAJE = 102;
    private static final int NUEVO_MANTENIMIENTO = 103;
    private static final int EDITAR_MANTENIMIENTO = 104;
    private static final int NUEVO_GASTO = 105;
    private static final int EDITAR_GASTO = 106;


    private ListView repostajesListView; // Actividades de la lista de repostajes
    RepostajeAdapter repostajeAdapter;
    private ListView mantenimientoListView; // Actividades de la lista de mantenimientos
    MantenimientoAdapter mantenimientoAdapter;

    private List<Repostaje> listaRepostajes = new ArrayList<Repostaje>(); // Lista de Respostajes
    private List<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>(); // Lista de Mantenimientos
    private ArrayList<Coche> listaCoches = new ArrayList<Coche>(); // Lista de Coches

    Repostaje repostajeSeleccionado = new Repostaje();
    Mantenimiento mantenimientoSeleccionado = new Mantenimiento();

    TabHost tabs;

    TablaCochesDB tablaCochesDB;
    TablaRepostajeDB tablaRepostajeDB;
    TablaMantenimientoDB tablaMantenimientoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext =  this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaOperacion(view);
            }
        });

        this.repostajesListView = (ListView) findViewById(R.id.listrep);
        this.mantenimientoListView = (ListView) findViewById(R.id.listmant);

        // Crear los dos Tabs, ponerles texto y seleccionar por defecto el de Repostajes
        tabs = (TabHost)findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.texto_tab1));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.texto_tab2));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        //******************************************************************************************
        //*************** Se crea la BD y se leen las tablas para rellenar las listas **************
        baseDatos = new CarExHelper(getApplicationContext());

        tablaCochesDB = new TablaCochesDB(baseDatos);
        listaCoches = tablaCochesDB.leerCochesBD();

        tablaRepostajeDB = new TablaRepostajeDB(baseDatos);
        listaRepostajes = tablaRepostajeDB.leerRepostajesBD();

        tablaMantenimientoDB = new TablaMantenimientoDB(baseDatos);
        listaMantenimientos = tablaMantenimientoDB.leerMantenimientosBD();
        //******************************************************************************************

        // Mostrar la lista principal (por defecto será la lista de Repostajes)
        repostajeAdapter = new RepostajeAdapter(this, listaRepostajes, listaCoches);
        this.repostajesListView.setAdapter(repostajeAdapter);

        mantenimientoAdapter = new MantenimientoAdapter(this, listaMantenimientos, listaCoches);
        this.mantenimientoListView.setAdapter(mantenimientoAdapter);

        // Responder a un click en la lista principal de REPOSTAJES
        // Eventos
        repostajesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                //Lead currentLead = mLeadsAdapter.getItem(position);
                abrirDetalleRepostaje(view, position);
            }
        });
        // Responder a un click en la lista principal de MANTENIMIENTOS
        mantenimientoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                //Lead currentLead = mLeadsAdapter.getItem(position);
                abrirDetalleMantenimiento(view, position);
                //abrirGraficos(view);
                //abrirVentanaDeBusqueda(view);
            }
        });

        // Añado un menú contextual al botón de "Añadir"
        registerForContextMenu(fab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (item.getItemId()){
            case R.id.action_settings:
                // Abrir el Activity de lista de coches
                intent = new Intent(this, CarsListActivity.class);
                intent.putExtra("listaCoches", listaCoches);
                startActivity(intent);
                break;
            case R.id.action_export:
                // Abrir el Activity de lista de coches
                /*try {
                    exportDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                try {
                    SqliteExporter.export(baseDatos.getReadableDatabase());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            case R.id.reportIcon:
                abrirTablaGastos(null);
                break;
            case R.id.chartIcon:
                abrirGraficos(null);
                break;
            case R.id.searchIcon:
                abrirVentanaDeBusqueda(null);
                //abrirTablaGastos2(null);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contexto_otros_gastos, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mITV:
                nuevoGasto(R.id.mITV);
                return true;
            case R.id.mSeguro:
                nuevoGasto(R.id.mSeguro);
                return true;
            case R.id.mImpCir:
                nuevoGasto(R.id.mImpCir);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public static MainActivity getInstance() {
        return myContext;
    }




    public void nuevoGasto(int tipo_gasto){
        Intent intent;

        intent = new Intent(this, NuevoGasto.class);
        intent.putExtra("listaCoches", listaCoches);
        intent.putExtra("tipoGasto", tipo_gasto);
        startActivityForResult(intent, NUEVO_GASTO);
    }


    public void nuevaOperacion(View view) {
        Intent intent;

        if (listaCoches.isEmpty()){
            Toast.makeText(this, R.string.Advertencia1, Toast.LENGTH_SHORT).show();
            return;
        }

        if (tabs.getCurrentTab() == 0)
        {
            // Estamos en el Tab de Repostajes
            intent = new Intent(this, NuevoRepostaje.class);
            intent.putExtra("listaCoches", listaCoches);
            startActivityForResult(intent, NUEVO_REPOSTAJE);
        }
        else
        {
            // Estamos en el Tab de Mantenimientos
            intent = new Intent(this, NuevoMantenimiento.class);
            intent.putExtra("listaCoches", listaCoches);
            startActivityForResult(intent, NUEVO_MANTENIMIENTO);
        }
    }

    public void abrirDetalleRepostaje(View view, int pos) {
        // Abrir la ventana de Detalle de Repostajes con los datos del objeto que se
        // ha seleccionado
        repostajeSeleccionado = listaRepostajes.get(pos);

        Intent intent;
        intent = new Intent(this, DetalleRepostaje.class);
        intent.putExtra("repostajeSeleccionado",repostajeSeleccionado);
        intent.putExtra("listaCoches", listaCoches);
        startActivityForResult(intent, EDITAR_REPOSTAJE);
    }

    public void abrirDetalleMantenimiento(View view, int pos) {
        // Abrir la ventana de Detalle de Mantenimientos con los datos del objeto que se
        // ha seleccionado
        mantenimientoSeleccionado = listaMantenimientos.get(pos);

         //Aquí hay que diferenciar si es un mantenimiento o un gasto y abrir la ventana que sea y pasarle el tipo en otro parámetro.
        if (mantenimientoSeleccionado.getTipo_gasto().equals("Mantenimiento")) {
            Intent intent;
            intent = new Intent(this, DetalleMantenimiento.class);
            intent.putExtra("mantenimientoSeleccionado", mantenimientoSeleccionado);
            intent.putExtra("listaCoches", listaCoches);
            startActivityForResult(intent, EDITAR_MANTENIMIENTO);
        } else {
            Intent intent;
            intent = new Intent(this, DetalleGasto.class);
            intent.putExtra("mantenimientoSeleccionado", mantenimientoSeleccionado);
            intent.putExtra("listaCoches", listaCoches);
            startActivityForResult(intent, EDITAR_GASTO);
        }
    }

    public void abrirGraficos(View view) {
        // Abrir ventana de Graficos (ahora solo con datos de ejemplo
        ArrayList<Repostaje> listaRep = new ArrayList<>();
        listaRep = (ArrayList<Repostaje>)listaRepostajes;
        ArrayList<Mantenimiento> listaMant = new ArrayList<>();
        listaMant = (ArrayList<Mantenimiento>)listaMantenimientos;
        Intent intent;
        intent = new Intent(this, Graficos.class);
        intent.putExtra("listaRepostajes", listaRep);
        intent.putExtra("listaMantenimientos", listaMant);
        startActivity(intent);
    }

    public void abrirVentanaDeBusqueda(View view){
        ArrayList<Mantenimiento> listaMant = (ArrayList<Mantenimiento>)listaMantenimientos;
        //ArrayList<Coche> listaCochesTemp = (ArrayList<Coche>)listaCoches;
        Intent intent;
        intent = new Intent(this, SearchActivity.class);
        intent.putExtra("listaMant", listaMant);
        intent.putExtra("listaCoches", listaCoches);
        startActivity(intent);
    }

    public void abrirTablaGastos(View view) {

        // Tabla buena
        Intent intent;
        intent = new Intent(this, InformesActivity.class);
        intent.putExtra("listaCoches", listaCoches);
        intent.putExtra("listaRepostajes", (ArrayList<Repostaje>)listaRepostajes);
        intent.putExtra("listaMantenimientos", (ArrayList<Mantenimiento>)listaMantenimientos);
        startActivity(intent);


    }

    public void abrirTablaGastos2(View view) {

        // Tabla de prueba
        Intent intent;
        intent = new Intent(this, activity_informes2.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to:
        // Posibles respuestas:
        //      1. Nuevo repostaje + resultado ok
        //      2. Nuevo repostaje + resultado cancel
        //      3. Nuevo mantenimiento + resultado ok
        //      4. Nuevo mantenimiento + resultado cancel

        //      5. Editar repostaje + resultado "guardar"
        //      6. Editar repostaje + resultado cancel

        // Para minimizar el código, lo lógico sería:
        // if (resultado == ok){
        // if (Nuevo repostaje){}
        // else (es Nuevo mantenimiento)}
        // else (es resultado == cancel. No hacer nada)

        Repostaje objRepostaje = new Repostaje();
        if (requestCode == NUEVO_REPOSTAJE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Hay que recoger el objeto que vendrá en el Intent y cargarlo en la BD
                objRepostaje = (Repostaje)data.getExtras().getSerializable("parametro");
                tablaRepostajeDB.insertarRepostajeDB(objRepostaje);
                tablaRepostajeDB.leerRepostajesBD();
                // Actualizar la lista en pantalla
                repostajeAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.
            }
        }else if (requestCode == EDITAR_REPOSTAJE) {
            if (resultCode == RESULT_OK) {
                // Puede ser una operación de MODIFICAR o de BORRAR. Comprobamos:

                objRepostaje = (Repostaje)data.getExtras().getSerializable("parametro");
                if (objRepostaje.getLugar().equals("Borrar Repostaje") && (objRepostaje.getKmTotales() == 0) && (objRepostaje.getImporte() == 110011))
                {
                    // Se borra el objeto de la BD
                    tablaRepostajeDB.borrarRepostajeDB(repostajeSeleccionado);
                    tablaRepostajeDB.leerRepostajesBD();
                    // Actualizar la lista en pantalla
                    repostajeAdapter.notifyDataSetChanged();
                } else{
                    // Se modifica el objeto en la BD
                    tablaRepostajeDB.actualizarRepostajeDB(objRepostaje, repostajeSeleccionado);
                    tablaRepostajeDB.leerRepostajesBD();
                    // Actualizar la lista en pantalla
                    repostajeAdapter.notifyDataSetChanged();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.
                }
            }

        Mantenimiento objMantenimiento = new Mantenimiento();
        if (requestCode == NUEVO_MANTENIMIENTO) {
            if (resultCode == RESULT_OK) {
                // Hay que recoger el objeto que vendrá en el Intent y cargarlo en la BD
                objMantenimiento = (Mantenimiento)data.getExtras().getSerializable("parametro");
                tablaMantenimientoDB.insertarMantenimientoDB(objMantenimiento);
                tablaMantenimientoDB.leerMantenimientosBD();
                // Actualizar la lista en pantalla
                mantenimientoAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.
            }
        }else if (requestCode == EDITAR_MANTENIMIENTO) {
            if (resultCode == RESULT_OK) {
                // Puede ser una operación de MODIFICAR o de BORRAR. Comprobamos:
                objMantenimiento = (Mantenimiento)data.getExtras().getSerializable("parametro");
                if (objMantenimiento.getLugar().equals("Borrar Mantenimiento") && (objMantenimiento.getKmTotales() == 0) && (objMantenimiento.getImporte() == 110011))
                {
                    // Se borra el objeto de la BD
                    tablaMantenimientoDB.borrarMantenimientoDB(mantenimientoSeleccionado);
                    tablaMantenimientoDB.leerMantenimientosBD();
                    // Actualizar la lista en pantalla
                    mantenimientoAdapter.notifyDataSetChanged();
                } else {
                    // Se modifica el objeto en la BD
                    tablaMantenimientoDB.actualizarMantenimientoDB(objMantenimiento, mantenimientoSeleccionado);
                    tablaMantenimientoDB.leerMantenimientosBD();
                    // Actualizar la lista en pantalla
                    mantenimientoAdapter.notifyDataSetChanged();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada
            }
        }

        // Los nuevos gastos serán considerados mantenimientos
        Mantenimiento objGasto = new Mantenimiento();
        if (requestCode == NUEVO_GASTO){
            if (resultCode == RESULT_OK){
                // Hay que recoger el objeto que vendrá en el Intent y cargarlo en la BD
                objGasto = (Mantenimiento)data.getExtras().getSerializable("parametro");
                tablaMantenimientoDB.insertarMantenimientoDB(objGasto);
                tablaMantenimientoDB.leerMantenimientosBD();
                // Actualizar la lista en pantalla
                mantenimientoAdapter.notifyDataSetChanged();
            }
            else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada.
            }
        }else if (requestCode == EDITAR_GASTO){
            if (resultCode == RESULT_OK){
                // Puede ser una operación de MODIFICAR o de BORRAR. Comprobamos:
                objGasto = (Mantenimiento)data.getExtras().getSerializable("parametro");
                if (objGasto.getLugar().equals("Borrar Mantenimiento") && (objGasto.getKmTotales() == 0) && (objGasto.getImporte() == 110011))
                {
                    // Se borra el objeto de la BD
                    tablaMantenimientoDB.borrarMantenimientoDB(mantenimientoSeleccionado);
                    tablaMantenimientoDB.leerMantenimientosBD();
                    // Actualizar la lista en pantalla
                    mantenimientoAdapter.notifyDataSetChanged();
                } else {
                    // Se modifica el objeto en la BD
                    tablaMantenimientoDB.actualizarMantenimientoDB(objGasto, mantenimientoSeleccionado);
                    tablaMantenimientoDB.leerMantenimientosBD();
                    // Actualizar la lista en pantalla
                    mantenimientoAdapter.notifyDataSetChanged();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Se ha cancelado; no se hace nada
            }
        }
    }

}
