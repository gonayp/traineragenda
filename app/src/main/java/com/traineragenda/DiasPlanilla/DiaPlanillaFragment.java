package com.traineragenda.DiasPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.traineragenda.CompromisoTerminadoActivity;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Compromisos.CompromisoActivity;
import com.traineragenda.Compromisos.CompromisoAdapter;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.R;

/**
 * Vista para los leads del CRM
 */
public class DiaPlanillaFragment extends Fragment {
    private ListView mLeadsList;
    private DiaPlanillaAdapter mLeadsAdapter;
    public static final String MESSAGE_titulo = "Titulo";
    public static final String MESSAGE_company = "Company";
    public static final String MESSAGE_descripcion = "Descripcion";
    public static final String MESSAGE_imagen = "imagen";


    public DiaPlanillaFragment() {
// Required empty public constructor
    }

    public static DiaPlanillaFragment newInstance() {
        DiaPlanillaFragment fragment = new DiaPlanillaFragment();
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
        View root = inflater.inflate(R.layout.fragment_dias, container, false);




        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new DiaPlanillaAdapter(getActivity(),
                DiaPlanillaRepository.getInstance().getLeads());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        // Eventos
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DiaPlanilla currentLead = mLeadsAdapter.getItem(position);

                Long planilla_id = currentLead.getPlanilla_id();
                Long dia_id = currentLead.getDia_id();

                Intent intent = new Intent(getContext(), DetalleDiaPlanillaActivity.class);
                intent.putExtra("mesg_planilla_id", planilla_id);
                intent.putExtra("mesg_dia_id", dia_id);
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }


}

