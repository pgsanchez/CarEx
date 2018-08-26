package com.example.retaliator.carex;


import java.io.Serializable;

/**
 * Created by Juan Nadie on 11/03/2017.
 */

public class Repostaje  extends Actividad{
    // Atributos de la clase
    private float litros;
    private float precio;

    public Repostaje() {
        super();
        litros = 0;
        precio = 0;
    }

    public float getLitros() {
        return litros;
    }

    public void setLitros(float litros) {
        this.litros = litros;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    }
