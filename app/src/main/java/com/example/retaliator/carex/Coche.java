package com.example.retaliator.carex;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Juan Nadie on 27/02/2018.
 */

public class Coche implements Serializable {
    private String nombre;
    private int id_coche;
    private int icono;
    private int color;

    public Coche(String nombre, int icono, int color) {
        this.nombre = nombre;
        this.icono = icono;
        this.color = color;
        id_coche = -1;
    }

    public Coche() {
        this.nombre = "";
        this.icono = R.drawable.ic_car_hatchback;
        this.color = Color.BLACK;
        id_coche = -1;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIcono() {
        return icono;
    }

    public int getId_coche() { return id_coche; }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId_coche(int id_coche) { this.id_coche = id_coche; }
}
