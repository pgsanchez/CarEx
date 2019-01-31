package com.example.retaliator.carex;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;


public class InformesActivity extends AppCompatActivity {

    ArrayList<Coche> listaCoches = new ArrayList<>();
    ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
    ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();

    int numeroDeVisitasAlTaller = 0;
    int numeroDeRepostajes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            listaCoches = (ArrayList<Coche>) getIntent().getExtras().getSerializable("listaCoches");
            listaRepostajes = (ArrayList<Repostaje>) getIntent().getExtras().getSerializable("listaRepostajes");
            listaMantenimientos = (ArrayList<Mantenimiento>) getIntent().getExtras().getSerializable("listaMantenimientos");
        }

        crearTablaDeGastos1Coche();



    }

    private void crearTablaDeGastos(){
        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String añoActual = sdf.format(fechaActual);
        float totalCoche1 = 0;

        TableLayout table = (TableLayout) findViewById(R.id.tablaGastos);

        // Vamos a tener 4 filas:
        TableRow tableRow0 = (TableRow) findViewById(R.id.cabecera); // Cabecera (Gastos)
        TableRow tableRow1 = (TableRow) findViewById(R.id.gasolina); // Gasolina
        TableRow tableRow2 = (TableRow) findViewById(R.id.mantenimiento); // Mantenimiento
        TableRow tableRow3 = (TableRow) findViewById(R.id.total); // Total

        //--> Estas cuatro celdas (es decir, la primera columna), también la puedo meter en el .xml y ponerle las características que quiera
        // Columna 0 (títulos)
        //TextView celda00 = new TextView(this);
        TextView celda00 = (TextView) findViewById(R.id.celda00);
        TextView celda10 = (TextView) findViewById(R.id.celda10);
        TextView celda20 = (TextView) findViewById(R.id.celda20);
        TextView celda30 = (TextView) findViewById(R.id.celda30);

        //celda00.setText("Gastos");
        //celda10.setText("Gasolina");
        //celda20.setText("Mantenimiento");
        //celda30.setText("TOTAL");

        //--> Esta separación igual también se puede poner en el .xml
        // Separación vertical entre celdas (entre las líneas)
        /*celda00.setLines(2);
        celda10.setLines(2);
        celda20.setLines(2);
        celda30.setLines(2);*/

        //tableRow0.addView(celda00);
        //tableRow1.addView(celda10);
        //tableRow2.addView(celda20);
        //tableRow3.addView(celda30);

        float importeGasolina = 0;
        float importeMantenimiento = 0;

        DecimalFormat formato1 = new DecimalFormat("0.00");

        // Ponemos la cabecera en negrita y centrada
        //celda00.setGravity(Gravity.CENTER_HORIZONTAL);
        //celda00.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //celda00.setLayoutParams(layoutCelda);

        int x = 1;
        for (Coche c: listaCoches) {
            TextView celda0 = new TextView(this);
            TextView celda1 = new TextView(this);
            TextView celda2 = new TextView(this);
            TextView celda3 = new TextView(this);

            TableRow.LayoutParams param = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(20,2,20,2);
            celda0.setText(c.getNombre()); // El nombre del coche
            celda0.setGravity(Gravity.END);
            celda0.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            celda0.setLayoutParams(param);

            importeGasolina = calcularImporteGasolina(c.getId_coche(), añoActual);
            celda1.setText(String.valueOf(formato1.format(importeGasolina))); // El importe de la gasolina
            /*if (x==1)
                celda1.setBackgroundColor(Color.CYAN);
            else
                celda1.setBackgroundColor(Color.GREEN);*/
            celda1.setGravity(Gravity.END);
            celda1.setLayoutParams(param);

            importeMantenimiento = calcularImporteMantenimiento(c.getId_coche(), añoActual);
            celda2.setText(String.valueOf(formato1.format(importeMantenimiento))); // El importe de los mantenimientos
            /*if (x==1)
                celda2.setBackgroundColor(Color.CYAN);
            else
                celda2.setBackgroundColor(Color.GREEN);*/
            celda2.setGravity(Gravity.CENTER_HORIZONTAL);
            celda2.setLayoutParams(param);

            celda3.setText(String.valueOf(formato1.format(importeGasolina+importeMantenimiento))); // El total
            celda3.setGravity(Gravity.CENTER_HORIZONTAL);
            celda3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            celda3.setLayoutParams(param);

            if ((importeGasolina != 0) || (importeMantenimiento != 0)) {
                tableRow0.addView(celda0);
                tableRow1.addView(celda1);
                tableRow2.addView(celda2);
                tableRow3.addView(celda3);
            }
            x++;
        }


        /*table.addView(tableRow0);
        //table.addView(frame1);
        table.addView(tableRow1);
        table.addView(tableRow2);
        table.addView(tableRow3);*/
    }

    private void crearTablaDeGastos1Coche(){
        Date fechaActual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String añoActual = sdf.format(fechaActual);
        float totalCoche1 = 0;


        TableLayout table = (TableLayout) findViewById(R.id.tablaGastos);

        TextView celda11 = (TextView) findViewById(R.id.celda11);
        TextView celda21 = (TextView) findViewById(R.id.celda21);
        TextView celda31 = (TextView) findViewById(R.id.celda31);

        float importeGasolina = 0;
        float importeMantenimiento = 0;

        DecimalFormat formato1 = new DecimalFormat("0.00");


        Coche c = listaCoches.get(0);


            importeGasolina = calcularImporteGasolina(c.getId_coche(), añoActual);
            String strImporteGasolina = String.valueOf(formato1.format(importeGasolina));
            celda11.setText(strImporteGasolina + "€"); // El importe de la gasolina

            importeMantenimiento = calcularImporteMantenimiento(c.getId_coche(), añoActual);
            String strImporteMantenimiento = String.valueOf(formato1.format(importeMantenimiento));
            celda21.setText(strImporteMantenimiento + "€"); // El importe de los mantenimientos

            String strImporteTotal = String.valueOf(formato1.format(importeGasolina+importeMantenimiento));
            celda31.setText(strImporteTotal + "€"); // El total

        TextView resumen = (TextView) findViewById(R.id.resumenGastos);
        String textoResumen = "En lo que va de año has ido al taller " + numeroDeVisitasAlTaller + " veces, con un gasto total de " + strImporteMantenimiento + "€. Además, has repostado " + numeroDeRepostajes + " veces, con un gasto de " + strImporteGasolina + "€. En total, has gastado en este coche " + strImporteTotal + "€.";
        resumen.setText(textoResumen);
    }

    private float calcularImporteGasolina(int coche, String anno)
    {
        float sumaImportes = 0;
        numeroDeRepostajes = 0;
        for (Repostaje rep : listaRepostajes) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String  annoRepostaje= sdf.format(rep.getFecha());
            if ((annoRepostaje.equals(anno)) && (rep.getCoche() == coche)){
                sumaImportes += rep.getImporte();
                numeroDeRepostajes++;
            }
        }
        return sumaImportes;
    }

    private float calcularImporteMantenimiento(int coche, String anno)
    {
        float sumaImportes = 0;
        numeroDeVisitasAlTaller = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String  annoRepostaje= sdf.format(mant.getFecha());
            if ((annoRepostaje.equals(anno)) && (mant.getCoche() == coche)){
                sumaImportes += mant.getImporte();
                numeroDeVisitasAlTaller++;
            }
        }
        return sumaImportes;
    }

    private int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }
}
