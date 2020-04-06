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

public class AnamneseActivity9 extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private Spinner spinner_diabetico;
    private EditText et_diabetico;
    private Spinner spinner_pulmones;
    private EditText et_pulmones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese9);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_diabetico = findViewById(R.id.et_diabetico);
        et_pulmones = findViewById(R.id.et_pulmones);
        spinner_diabetico = findViewById(R.id.spinner_diabetico);
        spinner_pulmones = findViewById(R.id.spinner_pulmones);


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

                if(u.getClinicaDiabetico()!= null) {
                    spinner_diabetico.setSelection(u.getClinicaDiabetico().intValue());
                }
                if(u.getClinicaPulmones() != null) {
                    spinner_pulmones.setSelection(u.getClinicaPulmones().intValue());
                }
                if(u.getClinicaDiabeticoCual() != null){
                    et_diabetico.setText(u.getClinicaDiabeticoCual());
                }
                if(u.getClinicaPulmonesCual() != null){
                    et_pulmones.setText(u.getClinicaPulmonesCual());
                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }






    public void onActionVolver(View view) {
        Intent intent = new Intent(this, AnamneseActivity8.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void onActionRegistrar(View view) {


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());

        mRef.child("clinicaDiabetico").setValue(spinner_diabetico.getSelectedItemId());
        mRef.child("clinicaDiabeticoCual").setValue(et_diabetico.getText().toString());
        mRef.child("clinicaPulmones").setValue(spinner_pulmones.getSelectedItemId() );
        mRef.child("clinicaPulmonesCual").setValue(et_pulmones.getText().toString());


        Intent intent = new Intent(this, AnamneseActivity10.class);
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
