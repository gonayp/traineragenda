package com.traineragenda.Compromisos;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.traineragenda.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador de Compromisos
 */
public class CompromisoAdapter extends ArrayAdapter<Compromiso> {
    public CompromisoAdapter(Context context, List<Compromiso> objects) {
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
                    R.layout.list_item_compromiso,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            //holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.fecha = (TextView) convertView.findViewById(R.id.tv_fecha);
            holder.description = (TextView) convertView.findViewById(R.id.tv_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Lead actual.
        Compromiso lead = getItem(position);

        // Setup.
        // holder.name.setText(lead.getName());
        holder.title.setText(lead.getTitle());

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yy HH:mm");
        String strDt = simpleDate.format(lead.getFecha());
        holder.fecha.setText(strDt);
        //holder.fecha.setText(lead.getFecha().toString());

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        String direccion = "";
        try {
            addresses = geocoder.getFromLocation(lead.getLatitud(), lead.getLongitud(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            direccion = address ;
        }
        catch (IOException e){
            direccion = lead.getLatitud().toString() + " / "+ lead.getLongitud().toString();
        }
        holder.description.setText(direccion);//lead.getCompromisoID().toString()+" "+lead.getEstado());
        //Cargar Imagen dependiendo del estado
        int image = 0;
        if(lead.getEstado() != null) {
            if (lead.getEstado().equals("pendiente"))
                image = R.drawable.baseline_directions_run_black_18dp;
            if (lead.getEstado().equals("cancelado"))
                image = R.drawable.ic_dialog_close_light;
            if (lead.getEstado().equals("evaluacion"))
                image = R.drawable.baseline_how_to_reg_black_18dp;
            if (lead.getEstado().equals("terminado"))
                image = R.drawable.baseline_check_box_black_18dp;
        }

        Glide.with(getContext()).load(image).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder {
        ImageView avatar;
        //TextView name;
        TextView title;
        TextView fecha;
        TextView description;
    }
}