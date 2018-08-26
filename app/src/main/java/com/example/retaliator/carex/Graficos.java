package com.example.retaliator.carex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

public class Graficos extends AppCompatActivity {

    private String meses[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    private ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>(); // Lista de Respostajes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        if (getIntent() != null){
            listaRepostajes = (ArrayList<Repostaje>)getIntent().getExtras().getSerializable("listaRepostajes");
        }


        //Collections.reverse(listaRepostajes);

        dibujaGraficaDeBarras();

    }

    private void dibujaGraficaDeBarras(){
        BarChart barchar1 = (BarChart) findViewById(R.id.barchart1);
        List<BarEntry> entries = new ArrayList<BarEntry>();

        Calendar mesActual = Calendar.getInstance();
        Log.i("MesActual = ", mesActual.getTime().toString() );

        float valorX = 12f;
        float valorY = 0f;

        Iterator<Repostaje> it = listaRepostajes.iterator();
        while(it.hasNext() && valorX > 0 ){
            Repostaje rep = it.next();
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(rep.getFecha());

            Log.i("MesElemento = ", fecha.getTime().toString() );
            Log.i("MesActual = ", mesActual.getTime().toString() );
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

    private void dibujaGraficaDeLineas(){
        LineChart lineChart1 = (LineChart) findViewById(R.id.linechart1);

        List<Entry> entries = new ArrayList<Entry>();



        Repostaje primerRepostaje = listaRepostajes.get(0);
        long valoractual = primerRepostaje.getFecha().getTime();
        long valoranterior = valoractual;
        valoranterior = valoranterior / (1000 * 60 * 60 * 24);

        Iterator<Repostaje> it = listaRepostajes.iterator();

        while(it.hasNext()){
            Repostaje rep = it.next();
            valoractual = rep.getFecha().getTime();
            valoractual = valoractual / (1000 * 60 * 60 * 24);
            entries.add(new Entry(valoractual - valoranterior, rep.getPrecio()));
            long x = valoractual - valoranterior;
            valoranterior = valoractual;
        }

        XAxis xAxis = lineChart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        //xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);

        LineDataSet dataSet = new LineDataSet(entries, "Label");

        LineData data = new LineData(dataSet);
        lineChart1.setData(data);
        lineChart1.invalidate();
    }

    private String obtenerNombreDelMes(int numeroMes){
        // 0 = Enero
        return meses[numeroMes];
    }

}
