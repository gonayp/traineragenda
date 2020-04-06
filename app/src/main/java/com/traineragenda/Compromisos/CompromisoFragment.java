package com.traineragenda.Compromisos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.traineragenda.CompromisoTerminadoActivity;
import com.traineragenda.MainMapActivity;
import com.traineragenda.R;

/**
 * Vista para los leads del CRM
 */
public class CompromisoFragment extends Fragment {
    private ListView mLeadsList;
    private CompromisoAdapter mLeadsAdapter;
    public static final String MESSAGE_titulo = "Titulo";
    public static final String MESSAGE_company = "Company";
    public static final String MESSAGE_descripcion = "Descripcion";
    public static final String MESSAGE_imagen = "imagen";


    public CompromisoFragment() {
// Required empty public constructor
    }

    public static CompromisoFragment newInstance(/*parámetros*/) {
        CompromisoFragment fragment = new CompromisoFragment();
// Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_compromiso, container, false);

//inicializar el repositorio
//Actividad a = new Actividad("test","test","test",R.drawable.lead_photo_4);
//ActividadesRepository.getInstance().addActividad(a);


// Instancia del ListView.
        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

// Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new CompromisoAdapter(getActivity(),
                CompromisoRepository.getInstance().getLeads());

//Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

// Eventos
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Compromiso currentLead = mLeadsAdapter.getItem(position);
                String tipo = currentLead.getTipo();
                Long id = currentLead.getCompromisoID();
//                Toast.makeText(getActivity(),
//                        "Ver detalle de compromiso",
//                        Toast.LENGTH_LONG).show();
                if(currentLead.getEstado().equals("pendiente")) {
                    Intent intent = new Intent(getContext(), CompromisoActivity.class);
                    intent.putExtra("mesg_compromiso_id", id);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getContext(), CompromisoTerminadoActivity.class);
                    intent.putExtra("mesg_compromiso_id", id);
                    startActivity(intent);
                }

            }
        });

        setHasOptionsMenu(true);
        return root;
    }


}

