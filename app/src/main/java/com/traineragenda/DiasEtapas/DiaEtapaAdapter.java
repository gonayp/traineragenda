package com.traineragenda.DiasEtapas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.traineragenda.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Adaptador de etapas de Dias
 */
public class DiaEtapaAdapter extends ArrayAdapter<DiaEtapa> {
    public DiaEtapaAdapter(Context context, List<DiaEtapa> objects) {
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
                    R.layout.list_item_dias_etapas,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.tipo = (TextView) convertView.findViewById(R.id.tv_title);
            holder.duracion = (TextView) convertView.findViewById(R.id.tv_duracion);
            holder.intensidad = (TextView) convertView.findViewById(R.id.tv_intensidad);
            //holder.fc = (TextView) convertView.findViewById(R.id.tv_fc);
            //holder.velocidad = (TextView) convertView.findViewById(R.id.tv_velocidad);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        DiaEtapa lead = getItem(position);

        /*SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yy");
        String strDt = simpleDate.format(lead.getDia());
        holder.fecha.setText(strDt);*/

        holder.duracion.setText(lead.getDuracion().toString());
        holder.intensidad.setText(lead.getIntensidad());
        //holder.fc.setText(lead.getFc());
        //holder.velocidad.setText(lead.getVelocidad());

        String[] mTestArray;
        mTestArray =   convertView.getResources().getStringArray(R.array.array_tipo_etapa);
        holder.tipo.setText(mTestArray[lead.getTipo().intValue()]);


        return convertView;
    }

    static class ViewHolder {

        TextView tipo;
        TextView duracion;
        TextView intensidad;
        TextView fc;
        TextView velocidad;
        TextView terminado;

    }
}