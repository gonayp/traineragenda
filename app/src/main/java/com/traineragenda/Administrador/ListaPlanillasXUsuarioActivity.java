package com.traineragenda.Administrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity1;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.Planillas.PlanillaFragment;
import com.traineragenda.Planillas.PlanillaRepository;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

public class ListaPlanillasXUsuarioActivity extends AppCompatActivity {

    private String usuario_id;

    private TextView tv_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planillas_xusuario);

        tv_titulo = findViewById(R.id.tv_titulo);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        usuario_id = intent.getStringExtra("mesg_usuario_id");

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef_Usuario = fdb.getReference("Users");

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef_Usuario.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                DataSnapshot d = dataSnapshot.child(usuario_id);
                Usuario u = d.getValue(Usuario.class);

                tv_titulo.setText("Planilhas de " + u.getNombre()+ " "+ u.getApellido());
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });


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


                for (int i=0; i < cantidad; i++) {
                    Planilla a = list_planillas.iterator().next().getValue(Planilla.class);

                    if(a.getUsuario_id().equals( usuario_id)) {

                        if(!a.getTerminado())//No finalizadas
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
        Intent intent = new Intent(this, ClientesPlanillasActivity.class);
        startActivity(intent);

    }

    public void onActionNueva(View view) {

        Intent intent = new Intent(this, ConfeccionarPlanillaActivity1.class);
        intent.putExtra("mesg_usuario_id", usuario_id);
        startActivity(intent);

    }

    public void onActionFinalizadas(View view) {

        Intent intent = new Intent(this, ListaPlanillasXUsuarioFinalizadasActivity.class);
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
