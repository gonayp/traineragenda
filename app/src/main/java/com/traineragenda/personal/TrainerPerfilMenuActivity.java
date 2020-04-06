package com.traineragenda.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.Compromisos.CompromisoFragment;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrainerPerfilMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mTextoField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_perfil_menu);


        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Compromisos");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mTextoField = findViewById(R.id.texto_informativo);



//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                Boolean muestra_actividades = false;
                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();


                Long cantidad = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_compromisos = dataSnapshot.getChildren();

                for (int i=0; i < cantidad; i++) {
                    Compromiso a = list_compromisos.iterator().next().getValue(Compromiso.class);
//                    Log.d("Tareas_Personal:", "****************************************************");
//                    Log.d("Tareas_Personal:", a.toString());
                    Usuario u = a.getEntrenador();
                    if(u != null) {
                        Log.d("Tareas_Personal:", u.getUsuarioID()+" == "+ mAuth.getUid());
                        if (u.getUsuarioID().equals(mAuth.getUid())) {
                            //a = new Actividad(a3.getName(),a3.getTitle(),a3.getCompany(),a3.getDescription(),a3.getImage());
                            Date fecha_actividad = a.getFecha();
                            Calendar calendar = Calendar.getInstance(Locale.US);
                            Date fecha_ahora = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                            Log.d("Tareas_Personal:", "diferencia de fecha: "+fecha_actividad.compareTo(fecha_ahora));
                            if (fecha_actividad.compareTo(fecha_ahora) == 1) {//si la fecha del evento es mayor a la actual
                                CompromisoRepository.getInstance().addCompromiso(a);
                                muestra_actividades = true;
                            }
                            else{
                                if(a.getEstado().equals("pendiente")){//Si no es un evento proximo pero esta pendiente
                                    CompromisoRepository.getInstance().addCompromiso(a);
                                    muestra_actividades = true;
                                }
                            }
                        }
                    }
                }

                if(muestra_actividades) mTextoField.setVisibility(View.GONE);
                else mTextoField.setVisibility(View.VISIBLE);

                CompromisoFragment leadsFragment = (CompromisoFragment)
                        getSupportFragmentManager().findFragmentById(R.id.compromiso_container);

                if (leadsFragment == null) {
                    leadsFragment = CompromisoFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.compromiso_container, leadsFragment)
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

        Intent intent = new Intent(this, TrainerMenuActivity.class);
        startActivity(intent);

    }

    public void onActionConta(View view) {

        Intent intent = new Intent(this, TreinerContactivity.class);
        startActivity(intent);

    }

    public void onActionPerfil(View view) {

        Intent intent = new Intent(this, TrainerPerfilActivity.class);
        startActivity(intent);

    }

    public void onActionGanancias(View view) {

        Intent intent = new Intent(this, TrainerGanhosActivity.class);
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
