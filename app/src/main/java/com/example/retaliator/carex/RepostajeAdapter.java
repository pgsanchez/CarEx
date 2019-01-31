package com.example.retaliator.carex;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juan Nadie on 05/04/2017.
 */

public class RepostajeAdapter extends BaseAdapter {
    private Context context;
    private List<Repostaje> listaRepostajes;
    private ArrayList<Coche> listaCoches;

    public RepostajeAdapter(Context context, List<Repostaje> items, ArrayList<Coche> listaCoches) {
        this.context = context;
        this.listaRepostajes = items;
        this.listaCoches = listaCoches;
    }

    @Override
    public int getCount(){
        return this.listaRepostajes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaRepostajes.get(position);
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
            convertView = inflater.inflate(R.layout.item_repostaje_list, parent, false);
        }

        ImageView icono = (ImageView) convertView.findViewById(R.id.imageView);
        TextView fecha  = (TextView) convertView.findViewById(R.id.tv_fecha);
        TextView importe = (TextView) convertView.findViewById(R.id.tv_importe);
        TextView km = (TextView) convertView.findViewById(R.id.tv_km);
        TextView lugar = (TextView) convertView.findViewById(R.id.tv_lugar);

        Repostaje item = this.listaRepostajes.get(position);
        Coche coche = getCocheById(item.getCoche());
        icono.setImageResource(coche.getIcono());
        icono.setColorFilter(coche.getColor());

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(formatoFecha.format(item.getFecha()));

        importe.setText(String.valueOf(item.getImporte()) + "€" + " / " + String.valueOf(item.getPrecio()) + "€/l");

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        DecimalFormat formatea = new DecimalFormat("###,###.##", otherSymbols);

        String textoKm = String.valueOf((formatea.format(item.getKmParciales()))) + " / " + String.valueOf(formatea.format(item.getKmTotales())) + " kms";
        //String textoKm = String.valueOf((item.getKmParciales())) + " / " + String.valueOf(item.getKmTotales() + " kms");
        km.setText(textoKm);
        lugar.setText((item.getLugar()));

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
