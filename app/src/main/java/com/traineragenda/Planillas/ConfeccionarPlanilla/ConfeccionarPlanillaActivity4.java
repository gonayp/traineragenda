package com.traineragenda.Planillas.ConfeccionarPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;

public class ConfeccionarPlanillaActivity4 extends AppCompatActivity {


    private Long planilla_id;


    private TextView et_braco;
    private TextView et_cintura;
    private TextView et_abdominal;
    private TextView et_quadril;
    private TextView et_coxa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla4);


        et_braco = findViewById(R.id.et_braco);
        et_cintura = findViewById(R.id.et_cintura);
        et_abdominal = findViewById(R.id.et_abdominal);
        et_quadril = findViewById(R.id.et_quadril);
        et_coxa = findViewById(R.id.et_coxa);


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



                if(p.getDatosAbdominal() != null) {
                    et_abdominal.setText(p.getDatosAbdominal().toString());
                }
                if(p.getDatosBrazo() != null) {
                    et_braco.setText(p.getDatosBrazo().toString());
                }

                if(p.getDatosCintura() != null) {
                    et_cintura.setText(p.getDatosCintura().toString());
                }

                if(p.getDatosQuadril() != null) {
                    et_quadril.setText(p.getDatosQuadril().toString());
                }

                if(p.getDatosCoxa() != null) {
                    et_coxa.setText(p.getDatosCoxa().toString());
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

        if(!et_abdominal.getText().toString().equals(""))
            mRef.child("datosAbdominal").setValue(Integer.parseInt(et_abdominal.getText().toString()));
        if(!et_braco.getText().toString().equals(""))
            mRef.child("datosBrazo").setValue(Integer.parseInt(et_braco.getText().toString()));
        if(!et_cintura.getText().toString().equals(""))
            mRef.child("datosCintura").setValue(Integer.parseInt(et_cintura.getText().toString()));
        if(!et_coxa.getText().toString().equals(""))
            mRef.child("datosCoxa").setValue(Integer.parseInt(et_coxa.getText().toString()));
        if(!et_quadril.getText().toString().equals(""))
            mRef.child("datosQuadril").setValue(Integer.parseInt(et_quadril.getText().toString()));


        Intent intent = new Intent(this, ConfeccionarPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity3.class);
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
