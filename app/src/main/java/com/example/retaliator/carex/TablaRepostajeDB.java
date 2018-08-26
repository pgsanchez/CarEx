package com.example.retaliator.carex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Juan Nadie on 10/04/2017.
 */

public class TablaRepostajeDB {

    CarExHelper baseDatos;

    private ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();

    public TablaRepostajeDB(CarExHelper baseDatos) {
        this.baseDatos = baseDatos;
    }


    public ArrayList<Repostaje> leerRepostajesBD() {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CarExContract.RepostajeEntry._ID,
                CarExContract.RepostajeEntry.COCHE,
                CarExContract.RepostajeEntry.KMTOTALES,
                CarExContract.RepostajeEntry.LITROS,
                CarExContract.RepostajeEntry.IMPORTE,
                CarExContract.RepostajeEntry.PRECIO,
                CarExContract.RepostajeEntry.LUGAR,
                CarExContract.RepostajeEntry.FECHA
        };

        String orderBy = CarExContract.RepostajeEntry.FECHA + " ASC";
        Cursor c = db.query(
                CarExContract.RepostajeEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                orderBy                                 // The sort order
        );

        //*************************************************
        ArrayList<Repostaje> listaRepAnt = new ArrayList<Repostaje>();
        boolean cocheEncontrado = false;
        //*************************************************
        listaRepostajes.clear();
        if (c.moveToFirst()) {
            do {
                Repostaje rep2 = new Repostaje();

                rep2.setCoche(c.getInt(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.COCHE)));

                ParsePosition pos = new ParsePosition(0);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = simpledateformat.parse(c.getString(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.FECHA)), pos);
                rep2.setFecha(fecha);

                rep2.setLitros(c.getFloat(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.LITROS)));
                rep2.setPrecio(c.getFloat(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.PRECIO)));
                rep2.setImporte(c.getFloat(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.IMPORTE)));
                rep2.setKmTotales(c.getInt(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.KMTOTALES)));
                rep2.setLugar(c.getString(c.getColumnIndexOrThrow(CarExContract.RepostajeEntry.LUGAR)));

                //***************************************************
                cocheEncontrado = false;
                for (Repostaje rep: listaRepAnt) {
                    if (rep.getCoche() == rep2.getCoche()) {
                        cocheEncontrado = true;
                        rep2.setKmParciales(rep2.getKmTotales()-rep.getKmTotales());
                        listaRepAnt.remove(rep);
                        rep = rep2;
                        listaRepAnt.add(rep);
                        break;
                    }
                }

                if (!cocheEncontrado){
                    rep2.setKmParciales(0);
                    listaRepAnt.add(rep2);
                }

                //***************************************************
                listaRepostajes.add(rep2);
            } while(c.moveToNext());
        }
        else
        {

        }
        //******************************************
        Collections.reverse(listaRepostajes);
        //******************************************
        return listaRepostajes;
    }

    // Función para insertar un objeto Repostaje
    int insertarRepostajeDB(Repostaje objRepostaje)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Funcion de insertar.
        if (db.insert(CarExContract.RepostajeEntry.TABLE_NAME, null, toContentValues(objRepostaje)) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    int actualizarRepostajeDB(Repostaje newRepostaje, Repostaje oldRepostaje){
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CarExContract.RepostajeEntry.COCHE + "='" + oldRepostaje.getCoche() + "' and " + CarExContract.RepostajeEntry.KMTOTALES + "='" + oldRepostaje.getKmTotales() + "'";

        if (db.update(CarExContract.RepostajeEntry.TABLE_NAME, toContentValues(newRepostaje),whereClause, null) == -1) {
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    int borrarRepostajeDB(Repostaje objRepostaje)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CarExContract.RepostajeEntry.COCHE + "='" + objRepostaje.getCoche() + "' and " + CarExContract.RepostajeEntry.KMTOTALES + "='" + objRepostaje.getKmTotales() + "'";

        if (db.delete(CarExContract.RepostajeEntry.TABLE_NAME, whereClause, null) == -1){
            // Mostrar mensaje de error
            return -1;
        }

        return 0;
    }

    public ContentValues toContentValues(Repostaje objetoRepostaje)
    {
        ContentValues values = new ContentValues();
        values.put(CarExContract.RepostajeEntry.COCHE, objetoRepostaje.getCoche());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fechatexto = formatter.format(objetoRepostaje.getFecha());

        values.put(CarExContract.RepostajeEntry.FECHA, fechatexto);
        values.put(CarExContract.RepostajeEntry.IMPORTE, objetoRepostaje.getImporte());
        values.put(CarExContract.RepostajeEntry.KMTOTALES, objetoRepostaje.getKmTotales());
        values.put(CarExContract.RepostajeEntry.LITROS, objetoRepostaje.getLitros());
        values.put(CarExContract.RepostajeEntry.LUGAR, objetoRepostaje.getLugar());
        values.put(CarExContract.RepostajeEntry.PRECIO, objetoRepostaje.getPrecio());
        return values;
    }

}
