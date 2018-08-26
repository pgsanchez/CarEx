package com.example.retaliator.carex;

import java.util.Date;

/**
 * Created by Juan Nadie on 11/03/2017.
 */

public class Mantenimiento extends Actividad{
    // Atributos de la clase
    private String taller ;
    private String reparacion;

    public Mantenimiento() {
        super();
        taller = "";
        reparacion = "";
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

}
