package com.example.retaliator.carex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Juan Nadie on 15/05/2018.
 */

public class TablaMantenimientoDB {

    CarExHelper baseDatos;

    private ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();

    public TablaMantenimientoDB(CarExHelper baseDatos) {
        this.baseDatos = baseDatos;
    }

    public ArrayList<Mantenimiento> leerMantenimientosBD() {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        // Campos
        String[] projection = {
                CarExContract.MantenimientoEntry._ID,
                CarExContract.MantenimientoEntry.COCHE,
                CarExContract.MantenimientoEntry.KMTOTALES,
                CarExContract.MantenimientoEntry.IMPORTE,
                CarExContract.MantenimientoEntry.LUGAR,
                CarExContract.MantenimientoEntry.TALLER,
                CarExContract.MantenimientoEntry.REPARACION,
                CarExContract.MantenimientoEntry.FECHA
        };

        String orderBy = CarExContract.MantenimientoEntry.FECHA + " ASC";
        Cursor c = db.query(
                CarExContract.MantenimientoEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy                                 // The sort order
        );

        //*************************************************
        ArrayList<Mantenimiento> listaMantAnt = new ArrayList<Mantenimiento>();
        boolean cocheEncontrado = false;
        //*************************************************
        listaMantenimientos.clear();
        if (c.moveToFirst()) {
            do {
                Mantenimiento mant = new Mantenimiento();

                mant.setCoche(c.getInt((c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.COCHE))));

                ParsePosition pos = new ParsePosition(0);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = simpledateformat.parse(c.getString(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.FECHA)), pos);
                mant.setFecha(fecha);

                mant.setImporte(c.getFloat(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.IMPORTE)));
                mant.setKmTotales(c.getInt(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.KMTOTALES)));
                mant.setLugar(c.getString(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.LUGAR)));
                mant.setTaller(c.getString(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.TALLER)));
                mant.setReparacion(c.getString(c.getColumnIndexOrThrow(CarExContract.MantenimientoEntry.REPARACION)));

                //***************************************************
                cocheEncontrado = false;
                for (Mantenimiento rep: listaMantAnt) {
                    if (rep.getCoche() == mant.getCoche()) {
                        cocheEncontrado = true;
                        mant.setKmParciales(mant.getKmTotales()-rep.getKmTotales());
                        listaMantAnt.remove(rep);
                        rep = mant;
                        listaMantAnt.add(rep);
                        break;
                    }
                }

                if (!cocheEncontrado){
                    mant.setKmParciales(0);
                    listaMantAnt.add(mant);
                }

                //***************************************************
                listaMantenimientos.add(mant);
            } while(c.moveToNext());
        }
        else
        {

        }

        //******************************************
        Collections.reverse(listaMantenimientos);
        //******************************************
        return listaMantenimientos;
    }

    // Función para insertar un objeto Mantenimiento
    int insertarMantenimientoDB(Mantenimiento objMantenimiento)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Funcion de insertar.
        if (db.insert(CarExContract.MantenimientoEntry.TABLE_NAME, null, toContentValues(objMantenimiento)) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    int actualizarMantenimientoDB(Mantenimiento newMantenimiento, Mantenimiento oldMantenimiento){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CarExContract.MantenimientoEntry.COCHE + "='" + oldMantenimiento.getCoche() + "' and " + CarExContract.MantenimientoEntry.KMTOTALES + "='" + oldMantenimiento.getKmTotales() + "'";

        if (db.update(CarExContract.MantenimientoEntry.TABLE_NAME, toContentValues(newMantenimiento),whereClause, null) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    int borrarMantenimientoDB(Mantenimiento objMantenimiento)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CarExContract.MantenimientoEntry.COCHE + "='" + objMantenimiento.getCoche() + "' and " + CarExContract.MantenimientoEntry.KMTOTALES + "='" + objMantenimiento.getKmTotales() + "'";

        if (db.delete(CarExContract.MantenimientoEntry.TABLE_NAME, whereClause, null) == -1){
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    public ContentValues toContentValues(Mantenimiento objetoMantenimiento)
    {
        ContentValues values = new ContentValues();
        values.put(CarExContract.MantenimientoEntry.COCHE, objetoMantenimiento.getCoche());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fechatexto = formatter.format(objetoMantenimiento.getFecha());

        values.put(CarExContract.MantenimientoEntry.FECHA, fechatexto);
        values.put(CarExContract.MantenimientoEntry.IMPORTE, objetoMantenimiento.getImporte());
        values.put(CarExContract.MantenimientoEntry.KMTOTALES, objetoMantenimiento.getKmTotales());
        values.put(CarExContract.MantenimientoEntry.LUGAR, objetoMantenimiento.getLugar());
        values.put(CarExContract.MantenimientoEntry.TALLER, objetoMantenimiento.getTaller());
        values.put(CarExContract.MantenimientoEntry.REPARACION, objetoMantenimiento.getReparacion());

        return values;
    }

}
