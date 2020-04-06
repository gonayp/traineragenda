package com.traineragenda.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.R;

/**
 * Vista para los leads del CRM
 */
public class UsuarioFragment extends Fragment {
    private ListView mLeadsList;
    private UsuarioAdapter mLeadsAdapter;
    public static final String MESSAGE_titulo = "Titulo";


    public UsuarioFragment() {
            // Required empty public constructor
    }

    public static UsuarioFragment newInstance() {
        UsuarioFragment fragment = new UsuarioFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_usuario, container, false);


        // Instancia del ListView.
        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new UsuarioAdapter(getActivity(),
                UsuarioRepository.getInstance().getLeads());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        // Eventos
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Usuario currentLead = mLeadsAdapter.getItem(position);
                Boolean planilla = currentLead.getPlanillas();
                String id = currentLead.getUsuarioID();


                if(planilla) {
                    Intent intent = new Intent(getContext(), ListaPlanillasXUsuarioActivity.class);
                    intent.putExtra("mesg_usuario_id", id);
                    startActivity(intent);
                }





            }
        });

        setHasOptionsMenu(true);
        return root;
    }


}

