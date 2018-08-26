package com.example.retaliator.carex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.retaliator.carex.CarExContract.*;

/**
 * Created by Juan Nadie on 12/03/2017.
 */

public class CarExHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CarExDB.db";

    public CarExHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla "repostajes"
        db.execSQL("CREATE TABLE " + RepostajeEntry.TABLE_NAME + " ("
                + RepostajeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RepostajeEntry.COCHE + " INTEGER,"
                + RepostajeEntry.FECHA + " DATE,"
                + RepostajeEntry.KMTOTALES + " INTEGER NOT NULL,"
                + RepostajeEntry.LUGAR + " TEXT,"
                + RepostajeEntry.IMPORTE + " REAL,"
                + RepostajeEntry.LITROS + " REAL,"
                + RepostajeEntry.PRECIO + " REAL)");

        // Crear la tabla "mantenimientos"
        db.execSQL("CREATE TABLE " + MantenimientoEntry.TABLE_NAME + " ("
                + MantenimientoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MantenimientoEntry.COCHE + " INTEGER,"
                + MantenimientoEntry.FECHA + " DATE,"
                + MantenimientoEntry.KMTOTALES + " INTEGER NOT NULL,"
                + MantenimientoEntry.LUGAR + " TEXT,"
                + MantenimientoEntry.IMPORTE + " REAL,"
                + MantenimientoEntry.TALLER + " TEXT,"
                + MantenimientoEntry.REPARACION + " TEXT)");

        // Crear la tabla "coches"
        db.execSQL("CREATE TABLE " + CochesEntry.TABLE_NAME + " ("
                + CochesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CochesEntry.NOMBRE + " TEXT NOT NULL UNIQUE,"
                + CochesEntry.COLOR + " TEXT,"
                + CochesEntry.ICONO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
