package com.example.retaliator.carex;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

public class XAxisValueFormatterDays implements IAxisValueFormatter {
    private String[] mValues;

    public XAxisValueFormatterDays(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Calendar annoActual = Calendar.getInstance();
        int anno = annoActual.get(Calendar.YEAR);
        /*Log.d("mValues.length = ", String.valueOf(mValues.length));
        Log.d("Anno actual = ", String.valueOf(anno));
        Log.d("value = ", String.valueOf((int)value));*/


        anno = obtenerAnnoDesdeDia((int)value);
        for (int i = 0; i < mValues.length; i++){
            if (anno == Integer.parseInt(mValues[i])) {
                //Log.d("Return = ", mValues[i]);
                return mValues[i];
            }
        }

        //Log.d("Return = ", mValues[0]);
        return mValues[0];
    }

    //Función para obtener el año al que pertenece un día, contando desde el año más bajo
    // del array mValues
    int obtenerAnnoDesdeDia(int dias){
        if (mValues.length <= 0) {
            Calendar fechaActual = Calendar.getInstance();
            return fechaActual.get(Calendar.YEAR);
        }

        Calendar fechaBase = Calendar.getInstance();
        // damos valor a fechaBase con el año más bajo de la lista, que será el último elemento.
        fechaBase.set(Integer.parseInt(mValues[mValues.length -1]),11,31);

        /*int numeroDias = 0;
        numeroDias += fechaBase.get(Calendar.DAY_OF_YEAR);
        if (dias < numeroDias)
            return fechaBase.get(Calendar.YEAR);*/

        Calendar diasDeAnno = Calendar.getInstance();
        int numeroDias = 0;
        for (int i = Integer.parseInt(mValues[mValues.length -1]); i <= Integer.parseInt(mValues[0]); i++){
            // damos valor a fechaBase con el 31/12 del año más bajo de la lista
            fechaBase.set(i,11,31);
            numeroDias += fechaBase.get(Calendar.DAY_OF_YEAR);
            if (dias < numeroDias)
                return fechaBase.get(Calendar.YEAR);
        }

        return fechaBase.get(Calendar.YEAR);
    }
}
