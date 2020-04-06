package com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.DiasPlanilla.DiaPlanillaFijoFragment;
import com.traineragenda.DiasPlanilla.DiaPlanillaRepository;
import com.traineragenda.Planillas.ListadoPlanillasActivity;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;
import com.traineragenda.otros.DatabaseUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VerPlanillaEspecialActivity extends AppCompatActivity {

    private Long planilla_id;
    private Long total_dias;
    private String usuario_id;

    private String usuario_actual;

    private FirebaseAuth mAuth;

    private String tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_planilla_especial);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id", 0);

        tipo = "CLIENTE";
        mAuth = FirebaseAuth.getInstance();
        usuario_actual = mAuth.getUid();

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas/Planilla_" + planilla_id );
        //Query myTopPostsQuery = fdb.getReference("Planillas/Planilla_" + planilla_id + "/diaPlanillas").orderByChild("asd");


//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.child("diaPlanillas")
                .orderByChild("dia_id")
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //borramos el repositorio de actividades
                DiaPlanillaRepository.getInstance().deleteDiaPlanilla();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    DiaPlanilla a = child.getValue(DiaPlanilla.class);
                    DiaPlanillaRepository.getInstance().addDiaPlanilla(a);
                }

                total_dias = dataSnapshot.getChildrenCount();
                /*Iterable<DataSnapshot> list_dias = dataSnapshot.getChildren();


                for (int i = 0; i < total_dias; i++) {
                    DiaPlanilla a = list_dias.iterator().next().getValue(DiaPlanilla.class);

                    DiaPlanillaRepository.getInstance().addDiaPlanilla(a);


                }*/



                DiaPlanillaFijoFragment leadsFragment = (DiaPlanillaFijoFragment)
                        getSupportFragmentManager().findFragmentById(R.id.dias_container);

                if (leadsFragment == null) {
                    leadsFragment = DiaPlanillaFijoFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.dias_container, leadsFragment)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
            }


        });

        DatabaseReference myRef_planilla = fdb.getReference("Planillas/Planilla_" + planilla_id);


        //MEtodo para buscar un dia de planilla de la lista
        DatabaseUtil.findPlanillaById(planilla_id, new DatabaseUtil.ListenerPlanilla() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPlanillaRetrieved(Planilla c) {
                Planilla planilla = c;
                usuario_id = planilla.getUsuario_id();


            }
        });

        //apuntamos al nodo que queremos leer
        DatabaseReference myRef_user_actual = fdb.getReference("Users");

        myRef_user_actual.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(usuario_actual);
                Usuario u = d.getValue(Usuario.class);

                if(u.getTipo().equals("admin")){
                    tipo = "ADMIN";
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }

    public void onActionVolver(View view) {


        if(tipo.equals("ADMIN")) {
            Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
            intent.putExtra("mesg_planilla_id", planilla_id);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ListadoPlanillasActivity.class);
            intent.putExtra("mesg_planilla_id", planilla_id);
            startActivity(intent);
        }

    }



    public void onActionFinalizar(View view) {

        if(tipo.equals("ADMIN")) {
            Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
            intent.putExtra("mesg_usuario_id", usuario_id);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ListadoPlanillasActivity.class);
            startActivity(intent);
        }

    }


    private static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }


    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}
