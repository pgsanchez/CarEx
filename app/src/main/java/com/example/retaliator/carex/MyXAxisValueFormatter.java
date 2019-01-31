package com.example.retaliator.carex;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Juan Nadie on 10/06/2018.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        Integer i = (int) value;
        //Log.d("Formatter size = ", i.toString());
        if (((int) value < 0) || ((int) value >= mValues.length)) {
            //Log.d("Return = ", "--");
            return "--";
        }
        else{
            //Log.d("Return = ", i.toString());
            return mValues[(int) value];
        }
    }
}
