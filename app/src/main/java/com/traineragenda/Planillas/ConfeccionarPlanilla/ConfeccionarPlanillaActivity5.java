package com.traineragenda.Planillas.ConfeccionarPlanilla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Administrador.ListaPlanillasXUsuarioActivity;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.DiasPlanilla.DiaPlanillaFragment;
import com.traineragenda.DiasPlanilla.DiaPlanillaRepository;
import com.traineragenda.DiasPlanilla.NuevoDiaPlanillaActivity;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;
import com.traineragenda.otros.DatabaseUtil;

import java.util.Calendar;
import java.util.Date;

public class ConfeccionarPlanillaActivity5 extends AppCompatActivity {

    private Long planilla_id;
    private Long total_dias;
    private String usuario_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla5);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id", 0);


        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas/Planilla_" + planilla_id + "/diaPlanillas");

        FirebaseApp.initializeApp(this);

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //borramos el repositorio de actividades
                DiaPlanillaRepository.getInstance().deleteDiaPlanilla();


                total_dias = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_dias = dataSnapshot.getChildren();

                if (total_dias == 0) {
                    Intent intent = new Intent(ConfeccionarPlanillaActivity5.this, PedirDosFechasActivity.class);
                    intent.putExtra("mesg_planilla_id", planilla_id);
                    startActivity(intent);
                }

                for (int i = 0; i < total_dias; i++) {
                    DiaPlanilla a = list_dias.iterator().next().getValue(DiaPlanilla.class);

                    DiaPlanillaRepository.getInstance().addDiaPlanilla(a);


                }


                DiaPlanillaFragment leadsFragment = (DiaPlanillaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.dias_container);

                if (leadsFragment == null) {
                    leadsFragment = DiaPlanillaFragment.newInstance();
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

    }

    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity4.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    public void onActionNuevo(View view) {
        Intent intent = new Intent(this, NuevoDiaPlanillaActivity.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        intent.putExtra("mesg_dia_id", (total_dias+1));
        startActivity(intent);

    }

    public void onActionFinalizar(View view) {

        //Grabar planilla
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_"+planilla_id);

        mRef.child("terminado").setValue(true);

        Intent intent = new Intent(this, ListaPlanillasXUsuarioActivity.class);
        intent.putExtra("mesg_usuario_id", usuario_id);
        startActivity(intent);

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
