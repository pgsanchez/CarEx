package com.example.retaliator.carex;

import java.util.Date;

/**
 * Created by Juan Nadie on 11/03/2017.
 */

public class Mantenimiento extends Actividad{
    // Atributos de la clase
    private String taller ;
    private String reparacion;
    private String tipo_gasto;

    public Mantenimiento() {
        super();
        taller = "";
        reparacion = "";
        tipo_gasto = "Mantenimiento"; // Por defecto, "Mantenimiento"
    }

    public String getTaller() {
        return taller;
    }

    public void setTaller(String taller) {
        this.taller = taller;
    }

    public String getReparacion() {
        return reparacion;
    }

    public void setReparacion(String reparacion) {
        this.reparacion = reparacion;
    }

    public String getTipo_gasto() {
        return tipo_gasto;
    }

    public void setTipo_gasto(String tipo_gasto) {
        this.tipo_gasto = tipo_gasto;
    }
}
