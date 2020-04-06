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

public class ConfeccionarPlanillaActivity3 extends AppCompatActivity {


    private Long planilla_id;


    private EditText et_peso;
    private EditText et_altura;
    private EditText et_imc;
    private EditText et_cooper;
    private EditText et_fc;
    private Spinner spinner_imc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla3);


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



                if(p.getDatosAltura() != null) {
                    et_altura.setText(p.getDatosAltura().toString());
                }
                if(p.getDatosCooper() != null) {
                    et_cooper.setText(p.getDatosCooper().toString());
                }


                if(p.getDatosPeso() != null) {
                    et_peso.setText(p.getDatosPeso().toString());
                }

                Long fc = 220 - p.getDatosEdad();
                et_fc.setText(fc.toString());

                Double a = Double.valueOf(p.getDatosAltura())/100;
                Long pe = p.getDatosPeso();
                Double imc = pe/(Math.pow(a,2));
                et_imc.setText(imc.toString());

                spinner_imc.setSelection(0);
                if(imc> 18.5 && imc <25){
                    spinner_imc.setSelection(1);
                }
                if(imc>= 25 && imc <30){
                    spinner_imc.setSelection(2);
                }
                if(imc>= 30 && imc <35){
                    spinner_imc.setSelection(3);
                }
                if(imc>= 35 && imc <40){
                    spinner_imc.setSelection(4);
                }
                if(imc>= 40 ){
                    spinner_imc.setSelection(5);
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

        mRef.child("datosAltura").setValue(Integer.parseInt(et_altura.getText().toString()));
        mRef.child("datosPeso").setValue(Integer.parseInt(et_peso.getText().toString()));
        mRef.child("datosIMC").setValue(Double.parseDouble(et_imc.getText().toString()));
        if(!et_cooper.getText().toString().equals(""))
            mRef.child("datosCooper").setValue(Integer.parseInt(et_cooper.getText().toString()));
        mRef.child("datosFCmax").setValue(Integer.parseInt(et_fc.getText().toString()));


        Intent intent = new Intent(this, ConfeccionarPlanillaActivity4.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity2.class);
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
