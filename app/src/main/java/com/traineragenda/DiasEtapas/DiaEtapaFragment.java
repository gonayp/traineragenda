package com.traineragenda.DiasEtapas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.traineragenda.R;

/**
 * Vista para los leads del CRM
 */
public class DiaEtapaFragment extends Fragment {
    private ListView mLeadsList;
    private DiaEtapaAdapter mLeadsAdapter;
    public static final String MESSAGE_titulo = "Titulo";
    public static final String MESSAGE_company = "Company";
    public static final String MESSAGE_descripcion = "Descripcion";
    public static final String MESSAGE_imagen = "imagen";


    public DiaEtapaFragment() {
// Required empty public constructor
    }

    public static DiaEtapaFragment newInstance() {
        DiaEtapaFragment fragment = new DiaEtapaFragment();
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
        View root = inflater.inflate(R.layout.fragment_dias_etapas, container, false);




        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new DiaEtapaAdapter(getActivity(),
                DiaEtapaRepository.getInstance().getLeads());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        // Eventos
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DiaEtapa currentLead = mLeadsAdapter.getItem(position);

                Long etapa_id = currentLead.getEtapa_id();
                Long dia_id = currentLead.getDia_id();
                Long planilla_id = currentLead.getPlanilla_id();

                Intent intent = new Intent(getContext(), DetalleDiaEtapaActivity.class);
                intent.putExtra("mesg_etapa_id", etapa_id);
                intent.putExtra("mesg_dia_id", dia_id);
                intent.putExtra("mesg_planilla_id", planilla_id);
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }


}

