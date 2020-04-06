package com.traineragenda.Planillas.Anamnese;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

public class AnamneseActivity3 extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private TextView mPeso;
    private TextView mAltura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese3);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mPeso = findViewById(R.id.fieldPeso);
        mAltura = findViewById(R.id.fieldAltura);


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

                if(u.getPeso()!= null) {
                    mPeso.setText(u.getPeso().toString());
                }
                if(u.getAltura() != null) {
                    mAltura.setText(u.getAltura().toString());
                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }






    public void onActionVolver(View view) {
        Intent intent = new Intent(this, AnamneseActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void onActionRegistrar(View view) {

        String peso = mPeso.getText().toString();
        if (TextUtils.isEmpty(peso)) {
            mPeso.setError("Required.");
            return;
        } else {
            mPeso.setError(null);
        }

        String altura = mAltura.getText().toString();
        if (TextUtils.isEmpty(altura)) {
            mAltura.setError("Required.");
            return;
        } else {
            mAltura.setError(null);
        }


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());


        mRef.child("peso").setValue(Integer.parseInt(mPeso.getText().toString()));
        mRef.child("altura").setValue(Integer.parseInt(mAltura.getText().toString()));


        Intent intent = new Intent(this, AnamneseActivity4.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


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
