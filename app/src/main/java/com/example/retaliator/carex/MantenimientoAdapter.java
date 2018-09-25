package com.example.retaliator.carex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Nadie on 10/12/2017.
 */

public class MantenimientoAdapter extends BaseAdapter {
    private Context context;
    private List<Mantenimiento> listaMantenimientos;
    private ArrayList<Coche> listaCoches;

    public MantenimientoAdapter(Context context, List<Mantenimiento> items, ArrayList<Coche> listaCoches) {
        this.context = context;
        this.listaMantenimientos = items;
        this.listaCoches = listaCoches;
    }

    @Override
    public int getCount() {
        return this.listaMantenimientos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaMantenimientos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // Create a new view into the list.
            convertView = inflater.inflate(R.layout.item_mantenimiento_list, parent, false);
        }

        // Esta parte es la que hay que modificar con el layout de los elementos que hay que crear
        // desde aquí-----------------

        ImageView icono = (ImageView) convertView.findViewById(R.id.imageView);
        TextView fecha  = (TextView) convertView.findViewById(R.id.tv_fecha);
        TextView importe = (TextView) convertView.findViewById(R.id.tv_importe);
        TextView km = (TextView) convertView.findViewById(R.id.tv_km);
        TextView lugar = (TextView) convertView.findViewById(R.id.tv_lugar);
        TextView reparacion = (TextView) convertView.findViewById(R.id.tv_reparacion);


        Mantenimiento item = this.listaMantenimientos.get(position);
        Coche coche = getCocheById(item.getCoche());
        icono.setImageResource(coche.getIcono());
        icono.setColorFilter(coche.getColor());

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(formatoFecha.format(item.getFecha()));
        importe.setText(String.valueOf(item.getImporte()) + "€");
        String textoKm = String.valueOf((item.getKmParciales())) + " / " + String.valueOf(item.getKmTotales() + " kms");
        km.setText(textoKm);
        lugar.setText((item.getLugar()));
        reparacion.setText(item.getReparacion());

        // hasta aquí--------------------------

        return convertView;
    }

    private Coche getCocheById(int id_coche){
        Coche c = new Coche();
        for (int i = 0; i < listaCoches.size(); i++) {
            c = (Coche)listaCoches.get(i);
            if (c.getId_coche() == id_coche)
                return c;
        }
        return c;
    }
}
