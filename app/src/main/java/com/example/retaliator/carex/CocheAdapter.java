package com.example.retaliator.carex;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Nadie on 05/03/2018.
 */

public class CocheAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Coche> items;

    public CocheAdapter(Context context, ArrayList<Coche> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount(){
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
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
            convertView = inflater.inflate(R.layout.item_coche_list, parent, false);
        }

        ImageView icono = (ImageView) convertView.findViewById(R.id.imageViewCarIcon);
        Coche item = this.items.get(position);
        icono.setImageResource(item.getIcono());
        icono.setColorFilter(item.getColor());
        TextView nombre = (TextView) convertView.findViewById(R.id.tvCarName);
        nombre.setText(item.getNombre());

        return convertView;
    }

}
