package com.traineragenda.DiasPlanilla;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.traineragenda.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador de Dias de Planillas
 */
public class DiaPlanillaAdapter extends ArrayAdapter<DiaPlanilla> {
    public DiaPlanillaAdapter(Context context, List<DiaPlanilla> objects) {
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
                    R.layout.list_item_dias,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.fecha = (TextView) convertView.findViewById(R.id.tv_title);
            holder.distancia = (TextView) convertView.findViewById(R.id.tv_distancia);
            holder.entrenamiento = (TextView) convertView.findViewById(R.id.tv_treino);
            holder.tipo = (TextView) convertView.findViewById(R.id.tv_tipo);
            holder.tiempo = (TextView) convertView.findViewById(R.id.tv_tiempo);
            holder.pace = (TextView) convertView.findViewById(R.id.tv_pace);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        DiaPlanilla lead = getItem(position);

        SimpleDateFormat simpleDate =  new SimpleDateFormat("E dd MMM yyyy");
        String strDt = simpleDate.format(lead.getDia());
        holder.fecha.setText(strDt);

        holder.distancia.setText(lead.getDistancia().toString());
        holder.entrenamiento.setText(lead.getEntrenamiento());
        holder.tiempo.setText(lead.getTiempo());
        holder.pace.setText(lead.getPace());

        String[] mTestArray;
        mTestArray =   convertView.getResources().getStringArray(R.array.array_tipo);
        holder.tipo.setText(mTestArray[lead.getTipo().intValue()]);


        //Cargar Imagen dependiendo del estado
        int image = 0;
        if(lead.getTerminado() != null) {
            if (lead.getTerminado()== 1)
                image = R.drawable.baseline_check_box_black_18dp;

        }

        Glide.with(getContext()).load(image).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder {

        TextView fecha;
        TextView entrenamiento;
        TextView distancia;
        TextView tipo;
        TextView tiempo;
        TextView pace;
        ImageView avatar;
    }
}