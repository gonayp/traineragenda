package com.traineragenda.Planillas.ConfeccionarPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;

public class ConfeccionarPlanillaActivity2 extends AppCompatActivity {


    private Long planilla_id;


    private EditText et_frecuencia;
    private EditText et_periodo;
    private Spinner spinner_periocidad;

    private Long cantidad_planillas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla2);


        et_frecuencia = findViewById(R.id.et_frecuencia);
        et_periodo = findViewById(R.id.et_periodo);
        spinner_periocidad = findViewById(R.id.spinner_periocidad);


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



                if(p.getDatosFrecuencia() != null) {
                    et_frecuencia.setText(p.getDatosFrecuencia());
                }
                if(p.getDatosPeriodo() != null) {
                    et_periodo.setText(p.getDatosPeriodo());
                }
                if(p.getDatosPeriodicidad() != null){
                    spinner_periocidad.setSelection(p.getDatosPeriodicidad().intValue());
                }

            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });


    }

    public void onActionRegistrar(View view) {

        //Grabar planilla
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_"+planilla_id);

        mRef.child("datosFrecuencia").setValue(et_frecuencia.getText().toString());
        mRef.child("datosPeriodo").setValue(et_periodo.getText().toString());
        mRef.child("datosPeriodicidad").setValue(spinner_periocidad.getSelectedItemId());

        Intent intent = new Intent(this, ConfeccionarPlanillaActivity3.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity1a.class);
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
