package com.traineragenda.Usuarios;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.traineragenda.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador de Usuario
 */
public class UsuarioAdapter extends ArrayAdapter<Usuario> {
    public UsuarioAdapter(Context context, List<Usuario> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            convertView = inflater.inflate(
                    R.layout.list_item_usuarios,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.fecha = (TextView) convertView.findViewById(R.id.tv_fecha);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Usuario lead = getItem(position);

        // Setup.
        holder.title.setText(lead.getNombre()+" "+ lead.getApellido());

        if(lead.getUlt_planilla() != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yy HH:mm");
            String strDt = simpleDate.format(lead.getUlt_planilla());
            holder.fecha.setText(strDt);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView fecha;
    }
}