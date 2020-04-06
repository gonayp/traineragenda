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
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.CompromisoRepository;
import com.traineragenda.Planillas.ActividadesAsesoriaActivity;
import com.traineragenda.R;
import com.traineragenda.Usuarios.Usuario;

import java.util.Date;

public class AnamneseActivity11 extends AppCompatActivity {

    private FirebaseAuth mAuth;


    private Spinner spinner_dolores;
    private EditText et_dolores;
    private Spinner spinner_recomendacion;
    private EditText et_recomendacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anamnese11);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_dolores = findViewById(R.id.et_dolores);
        et_recomendacion = findViewById(R.id.et_recomendacion);
        spinner_dolores = findViewById(R.id.spinner_dolores);
        spinner_recomendacion = findViewById(R.id.spinner_recomendacion);


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

                if(u.getClinicaDolores()!= null) {
                    spinner_dolores.setSelection(u.getClinicaDolores().intValue());
                }
                if(u.getClinicaRecomendaciones() != null) {
                    spinner_recomendacion.setSelection(u.getClinicaRecomendaciones().intValue());
                }
                if(u.getClinicaDoloresCual() != null){
                    et_dolores.setText(u.getClinicaDoloresCual());
                }
                if(u.getClinicaRecomendacionesCual() != null){
                    et_recomendacion.setText(u.getClinicaRecomendacionesCual());
                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

    }






    public void onActionVolver(View view) {
        Intent intent = new Intent(this, AnamneseActivity10.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void onActionRegistrar(View view) {


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        //Grabar usuario
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Users").child(mAuth.getUid());

        mRef.child("clinicaDolores").setValue(spinner_dolores.getSelectedItemId());
        mRef.child("clinicaDoloresCual").setValue(et_dolores.getText().toString());
        mRef.child("clinicaRecomendaciones").setValue(spinner_recomendacion.getSelectedItemId() );
        mRef.child("clinicaRecomendacionesCual").setValue(et_recomendacion.getText().toString());

        mRef.child("planillas").setValue(true);
        mRef.child("ult_planilla").setValue(new Date());

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "Registro com sucesso! Aguarde a resposta do professor.", Toast.LENGTH_LONG);

        toast1.show();


        Intent intent = new Intent(this, ActividadesAsesoriaActivity.class);
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
