package com.traineragenda.Planillas.VerPlanilla.ConfeccionarPlanilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;

public class VerPlanillaActivity4 extends AppCompatActivity {


    private Long planilla_id;


    private EditText et_braco;
    private EditText et_cintura;
    private EditText et_abdominal;
    private EditText et_quadril;
    private EditText et_coxa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confeccionar_planilla4);


        et_braco = findViewById(R.id.et_braco);
        et_cintura = findViewById(R.id.et_cintura);
        et_abdominal = findViewById(R.id.et_abdominal);
        et_quadril = findViewById(R.id.et_quadril);
        et_coxa = findViewById(R.id.et_coxa);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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




        Intent intent = new Intent(this, VerPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


    }


    public void onActionVolver(View view) {
        Intent intent = new Intent(this, VerPlanillaActivity3.class);
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
