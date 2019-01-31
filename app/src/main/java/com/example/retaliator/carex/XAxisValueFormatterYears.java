package com.example.retaliator.carex;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

public class XAxisValueFormatterYears implements IAxisValueFormatter {
    private String[] mValues;

    public XAxisValueFormatterYears(String[] values) {
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


        if (((int) value > anno) || ((int) value <= (anno - mValues.length))) {
            //Log.d("Return = ", "--");
            return "--";
        }
        else{
            //Log.d("Return = ", mValues[anno - (int) value]);
            return mValues[anno - (int) value];
            //return "--";
        }
    }
}
