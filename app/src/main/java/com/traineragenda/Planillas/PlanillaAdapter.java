package com.traineragenda.Planillas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adaptador de Usuario
 */
public class PlanillaAdapter extends ArrayAdapter<Planilla> {
    public PlanillaAdapter(Context context, List<Planilla> objects) {
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
                    R.layout.list_item_planillas,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Planilla lead = getItem(position);


        if(lead.getFecha() != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yy");
            String strDt = simpleDate.format(lead.getFecha());
            holder.title.setText(strDt);
        }


        return convertView;
    }

    static class ViewHolder {
        TextView title;
    }
}