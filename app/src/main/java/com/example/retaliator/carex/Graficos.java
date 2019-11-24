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

    private String meses[] = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
    //private String meses[] = {"E", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer nuevoAnno = (int) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
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

    private void dibujaGraficaDeBarrasXanno(int anno){
        BarChart barchar1 = (BarChart) findViewById(R.id.barchart1);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        float valorY = 0f;
        int mesEscalaX = 11; // Diciembre

        Iterator<Repostaje> it = listaRepostajes.iterator();
        while(it.hasNext() && mesEscalaX >= 0){
            Repostaje rep = it.next();
            Calendar fechaRepostaje = Calendar.getInstance();
            fechaRepostaje.setTime(rep.getFecha());

            if (fechaRepostaje.get((Calendar.YEAR)) > anno){
                // No se hace nada. Hay que coger el siguiente elemento
            }
            else if (fechaRepostaje.get((Calendar.YEAR)) < anno){
                // Hay que salir del bucle While
                break;
            }
            else{
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
        xAxis.setLabelCount(12);
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
        // Si annoFinal == 0, quiere decir que la lista está vacía y, por lo tanto, no hay datos
        // que dibujar. Dibujaremos la gráfica vacía y le pondremos una escala de 1 año atrás en
        // el eje X
        if (annoFinal == 0)
            annoFinal = annoEscalaX - 1;

        annos = new String[annoEscalaX - annoFinal + 1];
        for (int i = annoActual.get(Calendar.YEAR), j =0 ; i >= annoFinal; i--, j++) {
            annos[j] = String.valueOf(i);
        }

        Iterator<Mantenimiento> it = listaMantenimientos.iterator();
        while(it.hasNext() && annoEscalaX >= annoFinal){
            Mantenimiento mant = it.next();
            Calendar fechaMantenimiento = Calendar.getInstance();
            fechaMantenimiento.setTime(mant.getFecha());


            while (fechaMantenimiento.get(Calendar.YEAR) != annoEscalaX)
            {
                entries.add(new BarEntry(annoEscalaX, valorY));
                annoEscalaX --;
                valorY = 0;
            }
            valorY += mant.getImporte();
        }
        entries.add(new BarEntry(annoEscalaX, valorY));

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
        barchart2.setData(data);
        barchart2.setFitBars(true); // make the x-axis fit exactly all bars
        barchart2.setDescription(null);
    }

    private void dibujaGraficaDeLineas(){
        LineChart lineChart1 = (LineChart) findViewById(R.id.linechart1);

        List<Entry> entries = new ArrayList<Entry>();

        Calendar annoActual = Calendar.getInstance();
        int annoEscalaX = annoActual.get(Calendar.YEAR); // Diciembre

        int annoFinal = obtenerAnnoUltimoElementoListaRepostajes();
        // Si la función anterior devuelve un 0 quiere decir que la lista de repostajes está vacía.
        // Si continuamos, esta función fallará y hará caer la aplicación. Paramos aquí la función.
        if (annoFinal == 0)
            return;


        annos2 = new String[annoEscalaX - annoFinal + 1];
        for (int i = annoActual.get(Calendar.YEAR), j =0 ; i >= annoFinal; i--, j++) {
            annos2[j] = String.valueOf(i);
        }

        if (listaRepostajes.size() != 0) {
            ListIterator it = listaRepostajes.listIterator(listaRepostajes.size());

            while (it.hasPrevious()) {
                Repostaje rep = (Repostaje) it.previous();
                Calendar fechaRep = Calendar.getInstance();
                fechaRep.setTime(rep.getFecha());
                int dias = obtenerDiaDesdeAnno(annoFinal, fechaRep);
                entries.add(new Entry(dias, rep.getPrecio()));
            }
        }
        XAxis xAxis = lineChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);

        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new XAxisValueFormatterDays(annos2));
        xAxis.setGranularity(1f);

        YAxis leftAxis = lineChart1.getAxisLeft();
        leftAxis.setDrawZeroLine(false);

        YAxis rightAxis = lineChart1.getAxisRight();
        rightAxis.setEnabled(false);

        LineDataSet dataSet = new LineDataSet(entries, "Evolución del precio del gasoil");

        LineData data = new LineData(dataSet);
        lineChart1.setData(data);
        lineChart1.setDescription(null);
    }

    private String obtenerNombreDelMes(int numeroMes){
        // 0 = Enero
        return meses[numeroMes];
    }

    private int obtenerAnnoUltimoElementoListaMantenimientos(){
        if (listaMantenimientos.size()>0) {
            Mantenimiento mant = listaMantenimientos.get(listaMantenimientos.size() - 1);
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(mant.getFecha());
            return fecha.get(Calendar.YEAR);
        }
        return 0;
    }

    private int obtenerAnnoUltimoElementoListaRepostajes(){
        if (listaRepostajes.size()>0) {
            Repostaje rep = listaRepostajes.get(listaRepostajes.size() - 1);
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(rep.getFecha());
            return fecha.get(Calendar.YEAR);
        }
        return 0;
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
