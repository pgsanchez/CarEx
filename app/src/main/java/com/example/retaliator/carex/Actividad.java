package com.example.retaliator.carex;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Juan Nadie on 05/04/2017.
 */

public class Actividad implements Serializable {
    private float importe;
    private int kmTotales; // Será también el ID
    private String lugar;
    private Date fecha;
    private int coche;
    private int kmParciales; // Se calcula; no se guarda en BD

    public Actividad() {
        importe = 0;
        kmTotales = 0;
        lugar = "Prueba";
        fecha = new Date(); // Fecha actual del sistema
        coche = -1;
        kmParciales = 0;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public int getKmTotales() {
        return kmTotales;
    }

    public void setKmTotales(int kmTotales) {
        this.kmTotales = kmTotales;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCoche() {
        return coche;
    }

    public void setCoche(int coche) {
        this.coche = coche;
    }

    public int getKmParciales() {
        return kmParciales;
    }

    public void setKmParciales(int kmParciales) {
        this.kmParciales = kmParciales;
    }
}
