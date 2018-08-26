package com.example.retaliator.carex;

import android.provider.BaseColumns;

/**
 * Created by Juan Nadie on 11/03/2017.
 */

public final class CarExContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CarExContract() {}

    /* Inner class that defines the table contents */
    public static abstract class RepostajeEntry implements BaseColumns {
        // Nombre de la tabla
        public static final String TABLE_NAME ="repostajes";
        // Campos
        public static final String COCHE = "coche";
        public static final String KMTOTALES = "kmTotales";
        public static final String LITROS = "litros";
        public static final String IMPORTE = "importe";
        public static final String PRECIO = "precio";
        public static final String LUGAR = "lugar";
        public static final String FECHA = "fecha";
    }

    public static abstract class MantenimientoEntry implements BaseColumns {
        // Nombre de la tabla
        public static final String TABLE_NAME ="mantenimientos";
        // Campos
        public static final String COCHE = "coche";
        public static final String KMTOTALES = "kmTotales";
        public static final String IMPORTE = "importe";
        public static final String LUGAR = "lugar";
        public static final String TALLER = "taller";
        public static final String REPARACION = "reparacion";
        public static final String FECHA = "fecha";
    }

    public static abstract class CochesEntry implements BaseColumns {
        // Nombre de la tabla
        public static final String TABLE_NAME ="coches";
        // Campos
        public static final String NOMBRE = "nombre";
        public static final String COLOR = "color";
        public static final String ICONO = "icono";
    }

}
