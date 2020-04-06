package com.traineragenda.DiasEtapas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import com.traineragenda.otros.DatabaseUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetalleDiaEtapaActivity extends AppCompatActivity {

    private Long planilla_id;
    private Long dia_id;
    private Long etapa_id;
    private Long pace;

    private TextView tv_fecha;
    //private TextView tv_distancia;
    //private TextView tv_treino;
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
        setContentView(R.layout.activity_detalle_dia_etapa);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);
        dia_id = intent.getLongExtra("mesg_dia_id",0);
        etapa_id = intent.getLongExtra("mesg_etapa_id",0);


        //tv_distancia = findViewById(R.id.tv_distancia);
        //tv_treino = findViewById(R.id.tv_treino);
        tv_tiempo = findViewById(R.id.tv_tiempo);
        tv_pace = findViewById(R.id.tv_pace);
        // tv_fc = findViewById(R.id.tv_fc);
        //tv_velocidad = findViewById(R.id.tv_velocidad);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_zona = findViewById(R.id.spinner_zona);


        //MEtodo para buscar un dia de planilla de la lista
        DatabaseUtil.findDiaEtapaById(planilla_id, dia_id, etapa_id, new DatabaseUtil.ListenerDiaEtapa() {
                    @Override
                    public void onDiaEtapaRetrieved(DiaEtapa d_etapa) {
                        DiaEtapa etapa = d_etapa;

                        //tv_distancia.setText(etapa.getDistancia().toString());
                        tv_pace.setText(etapa.getIntensidad());
                        //tv_fc.setText(etapa.getFc());
                        //tv_velocidad.setText(etapa.getVelocidad());
                        tv_tiempo.setText(etapa.getDuracion());
                        spinner_tipo.setSelection(  etapa.getTipo().intValue(),true);
                        spinner_zona.setSelection(  etapa.getZona().intValue(),true);

                    }
                });

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
                        tv_pace.setText("07’20” a 05’50”");
                        //tv_fc.setText("127-149");
                        //tv_velocidad.setText("8,18 - 10,3");
                        break;
                    case 1:
                        tv_pace.setText("05’49” a 05’07”");
                        //tv_fc.setText("155-162");
                        //tv_velocidad.setText("10,29 - 11,71");
                        break;
                    case 2:
                        tv_pace.setText("05’08” a 04’50”");
                        //tv_fc.setText("162-175");
                        //tv_velocidad.setText("11,68 - 12,38");
                        break;
                    case 3:
                        tv_pace.setText("04’38” a 04’24”");
                        //tv_fc.setText("178-182");
                        //tv_velocidad.setText("12,95 - 13,63");
                        break;
                    case 4:
                        tv_pace.setText("04’29” a 04’06”");
                        //tv_fc.setText("182-182");
                        //tv_velocidad.setText("13,36 - 14,58");
                        break;
                    case 5:
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

    public void onActionVolver(View view) {
        volver();
    }

    public void onActionGuardar(View view) {

        //Codigo para crear una planilla nuevo
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Date date = calendario_final.getTime();
        //Long distancia = Long.parseLong(tv_distancia.getText().toString());
        //String entrenamiento = tv_treino.getText().toString();
        Long tipo = spinner_tipo.getSelectedItemId();
        String tiempo = tv_tiempo.getText().toString();
        String pace = tv_pace.getText().toString();
        //String fc = tv_fc.getText().toString();
        //String velocidad = tv_velocidad.getText().toString();
        Long zona = spinner_zona.getSelectedItemId();

        DiaEtapa a = new DiaEtapa(dia_id,planilla_id,etapa_id,0l,tipo,tiempo,pace,zona,"","");


        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas").child("dia_"+dia_id).child("etapas").child("etapa_"+etapa_id);
        mRef.setValue(a);

        volver();

    }

    public  void volver(){
        Intent intent = new Intent(this, DetalleDiaPlanillaActivity.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        intent.putExtra("mesg_dia_id", dia_id);
        startActivity(intent);
    }

    public void onActionBorrar(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(true);
        builder.setTitle("Apagar");
        builder.setMessage("Tem certeza de que deseja excluir esta etapa do dia?");
        builder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas").child("dia_"+dia_id).child("etapas");
                        mRef.child("etapa_"+etapa_id).removeValue();

                        volver();
                    }
                });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();



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
