package com.traineragenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Actividades.ActividadFragment;
import com.traineragenda.Compromisos.Compromiso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActividadPlanificarNuevoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String message_imagen;
    String message_descripcion;
    Long cantidad_compromisos;
    String message_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_planificar_nuevo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message_titulo = intent.getStringExtra(ActividadFragment.MESSAGE_titulo);
        String message_company = intent.getStringExtra(ActividadFragment.MESSAGE_company);
        message_descripcion = intent.getStringExtra(ActividadFragment.MESSAGE_descripcion);
        message_imagen = intent.getStringExtra(ActividadFragment.MESSAGE_imagen);

        // Capture the layout's TextView and set the string as its text
        TextView textView_titulo = findViewById(R.id.tv_title);
        textView_titulo.setText(message_titulo);
        TextView textView_company = findViewById(R.id.tv_company);
        textView_company.setText(message_company);
        TextView textView_descripcion = findViewById(R.id.tv_description);
        textView_descripcion.setText(message_descripcion);
        ImageView view_imagen = findViewById(R.id.iv_avatar);
        view_imagen.setImageResource(Integer.parseInt(message_imagen));

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Compromisos");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                cantidad_compromisos = dataSnapshot.getChildrenCount();

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });
    }



    /** Called when the user taps the Send button */
    public void onActionGuardar(View view) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        //Codigo para modificar una actividad o crear una nueva
        FirebaseDatabase database =  FirebaseDatabase.getInstance();
        List<String> mParticipantes = new ArrayList<String>();
        mParticipantes.add(userId);
        Long id = cantidad_compromisos+1;
        Compromiso a = new Compromiso(id,message_titulo,mParticipantes, new Date(),0.0,0.0,"ownere",Integer.parseInt(message_imagen),"pendiente",null);
        DatabaseReference mRef =  database.getReference().child("Compromisos").child("Compromiso_"+(id));
        mRef.setValue(a);

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }



}
