package com.traineragenda.Actividades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.traineragenda.R;

import java.util.List;

/**
 * Adaptador de Actividades
 */
public class ActividadAdapter extends ArrayAdapter<Actividad> {
    public ActividadAdapter(Context context, List<Actividad> objects) {
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
                    R.layout.list_item_actividad,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            //holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.company = (TextView) convertView.findViewById(R.id.tv_company);
            holder.description = (TextView) convertView.findViewById(R.id.tv_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Actividad lead = getItem(position);

        // Setup.
        // holder.name.setText(lead.getName());
        holder.title.setText(lead.getTitle());
        if (lead.getCompany() != null)holder.company.setText(lead.getCompany());
        holder.description.setText(lead.getDescription());
        Glide.with(getContext()).load(lead.getImage()).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder {
        ImageView avatar;
        //TextView name;
        TextView title;
        TextView company;
        TextView description;
    }
}