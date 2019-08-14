package com.example.retaliator.carex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Nadie on 27/02/2018.
 */

public class TablaCochesDB {

    CarExHelper baseDatos;

    private ArrayList<Coche> listaCoches = new ArrayList<Coche>(); // Lista de Coches

    public TablaCochesDB(CarExHelper baseDatos) {
        this.baseDatos = baseDatos;
    }

    public ArrayList<Coche> leerCochesBD(){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CarExContract.CochesEntry._ID,
                CarExContract.CochesEntry.NOMBRE,
                CarExContract.CochesEntry.COLOR,
                CarExContract.CochesEntry.ICONO
        };

        String orderBy = CarExContract.CochesEntry._ID + " DESC";
        Cursor c = db.query(
                CarExContract.CochesEntry.TABLE_NAME, // The table to query
                projection,                           // The columns to return
                null,                                 // The columns for the WHERE clause
                null,                                 // The values for the WHERE clause
                null,                                 // don't group the rows
                null,                                 // don't filter by row groups
                orderBy                                  // The sort order
        );

        listaCoches.clear();
        if (c.moveToFirst()) {
            do {
                Coche coche = new Coche();
                coche.setId_coche(c.getInt(c.getColumnIndexOrThrow(CarExContract.CochesEntry._ID)));
                coche.setNombre(c.getString(c.getColumnIndexOrThrow((CarExContract.CochesEntry.NOMBRE))));
                coche.setColor(c.getInt(c.getColumnIndexOrThrow(CarExContract.CochesEntry.COLOR)));
                coche.setIcono(c.getInt(c.getColumnIndexOrThrow(CarExContract.CochesEntry.ICONO)));

                listaCoches.add(coche);
            } while(c.moveToNext());
        }
        else{

        }

        return listaCoches;
    }

    int insertarCocheDB(Coche objCoche) {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Funcion de insertar.
        if (db.insert(CarExContract.CochesEntry.TABLE_NAME, null, toContentValues(objCoche)) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    int actualizarCocheDB(Coche newCoche, Coche oldCoche){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CarExContract.CochesEntry.NOMBRE + "='" + oldCoche.getNombre() + "'";
        // Funcion de insertar.
        ContentValues values = new ContentValues();
        values = toContentValues(newCoche);
        if (db.update(CarExContract.CochesEntry.TABLE_NAME, toContentValues(newCoche),whereClause, null) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    public ContentValues toContentValues(Coche objetoCoche)
    {
        ContentValues values = new ContentValues();
        values.put(CarExContract.CochesEntry.NOMBRE, objetoCoche.getNombre());
        values.put(CarExContract.CochesEntry.COLOR, objetoCoche.getColor());
        values.put(CarExContract.CochesEntry.ICONO, objetoCoche.getIcono());

        return values;
    }
}
