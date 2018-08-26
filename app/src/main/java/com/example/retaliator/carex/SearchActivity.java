package com.example.retaliator.carex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView restultsListView; // Actividades de la lista de mantenimientos
    MantenimientoAdapter mantenimientoAdapter;
    private ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
    private List<Mantenimiento> listaResultados = new ArrayList<Mantenimiento>();
    private ArrayList<Coche> listaCoches;


    //EditText editBuscar = (EditText) findViewById(R.id.editSearch);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        final EditText editBuscar = (EditText) findViewById(R.id.editSearch);

        if (getIntent() != null){
            listaMantenimientos = (ArrayList<Mantenimiento>)getIntent().getExtras().getSerializable("listaMant");
            listaCoches = (ArrayList<Coche>)getIntent().getExtras().getSerializable("listaCoches");
        }

        this.restultsListView = (ListView) findViewById(R.id.listResults);

        editBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editBuscar.getText().length() > 2){
                    // Buscar ese texto en la lista
                    buscarTextoEnListaMantenimientos(editBuscar.getText().toString());
                }
            }
        });

        // Responder a un click en la lista de resultados
        restultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                abrirDetalleMantenimiento(view, position);
            }
        });

    }

    //Funcion para buscar un texto en la lista y mostrar la lista en la pantalla
    public void buscarTextoEnListaMantenimientos(String texto){
        listaResultados.clear();
        float suma = 0.0f;

        for(int indice = 0;indice<listaMantenimientos.size();indice++)
        {
            if (listaMantenimientos.get(indice).getReparacion().toUpperCase().contains(texto.toUpperCase())){
                listaResultados.add(listaMantenimientos.get(indice));
                suma += listaMantenimientos.get(indice).getImporte();
            }
        }

        mantenimientoAdapter = new MantenimientoAdapter(this, listaResultados, listaCoches);
        this.restultsListView.setAdapter(mantenimientoAdapter);

        TextView sumaTotal  = (TextView) findViewById(R.id.sumTotal);
        sumaTotal.setText(String.valueOf(suma) + "€");
    }

    public void abrirDetalleMantenimiento(View view, int pos) {
        // Abrir la ventana de Detalle de Mantenimientos con los datos del objeto que se
        // ha seleccionado
        Mantenimiento mantenimientoSeleccionado = new Mantenimiento();
        mantenimientoSeleccionado = listaResultados.get(pos);

        Intent intent;
        intent = new Intent(this, DetalleMantenimiento.class);
        intent.putExtra("mantenimientoSeleccionado",mantenimientoSeleccionado);
        intent.putExtra("listaCoches", listaCoches);
        startActivityForResult(intent, 0);
    }

}
