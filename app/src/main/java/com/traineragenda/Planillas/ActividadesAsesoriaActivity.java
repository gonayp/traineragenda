package com.traineragenda.Planillas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.traineragenda.ActividadesMenuActivity;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.Planillas.Anamnese.AnamneseActivity1;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

public class ActividadesAsesoriaActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_asesoria);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Users");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                //borramos el repositorio de actividades
                CompromisoRepository.getInstance().deleteCompromisos();

                DataSnapshot d = dataSnapshot.child(mAuth.getUid());
                Usuario u = d.getValue(Usuario.class);

                if(u.getPlanillas()){
                    Intent intent = new Intent(ActividadesAsesoriaActivity.this, ListadoPlanillasActivity.class);
                    startActivity(intent);
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


    public void onActionRegistro(View view) {//AnamneseActivity1//
        Intent intent = new Intent(this, AnamneseActivity1.class);
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
