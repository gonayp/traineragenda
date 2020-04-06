package com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;

public class VerPlanillaActivity3 extends AppCompatActivity {


    private Long planilla_id;


    private TextView et_peso;
    private TextView et_altura;
    private TextView et_imc;
    private TextView et_cooper;
    private TextView et_fc;
    private TextView spinner_imc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_planilla3);


        et_peso = findViewById(R.id.et_peso);
        et_altura = findViewById(R.id.et_altura);
        et_imc = findViewById(R.id.et_imc);
        et_cooper = findViewById(R.id.et_cooper);
        et_fc = findViewById(R.id.et_fc);
        spinner_imc = findViewById(R.id.spinner_imc);


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas");


//        Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
//        se reflejen en la aplicacion
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                DataSnapshot d = dataSnapshot.child("Planilla_"+planilla_id);
                Planilla p = d.getValue(Planilla.class);

                String[] mIMCArray;
                mIMCArray =   getResources().getStringArray(R.array.array_imc);

                if(p.getDatosAltura() != null) {
                    et_altura.setText(p.getDatosAltura().toString());
                }
                if(p.getDatosCooper() != null) {
                    et_cooper.setText(p.getDatosCooper().toString());
                }

                if(p.getDatosPeso() != null) {
                    et_peso.setText(p.getDatosPeso().toString());
                }

                if(p.getDatosFCmax() != null) {
                    et_fc.setText(p.getDatosFCmax().toString());
                }

                if(p.getDatosIMC() != null) {
                    et_imc.setText(p.getDatosIMC().toString());
                }


            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });


    }

    public void onActionRegistrar(View view) {



        Intent intent = new Intent(this, VerPlanillaActivity4.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, VerPlanillaActivity2.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

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
