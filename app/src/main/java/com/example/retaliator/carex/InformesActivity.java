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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class InformesActivity extends AppCompatActivity {

    ArrayList<Coche> listaCoches = new ArrayList<>();
    ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
    ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();

    Coche selectedCar = new Coche(); // Coche seleccionado
    Integer selectedYear = Calendar.getInstance().get(Calendar.YEAR); // Año seleccionado

    int numeroDeVisitasAlTaller = 0;
    int numeroDeRepostajes = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            listaCoches = (ArrayList<Coche>) getIntent().getExtras().getSerializable("listaCoches");
            listaRepostajes = (ArrayList<Repostaje>) getIntent().getExtras().getSerializable("listaRepostajes");
            listaMantenimientos = (ArrayList<Mantenimiento>) getIntent().getExtras().getSerializable("listaMantenimientos");
        }


        List<Integer> list = new ArrayList<Integer>(); // Lista de años. Comienzo en 2015 hasta el año actual
        final Spinner spinner = (Spinner) findViewById(R.id.annoSpinner);
        Calendar annoActual = Calendar.getInstance();
        selectedYear = annoActual.get(Calendar.YEAR);
        for (int i = annoActual.get(Calendar.YEAR); i > 2015; i--) {
            list.add(i);
        }
        // Se mete la lista de años en su spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = (int) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                crearTablaDeGastos1Coche();
                EscribirGastosTotales();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        List<String> carsList = new ArrayList<String>(); // Lista de coches para el spinner
        final Spinner carSpinner = (Spinner) findViewById(R.id.carSpinner);
        for (int i = 0; i < listaCoches.size(); i++) {
            Coche c = (Coche)listaCoches.get(i);
            carsList.add(c.getNombre());
        }

        ArrayAdapter<String> dataCarAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, carsList);
        carSpinner.setAdapter(dataCarAdapter);

        // Se pone el icono correspondiente
        final ImageView iconCar = (ImageView) findViewById((R.id.iconCar));
        // Seleccionamos el primer coche de la lista:
        selectedCar = listaCoches.get(0);
        iconCar.setImageResource(selectedCar.getIcono());
        iconCar.setColorFilter(selectedCar.getColor());

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String textoSpinner = carSpinner.getItemAtPosition(carSpinner.getSelectedItemPosition()).toString();
                for (int i = 0; i < listaCoches.size(); i++) {
                    if (textoSpinner.equals(((Coche)listaCoches.get(i)).getNombre())){
                        //dlgRepostaje.setCoche(((Coche) lista.get(i)).getId_coche());
                        selectedCar = (Coche)listaCoches.get(i);
                        iconCar.setImageResource(selectedCar.getIcono());
                        iconCar.setColorFilter(selectedCar.getColor());
                        crearTablaDeGastos1Coche();
                        EscribirGastosTotales();
                        return;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        crearTablaDeGastos1Coche();
        EscribirGastosTotales();



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
        String añoActual2 = selectedYear.toString();

        TableLayout table = (TableLayout) findViewById(R.id.tablaGastos);

        TextView celda11 = (TextView) findViewById(R.id.celda11);
        TextView celda21 = (TextView) findViewById(R.id.celda21);
        TextView celda31 = (TextView) findViewById(R.id.celda31);
        TextView celda41 = (TextView) findViewById(R.id.celda41);
        TextView celda51 = (TextView) findViewById(R.id.celda51);
        TextView celda61 = (TextView) findViewById(R.id.celda61);

        float importeGasolina = 0;
        float importeMantenimiento = 0;
        float importeITV = 0;
        float importeSeguro = 0;
        float importeImpCir = 0;

        DecimalFormat formato1 = new DecimalFormat("0.00");


        Coche c = selectedCar;


            importeGasolina = calcularImporteGasolina(c.getId_coche(), añoActual2);
            String strImporteGasolina = String.valueOf(formato1.format(importeGasolina));
            celda11.setText(strImporteGasolina + "€"); // El importe de la gasolina

            importeMantenimiento = calcularImporteMantenimiento(c.getId_coche(), añoActual2);
            String strImporteMantenimiento = String.valueOf(formato1.format(importeMantenimiento));
            celda21.setText(strImporteMantenimiento + "€"); // El importe de los mantenimientos

        importeITV = calcularImporteITV(c.getId_coche(), añoActual2);
        String strImporteITV = String.valueOf(formato1.format(importeITV));
        celda31.setText(strImporteITV + "€"); // El importe de la ITV

        importeSeguro = calcularImporteSeguro(c.getId_coche(), añoActual2);
        String strImporteSeguro = String.valueOf(formato1.format(importeSeguro));
        celda41.setText(strImporteSeguro + "€"); // El importe del Seguro

        importeImpCir = calcularImporteImpCirculacion(c.getId_coche(), añoActual2);
        String strImporteImpCir = String.valueOf(formato1.format(importeImpCir));
        celda51.setText(strImporteImpCir + "€"); // El importe del Seguro

            String strImporteTotal = String.valueOf(formato1.format(importeGasolina+importeMantenimiento+importeITV+importeSeguro+importeImpCir));
            celda61.setText(strImporteTotal + "€"); // El total

        TextView resumen = (TextView) findViewById(R.id.resumenGastos);
        String textoResumen = "En este año (" + añoActual2 + ") has repostado " + numeroDeRepostajes + " veces, y has ido al taller " + numeroDeVisitasAlTaller + " veces.";
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
            if ((annoRepostaje.equals(anno)) && (mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Mantenimiento"))){
                sumaImportes += mant.getImporte();
                numeroDeVisitasAlTaller++;
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche y un año, y devuelve el importe de la ITV de ese coche en
    // ese año.
    private float calcularImporteITV(int coche, String anno)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String  annoRepostaje= sdf.format(mant.getFecha());
            if ((annoRepostaje.equals(anno)) && (mant.getCoche() == coche) && (mant.getTipo_gasto().equals("ITV"))){
                sumaImportes += mant.getImporte();
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche, y devuelve el importe TOTAL de la ITV de ese coche a lo largo de
    // todos los años
    private float calcularImporteITVTodosAnnos(int coche)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            if ((mant.getCoche() == coche) && (mant.getTipo_gasto().equals("ITV"))){
                sumaImportes += mant.getImporte();
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche y un año, y devuelve el importe del seguro de ese coche en
    // ese año.
    private float calcularImporteSeguro(int coche, String anno)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String  annoRepostaje= sdf.format(mant.getFecha());
            if ((annoRepostaje.equals(anno)) && (mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Seguro"))){
                sumaImportes += mant.getImporte();
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche, y devuelve el importe TOTAL del seguro de ese coche a lo largo de
    // todos los años
    private float calcularImporteSeguroTodosAnnos(int coche)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            if ((mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Seguro"))){
                sumaImportes += mant.getImporte();
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche y un año, y devuelve el importe del IVTM de ese coche en
    // ese año.
    private float calcularImporteImpCirculacion(int coche, String anno)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String  annoRepostaje= sdf.format(mant.getFecha());
            if ((annoRepostaje.equals(anno)) && (mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Imp. Circulacion"))){
                sumaImportes += mant.getImporte();
            }
        }
        return sumaImportes;
    }

    // Se le pasa por parámetro un coche, y devuelve el importe TOTAL IVTM de ese coche a lo largo de
    // todos los años
    private float calcularImporteImpCirculacionTodosAnnos(int coche)
    {
        float sumaImportes = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            if ((mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Imp. Circulacion"))){
                sumaImportes += mant.getImporte();
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

    private void EscribirGastosTotales()
    {
        Coche c = selectedCar;
        int coche = c.getId_coche();

        // Recorrer la lista de repostajes para obtener el número total de repostajes de ese coche
        // y la suma total del importe de todos los repostajes de dicho coche.
        float sumaImportesRep = 0;
        numeroDeRepostajes = 0;
        for (Repostaje rep : listaRepostajes) {
            if (rep.getCoche() == coche){
                sumaImportesRep += rep.getImporte();
                numeroDeRepostajes++;
            }
        }

        // Recorrer la lista de mantenimientos para obtener el número total de mantenimientos de ese coche
        // y la suma total del importe de todos los mantenimientos de dicho coche. Esta suma NO incluye las
        // ITVs, Seguros e IVTMs; solo los mantenimientos.
        float sumaImportesMant = 0;
        numeroDeVisitasAlTaller = 0;
        for (Mantenimiento mant : listaMantenimientos) {
            if ((mant.getCoche() == coche) && (mant.getTipo_gasto().equals("Mantenimiento"))){
                sumaImportesMant += mant.getImporte();
                numeroDeVisitasAlTaller++;
            }
        }

        // Se calculan los importes totales del Seguro, ITV e IVTM del coche seleccionado para todos los años
        float sumaImportesSeguro = calcularImporteSeguroTodosAnnos(selectedCar.getId_coche());
        float sumaImportesITV = calcularImporteITVTodosAnnos(selectedCar.getId_coche());
        float sumaImportesIVTM = calcularImporteImpCirculacionTodosAnnos(selectedCar.getId_coche());

        // Se pasan todos los valores de los importes a string dándoles formato de "moneda"
        DecimalFormat formato1 = new DecimalFormat("0.00");
        String strImporteGasolina = String.valueOf(formato1.format(sumaImportesRep));
        String strImporteMantenimientos = String.valueOf(formato1.format(sumaImportesMant));
        String strImporteTotalSeguro = String.valueOf(formato1.format(sumaImportesSeguro));
        String strImporteTotalITV = String.valueOf(formato1.format(sumaImportesITV));
        String strImporteTotalIVTM = String.valueOf(formato1.format(sumaImportesIVTM));
        // Se calcula la suma total de todos los gastos anteriores
        String strImporteTotal = String.valueOf(formato1.format(sumaImportesRep+sumaImportesMant+sumaImportesSeguro+sumaImportesITV+sumaImportesIVTM));

        TextView resumenTotales = (TextView) findViewById(R.id.resumenGastosTotales);
        String textoResumen = "Desde que apuntas los gastos, en este coche has echado gasolina " + numeroDeRepostajes
                + " veces, con un gasto total de " + strImporteGasolina + "€. Además, has ido al taller " + numeroDeVisitasAlTaller
                + " veces, con un gasto total de " + strImporteMantenimientos + "€. "
                + "En el Seguro te has gastado en total " + strImporteTotalSeguro + "€. "
                + "En la ITV te has gastado en total " + strImporteTotalITV + "€. "
                + "Y en el Impuesto de circulación " + strImporteTotalIVTM+ "€. "
                + "En total, en el coche llevas gastados " + strImporteTotal + "€.";
        resumenTotales.setText(textoResumen);
    }
}
