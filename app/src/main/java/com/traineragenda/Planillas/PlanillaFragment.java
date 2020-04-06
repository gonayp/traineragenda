package com.traineragenda.Planillas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity1a;
import com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla.VerPlanillaActivity1;
import com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla.VerPlanillaEspecialActivity;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.otros.DatabaseUtil;
import com.traineragenda.otros.MetodosGlobales;

/**
 * Vista para los leads del CRM
 */
public class PlanillaFragment extends Fragment {
    private ListView mLeadsList;
    private PlanillaAdapter mLeadsAdapter;
    public static final String MESSAGE_titulo = "Titulo";
    String nombres;
    Double edad;

    public PlanillaFragment() {
            // Required empty public constructor
    }

    public static PlanillaFragment newInstance() {
        PlanillaFragment fragment = new PlanillaFragment();
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
        View root = inflater.inflate(R.layout.fragment_planilla, container, false);



        // Instancia del ListView.
        mLeadsList = (ListView) root.findViewById(R.id.leads_list);

        // Inicializar el adaptador con la fuente de datos.
        mLeadsAdapter = new PlanillaAdapter(getActivity(),
                PlanillaRepository.getInstance().getLeads());

        //Relacionando la lista con el adaptador
        mLeadsList.setAdapter(mLeadsAdapter);

        // Eventos
        mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Planilla currentLead = mLeadsAdapter.getItem(position);

                Long id = currentLead.getPlanilla_id();
                String usuario = currentLead.getUsuario_id();



                //MEtodo para buscar un dia de planilla de la lista
                DatabaseUtil.findUsuarioById(usuario, new DatabaseUtil.ListenerUsuario() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onUsuarioRetrieved(Usuario u) {
                        Usuario usuario = u;
                        nombres = usuario.getNombre()+" "+usuario.getApellido();
                        edad = usuario.getAge();

                    }
                });

                if(currentLead.getTerminado()){
                    Intent intent = new Intent(getContext(), VerPlanillaEspecialActivity.class);//VerPlanillaActivity1.class);
                    intent.putExtra("mesg_planilla_id", id);
                    intent.putExtra("mesg_usuario_id", usuario);
                    startActivity(intent);
                    /*
                    MetodosGlobales myGlog;
                    myGlog = new MetodosGlobales(getContext(),currentLead,nombres,edad);
                    myGlog.createPdfPlanilla();

                    Intent intent = new Intent(getContext(), VerPlanillaPDFActivity.class);
                    intent.putExtra("mesg_planilla_id", id);
                    intent.putExtra("mesg_usuario_id", usuario);
                    startActivity(intent);

                     */
                }
                else {
                    Intent intent = new Intent(getContext(), ConfeccionarPlanillaActivity1a.class);
                    intent.putExtra("mesg_planilla_id", id);
                    startActivity(intent);
                }


            }
        });

        setHasOptionsMenu(true);
        return root;
    }


}

