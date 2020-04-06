package com.traineragenda.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.traineragenda.ActividadPlanificarNuevoActivity;
import com.traineragenda.R;

/**
 * Vista para los leads del CRM
 */
public class ActividadFragment extends Fragment {
  private ListView mLeadsList;
  private ActividadAdapter mLeadsAdapter;
  public static final String MESSAGE_titulo = "Titulo";
  public static final String MESSAGE_company = "Company";
  public static final String MESSAGE_descripcion = "Descripcion";
  public static final String MESSAGE_imagen = "imagen";


  public ActividadFragment() {
    // Required empty public constructor
  }

  public static ActividadFragment newInstance(/*parámetros*/) {
    ActividadFragment fragment = new ActividadFragment();
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
    View root = inflater.inflate(R.layout.fragment_actividad, container, false);

    //inicializar el repositorio
    //Actividad a = new Actividad("test","test","test",R.drawable.lead_photo_4);
    //ActividadesRepository.getInstance().addActividad(a);


    // Instancia del ListView.
    mLeadsList = (ListView) root.findViewById(R.id.leads_list);

    // Inicializar el adaptador con la fuente de datos.
    mLeadsAdapter = new ActividadAdapter(getActivity(),
            ActividadesRepository.getInstance().getLeads());

    //Relacionando la lista con el adaptador
    mLeadsList.setAdapter(mLeadsAdapter);

    // Eventos
    mLeadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Actividad currentLead = mLeadsAdapter.getItem(position);
        String tipo = currentLead.getTipo();

        if(tipo.equals("plantilla") ) {
          Toast.makeText(getActivity(),
                  "Iniciar configuración de nueva planilla: \n" + currentLead.getTitle(),
                  Toast.LENGTH_LONG).show();
        }
        if(tipo.equals("compromiso")) {

          Intent intent = new Intent(getActivity(), ActividadPlanificarNuevoActivity.class);
          //EditText editText = (EditText) findViewById(R.id.editText);
          String message = currentLead.getTitle();//editText.getText().toString();
          intent.putExtra(MESSAGE_titulo, message);
          intent.putExtra(MESSAGE_company, currentLead.getCompany());
          intent.putExtra(MESSAGE_descripcion, currentLead.getDescription());
          Integer i = currentLead.getImage();
          intent.putExtra(MESSAGE_imagen, i.toString());
          startActivity(intent);
        }
      }
    });

    setHasOptionsMenu(true);
    return root;
  }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_leads_list, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_delete_all) {
//            // Eliminar todos los leads
//            mLeadsAdapter.clear();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}


//
//public class ActividadFragment extends Fragment {
//
//    ListView mLeadsList;
//    ArrayAdapter<String> mLeadsAdapter;
//
//    public ActividadFragment() {
//        // Required empty public constructor
//    }
//
//    public static ActividadFragment newInstance(/*parámetros*/) {
//        ActividadFragment fragment = new ActividadFragment();
//        // Setup parámetros
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            // Gets parámetros
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_actividad, container, false);
//        mLeadsList = (ListView) root.findViewById(R.id.leads_list);
//        String[] leadsNames = {
//                "Alexander Pierrot",
//                "Carlos Lopez",
//                "Sara Bonz",
//                "Liliana Clarence",
//                "Benito Peralta",
//                "Juan Jaramillo",
//                "Christian Steps",
//                "Alexa Giraldo",
//                "Linda Murillo",
//                "Lizeth Astrada"
//        };
//
//        mLeadsAdapter = new ArrayAdapter<>(
//                getActivity(),
//                android.R.layout.simple_list_item_1,
//                leadsNames);
//
//        mLeadsList.setAdapter(mLeadsAdapter);
//
//        return root;
//    }
//}
