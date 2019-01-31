package com.example.retaliator.carex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Graficos extends AppCompatActivity {

    private String meses[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private String[] annos;
    private String[] annos2;

    private ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>(); // Lista de Respostajes
    private ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>(); // Lista de Mantenimientos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        if (getIntent() != null){
            listaRepostajes = (ArrayList<Repostaje>)getIntent().getExtras().getSerializable("listaRepostajes");
            listaMantenimientos = (ArrayList<Mantenimiento>)getIntent().getExtras().getSerializable("listaMantenimientos");
        }


        //Collections.reverse(listaRepostajes);
        List<Integer> list = new ArrayList<Integer>(); // Lista de años. Comienzo en 2015
        final Spinner spinner = (Spinner) findViewById(R.id.annoSpinner);
        Calendar annoActual = Calendar.getInstance();
        for (int i = annoActual.get(Calendar.YEAR); i > 2015; i--) {
            list.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);

        //dibujaGraficaDeBarras();
        //dibujaGraficaDeBarrasXaño(annoActual.get(Calendar.YEAR));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String texto = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Integer nuevoAnno = (int) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                //Log.i("Nuevo Año = ", nuevoAnno.toString());
                dibujaGraficaDeBarrasXanno(nuevoAnno);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        dibujaGraficaDeBarrasMantenimientos();
        dibujaGraficaDeLineas();

    }

    private void dibujaGraficaDeBarras(){
        BarChart barchar1 = (BarChart) findViewById(R.id.barchart1);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        Calendar mesActual = Calendar.getInstance();
        //Log.i("MesActual = ", mesActual.getTime().toString() );

        float valorX = 12f;
        float valorY = 0f;

        Iterator<Repostaje> it = listaRepostajes.iterator();
        while(it.hasNext() && valorX > 0 ){
            Repostaje rep = it.next();
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(rep.getFecha());

            //Log.i("MesElemento = ", fecha.getTime().toString() );
            //Log.i("MesActual = ", mesActual.getTime().toString() );
            while (fecha.get(Calendar.MONTH) != mesActual.get(Calendar.MONTH))
            {
                entries.add(new BarEntry(valorX, valorY));
                valorX -= 1.f;
                valorY = 0;
                mesActual.set(Calendar.MONTH, mesActual.get(Calendar.MONTH)-1);
            }
            valorY += rep.getImporte();

        }
        entries.add(new BarEntry(valorX, valorY));

        XAxis xAxis = barchar1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = barchar1.getAxisLeft();
        YAxis rightAxis = barchar1.getAxisRight();
        rightAxis.setEnabled(false);

        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barchar1.setData(data);
        barchar1.setFitBars(true); // make the x-axis fit exactly all bars
        barchar1.invalidate(); // refresh*/
    }

    private void dibujaGraficaDeBarrasXanno(int anno){
        BarChart barchar1 = (BarChart) findViewById(R.id.barchart1);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        //Calendar annoActual = Calendar.getInstance();
        //annoActual.set
        //Log.i("Año Actual = ", annoActual.getTime().toString() );

        float valorY = 0f;
        int mesEscalaX = 11; // Diciembre

        Iterator<Repostaje> it = listaRepostajes.iterator();
        while(it.hasNext() && mesEscalaX >= 0){
            Repostaje rep = it.next();
            Calendar fechaRepostaje = Calendar.getInstance();
            fechaRepostaje.setTime(rep.getFecha());
            //if (fechaRepostaje.get((Calendar.YEAR)) != annoActual.get(Calendar.YEAR)){
            if (fechaRepostaje.get((Calendar.YEAR)) > anno){
                // No se hace nada. Hay que coger el siguiente elemento
            }
            else if (fechaRepostaje.get((Calendar.YEAR)) < anno){
                // Hay que salir del bucle While
                break;
            }
            else{
                //Log.i("MesElemento = ", fechaRepostaje.getTime().toString() );
                //Log.i("MesActual = ", annoActual.getTime().toString() );
                while (fechaRepostaje.get(Calendar.MONTH) != mesEscalaX)
                {
                    // No hay repostajes en el mes mesEScalaX. Pasamos al mes anterior.
                    entries.add(new BarEntry(mesEscalaX, valorY));
                    mesEscalaX --;
                    valorY = 0;
                }
                valorY += rep.getImporte();
            }

        }
        entries.add(new BarEntry(mesEscalaX, valorY));
        mesEscalaX--;

        // Si hemos llegado al final de la lista pero aun quedan meses hasta Enero, completamos la
        // gráfica añadiendo los meses que faltan hasta Enero.
        while(mesEscalaX >= 0){
            entries.add(new BarEntry(mesEscalaX, 0));
            mesEscalaX--;
        }

        XAxis xAxis = barchar1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(meses));

        YAxis leftAxis = barchar1.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setAxisMinimum(0f); // start at zero
        YAxis rightAxis = barchar1.getAxisRight();
        rightAxis.setEnabled(false);

        BarDataSet set = new BarDataSet(entries, "Importe de los repostajes");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barchar1.setData(data);
        barchar1.setFitBars(true); // make the x-axis fit exactly all bars
        barchar1.setDescription(null);
        barchar1.invalidate(); // refresh*/
    }

    private void dibujaGraficaDeBarrasMantenimientos(){
        BarChart barchart2 = (BarChart) findViewById(R.id.barchart2);
        List<BarEntry> entries = new ArrayList<BarEntry>();


        Calendar annoActual = Calendar.getInstance();

        float valorY = 0f;
        int annoEscalaX = annoActual.get(Calendar.YEAR); // Diciembre
        int annoFinal = obtenerAnnoUltimoElementoListaMantenimientos();
        annos = new String[annoEscalaX - annoFinal + 1];
        for (int i = annoActual.get(Calendar.YEAR), j =0 ; i >= annoFinal; i--, j++) {
            annos[j] = String.valueOf(i);
            //Log.d("annos.length = ", String.valueOf(annos.length));
        }

        Iterator<Mantenimiento> it = listaMantenimientos.iterator();
        while(it.hasNext() && annoEscalaX >= annoFinal){
            Mantenimiento mant = it.next();
            Calendar fechaMantenimiento = Calendar.getInstance();
            fechaMantenimiento.setTime(mant.getFecha());


            while (fechaMantenimiento.get(Calendar.YEAR) != annoEscalaX)
            {
                entries.add(new BarEntry(annoEscalaX, valorY));
                //Log.d("entries = ", String.valueOf(annoEscalaX));
                annoEscalaX --;
                valorY = 0;
            }
            valorY += mant.getImporte();
        }
        entries.add(new BarEntry(annoEscalaX, valorY));
        //Log.d("entries fin = ", String.valueOf(annoEscalaX));

        XAxis xAxis = barchart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new XAxisValueFormatterYears(annos));
        xAxis.setGranularity(1f);

        YAxis leftAxis = barchart2.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        leftAxis.setAxisMinimum(0f); // start at zero
        YAxis rightAxis = barchart2.getAxisRight();
        rightAxis.setEnabled(false);

        BarDataSet set = new BarDataSet(entries, "Importe del taller");

        BarData data = new BarData(set);
        //data.setBarWidth(0.9f); // set custom bar width
        barchart2.setData(data);
        barchart2.setFitBars(true); // make the x-axis fit exactly all bars
        barchart2.setDescription(null);
        //barchart2.setDrawBorders(false);
        //barchart2.setNoDataText("");
        //barchart2.invalidate(); // refresh*/
    }

    private void dibujaGraficaDeLineas(){
        LineChart lineChart1 = (LineChart) findViewById(R.id.linechart1);

        List<Entry> entries = new ArrayList<Entry>();

        int annoFinal = obtenerAnnoUltimoElementoListaRepostajes();

        Calendar annoActual = Calendar.getInstance();
        int annoEscalaX = annoActual.get(Calendar.YEAR); // Diciembre
        annos2 = new String[annoEscalaX - annoFinal + 1];
        for (int i = annoActual.get(Calendar.YEAR), j =0 ; i >= annoFinal; i--, j++) {
            annos2[j] = String.valueOf(i);
            //Log.d("annos2.length = ", String.valueOf(annos2.length));
        }

        /*Repostaje primerRepostaje = listaRepostajes.get(0);
        long valoractual = primerRepostaje.getFecha().getTime();
        long valoranterior = valoractual;
        valoranterior = valoranterior / (1000 * 60 * 60 * 24);*/

        //Iterator<Repostaje> it = listaRepostajes.iterator(listaRepostajes.size());
        ListIterator it = listaRepostajes.listIterator(listaRepostajes.size());

        while (it.hasPrevious()){
            Repostaje rep = (Repostaje) it.previous();
            Calendar fechaRep = Calendar.getInstance();
            fechaRep.setTime(rep.getFecha());
            int dias = obtenerDiaDesdeAnno(annoFinal, fechaRep);
            //Log.d("Dias = ", String.valueOf(dias));
            //Log.d("Precio = ", String.valueOf(rep.getPrecio()));
            entries.add(new Entry(dias, rep.getPrecio()));
        }
        /*while(it.hasNext()){
            Repostaje rep = it.next();
            /*valoractual = rep.getFecha().getTime();
            valoractual = valoractual / (1000 * 60 * 60 * 24);
            entries.add(new Entry(valoractual - valoranterior, rep.getPrecio()));
            long x = valoractual - valoranterior;
            valoranterior = valoractual;* /

            Calendar fechaRep = Calendar.getInstance();
            fechaRep.setTime(rep.getFecha());
            int dias = obtenerDiaDesdeAnno(annoFinal, fechaRep);
            Log.d("Dias = ", String.valueOf(dias));
            Log.d("Precio = ", String.valueOf(rep.getPrecio()));
            entries.add(new Entry(dias, rep.getPrecio()));
        }*/

        /*entries.add(new Entry(280, 1.019f));
        entries.add(new Entry(547, 1.059f));
        entries.add(new Entry(549, 1.059f));
        entries.add(new Entry(561, 1.059f));
        entries.add(new Entry(571, 1.059f));
        entries.add(new Entry(575, 1.229f));*/


        //Log.d("LLega hasta aquí", "");
        XAxis xAxis = lineChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        //xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new XAxisValueFormatterDays(annos2));
        xAxis.setGranularity(1f);

        YAxis leftAxis = lineChart1.getAxisLeft();
        leftAxis.setDrawZeroLine(false);
        //leftAxis.setAxisMinimum(0f); // start at zero
        YAxis rightAxis = lineChart1.getAxisRight();
        rightAxis.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Evolución del precio del gasoil");

        LineData data = new LineData(dataSet);
        lineChart1.setData(data);
        lineChart1.setDescription(null);
        //lineChart1.setDrawGridBackground(true);
        //lineChart1.setDrawBorders(true);
        //lineChart1.invalidate();
    }

    private String obtenerNombreDelMes(int numeroMes){
        // 0 = Enero
        return meses[numeroMes];
    }

    private int obtenerAnnoUltimoElementoListaMantenimientos(){
        Mantenimiento mant = listaMantenimientos.get(listaMantenimientos.size()-1);
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(mant.getFecha());
        return fecha.get(Calendar.YEAR);
    }

    private int obtenerAnnoUltimoElementoListaRepostajes(){
        Repostaje rep = listaRepostajes.get(listaRepostajes.size()-1);
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(rep.getFecha());
        return fecha.get(Calendar.YEAR);
    }

    //Función para obtener el número de días de una fecha desde el 1/1 de un año dado.
    int obtenerDiaDesdeAnno(int annoReferencia, Calendar fecha){
        Calendar fechaBase = Calendar.getInstance();
        fechaBase.set(Calendar.YEAR, annoReferencia);
        Calendar diasDeAnno = Calendar.getInstance();
        int numeroDias = 0;
        for (int i = annoReferencia; i < fecha.get(Calendar.YEAR); i++){
            diasDeAnno.set(i,11,31);
            numeroDias += diasDeAnno.get(Calendar.DAY_OF_YEAR);
        }
        numeroDias += fecha.get(Calendar.DAY_OF_YEAR);
        return numeroDias;
    }

}
