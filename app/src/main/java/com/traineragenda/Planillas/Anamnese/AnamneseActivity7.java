package com.traineragenda.Planillas.Anamnese;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;

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

public class AnamneseActivity7 extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private Spinner spinner_colesterol;
    private EditText et_colesterol;
    private Spinner spinner_cardiaca;
    private EditText et_cardiaco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese7);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_colesterol = findViewById(R.id.et_colesterol);
        et_cardiaco = findViewById(R.id.et_cardiaco);
        spinner_colesterol = findViewById(R.id.spinner_colesterol);
        spinner_cardiaca = findViewById(R.id.spinner_cardiaca);


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

                if(u.getClinicaCardiopatia()!= null) {
                    spinner_cardiaca.setSelection(u.getClinicaCardiopatia().intValue());
                }
                if(u.getClinicaColesterol() != null) {
                    spinner_colesterol.setSelection(u.getClinicaColesterol().intValue());
                }
                if(u.getClinicaCardiopatiaCual() != null){
                    et_cardiaco.setText(u.getClinicaCardiopatiaCual());
                }
                if(u.getClinicaColesterolCual() != null){
                    et_colesterol.setText(u.getClinicaColesterolCual());
                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }






    public void onActionVolver(View view) {
        Intent intent = new Intent(this, AnamneseActivity6.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void onActionRegistrar(View view) {


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());

        mRef.child("clinicaCardiopatia").setValue(spinner_cardiaca.getSelectedItemId());
        mRef.child("clinicaCardiopatiaCual").setValue(et_cardiaco.getText().toString());
        mRef.child("clinicaColesterol").setValue(spinner_colesterol.getSelectedItemId() );
        mRef.child("clinicaColesterolCual").setValue(et_colesterol.getText().toString());


        Intent intent = new Intent(this, AnamneseActivity8.class);
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
