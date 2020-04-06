package com.traineragenda.Administrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

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
import com.traineragenda.Usuarios.UsuarioFragment;
import com.traineragenda.Usuarios.UsuarioRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientesPlanillasActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_planillas);


        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Users");

        FirebaseApp.initializeApp(this);

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){


                //borramos el repositorio de actividades
                UsuarioRepository.getInstance().deleteUsuarios();


                Long cantidad = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_usuarios = dataSnapshot.getChildren();


                for (int i=0; i < cantidad; i++) {
                    Usuario a = list_usuarios.iterator().next().getValue(Usuario.class);

                    if(a.getPlanillas()!= null) {
                        if (a.getPlanillas()) {
                            UsuarioRepository.getInstance().addUsuario(a);
                        }
                    }

                }


                UsuarioFragment leadsFragment = (UsuarioFragment)
                        getSupportFragmentManager().findFragmentById(R.id.usuarios_container);

                if (leadsFragment == null) {
                    leadsFragment = UsuarioFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.usuarios_container, leadsFragment)
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
        Intent intent = new Intent(this, MainAdministradorActivity.class);
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
