package com.traineragenda.DiasEtapas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.DiasPlanilla.DetalleDiaPlanillaActivity;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity5;
import com.traineragenda.Planillas.Planilla;
import com.traineragenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NuevoDiaetapaActivity extends AppCompatActivity {

    private Long planilla_id;
    private Long dia_id;
    private Long etapa_id;
    private Long pace;

    private TextView tv_fecha;
    private TextView tv_distancia;
    private TextView tv_treino;
    private TextView tv_tiempo;
    private TextView tv_pace;
    //private TextView tv_fc;
    //private TextView tv_velocidad;
    private Spinner spinner_tipo;
    private Spinner spinner_zona;

    Calendar calendario_final = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_dia_etapa);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);
        dia_id = intent.getLongExtra("mesg_dia_id",0);
        etapa_id = intent.getLongExtra("mesg_etapa_id",0);



        tv_distancia = findViewById(R.id.tv_distancia);
        tv_treino = findViewById(R.id.tv_treino);
        tv_tiempo = findViewById(R.id.tv_tiempo);
        tv_pace = findViewById(R.id.tv_pace);
        //tv_fc = findViewById(R.id.tv_fc);
        //tv_velocidad = findViewById(R.id.tv_velocidad);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_zona = findViewById(R.id.spinner_zona);



        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fdb.getReference("Planillas");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                DataSnapshot d = dataSnapshot.child("Planilla_"+planilla_id);
                Planilla p = d.getValue(Planilla.class);

                pace = p.getDatosPaceM();



            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e("ERROR FIREBASE",error.getMessage());
            }

        });

        spinner_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Integer i = (int) spinner_zona.getSelectedItemId();
                switch (i){
                    case 0:
                        tv_pace.setText("0"+(pace+1)+"’20” a 0"+(pace-1)+"’50”");
                        //tv_fc.setText("127-149");
                        //tv_velocidad.setText("8,18 - 10,3");
                        break;
                    case 1:
                        tv_pace.setText("0"+ pace +"’59” a 0"+pace+"’00”");
                        //tv_fc.setText("155-162");
                        //tv_velocidad.setText("10,29 - 11,71");
                        break;
                    case 2:
                        tv_pace.setText("0"+(pace-1)+"’08” a 0"+(pace-2)+"’50”");
                        //tv_fc.setText("162-175");
                        //tv_velocidad.setText("11,68 - 12,38");
                        break;
                    case 3:
                        tv_pace.setText("0"+(pace-2)+"’38” a 0"+(pace-2)+"’24”");
                        //tv_fc.setText("178-182");
                        //tv_velocidad.setText("12,95 - 13,63");
                        break;
                    case 4:
                        tv_pace.setText("0"+(pace-2)+"’29” a 0"+(pace-1)+"’06”");
                        //tv_fc.setText("182-182");
                        //tv_velocidad.setText("13,36 - 14,58");
                        break;
                    case 5:
                        if(pace > 3)
                            tv_pace.setText("0"+(pace-2)+"’00” a 0"+(pace-3)+"’45”");
                        else
                            tv_pace.setText("04’00” a 03’45”");

                        //tv_fc.setText("180-190");
                        //tv_velocidad.setText("15,00");
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    public  void volver(){
        Intent intent = new Intent(this, DetalleDiaPlanillaActivity.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        intent.putExtra("mesg_dia_id", dia_id);
        startActivity(intent);
    }


    public void onActionGuardar(View view) {

        //Codigo para crear una planilla nuevo
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Date date = calendario_final.getTime();
        Long distancia = Long.parseLong(tv_distancia.getText().toString());
        String entrenamiento = tv_treino.getText().toString();
        Long tipo = spinner_tipo.getSelectedItemId();
        String tiempo = tv_tiempo.getText().toString();
        String pace = tv_pace.getText().toString();
        //String fc = tv_fc.getText().toString();
        //String velocidad = tv_velocidad.getText().toString();
        Long zona = spinner_zona.getSelectedItemId();

        DiaEtapa a = new DiaEtapa(dia_id,planilla_id,etapa_id,0l,tipo,tiempo,pace,zona,"","");


        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas").child("dia_"+dia_id).child("etapas").child("etapa_"+etapa_id);
        mRef.setValue(a);

        Intent intent = new Intent(this, DetalleDiaPlanillaActivity.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        intent.putExtra("mesg_dia_id", dia_id);
        startActivity(intent);
    }


    public void onActionVolver(View view) {
        volver();
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
