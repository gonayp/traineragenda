package com.traineragenda.Planillas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.ActividadesMenuActivity;
import com.traineragenda.R;

public class ListadoPlanillasActivity extends AppCompatActivity {

    private TextView tv_descripton;
    private String usuario_id;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_planillas);

        tv_descripton = findViewById(R.id.tv_descripton);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        usuario_id = mAuth.getUid();

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef_planillas = fdb.getReference("Planillas");

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef_planillas.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){


                //borramos el repositorio de actividades
                PlanillaRepository.getInstance().deletePlanilla();


                Long cantidad = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_planillas = dataSnapshot.getChildren();

                tv_descripton.setVisibility(View.VISIBLE);
                if(cantidad > 0){
                    tv_descripton.setVisibility(View.GONE);
                }


                for (int i=0; i < cantidad; i++) {
                    Planilla a = list_planillas.iterator().next().getValue(Planilla.class);

                    if(a.getUsuario_id().equals( usuario_id)) {

                        if(a.getTerminado())//solo finalizadas
                            PlanillaRepository.getInstance().addPlanilla(a);

                    }

                }


                PlanillaFragment leadsFragment = (PlanillaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.planillas_container);

                if (leadsFragment == null) {
                    leadsFragment = PlanillaFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.planillas_container, leadsFragment)
                            .commit();
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }


        });
    }



    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ActividadesMenuActivity.class);
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
