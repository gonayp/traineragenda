package com.traineragenda.DiasPlanilla;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traineragenda.Compromisos.Compromiso;
import com.traineragenda.DiasEtapas.DetalleDiaEtapaActivity;
import com.traineragenda.DiasEtapas.DiaEtapa;
import com.traineragenda.DiasEtapas.DiaEtapaFragment;
import com.traineragenda.DiasEtapas.DiaEtapaRepository;
import com.traineragenda.Planillas.Anamnese.AnamneseActivity1;
import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity5;
import com.traineragenda.Planillas.ConfeccionarPlanilla.PedirDosFechasActivity;
import com.traineragenda.R;
import com.traineragenda.otros.DatabaseUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetalleDiaPlanillaActivity extends AppCompatActivity {

    private Long planilla_id;
    private Long dia_id;
    private Long total_etapas;

    private TextView tv_fecha;
    //private TextView tv_distancia;
    //private TextView tv_treino;
    //private TextView tv_tiempo;
    //private TextView tv_pace;
    private Spinner spinner_tipo;

    Calendar calendario_final = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_dia_planilla);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);
        dia_id = intent.getLongExtra("mesg_dia_id",0);

        tv_fecha = findViewById(R.id.tv_fecha);
        tv_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DetalleDiaPlanillaActivity.this, date_final, calendario_final
                        .get(Calendar.YEAR), calendario_final.get(Calendar.MONTH),
                        calendario_final.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //tv_distancia = findViewById(R.id.tv_distancia);
        //tv_treino = findViewById(R.id.tv_treino);
        //tv_tiempo = findViewById(R.id.tv_tiempo);
        //tv_pace = findViewById(R.id.tv_pace);
        spinner_tipo = findViewById(R.id.spinner_tipo);


        //MEtodo para buscar un dia de planilla de la lista
        DatabaseUtil.findDiaPlanillaById(planilla_id,dia_id, new DatabaseUtil.ListenerDiaPlanilla() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDiaPlanillaRetrieved(DiaPlanilla c) {
                DiaPlanilla d_planilla = c;
                if(d_planilla != null){
                    //tv_distancia.setText(d_planilla.getDistancia().toString());
                    //tv_pace.setText(d_planilla.getPace());
                    //tv_tiempo.setText(d_planilla.getTiempo());
                    //tv_treino.setText(d_planilla.getEntrenamiento());
                    spinner_tipo.setSelection(  d_planilla.getTipo().intValue(),true);

                    calendario_final.set(Calendar.YEAR, d_planilla.getDia().getYear());
                    calendario_final.set(Calendar.MONTH, d_planilla.getDia().getMonth());
                    calendario_final.set(Calendar.DAY_OF_MONTH, d_planilla.getDia().getDate());
                    actualizarInputFinal();
                }
            }
        });

        //Instancia a la base de datos
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        //apuntamos al nodo que queremos leer
        DatabaseReference myRef = fdb.getReference("Planillas/Planilla_" + planilla_id + "/diaPlanillas/dia_"+dia_id+"/etapas");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //borramos el repositorio de actividades
                DiaEtapaRepository.getInstance().deleteDiaEtapa();


                total_etapas = dataSnapshot.getChildrenCount();
                Iterable<DataSnapshot> list_dias = dataSnapshot.getChildren();



                for (int i = 0; i < total_etapas; i++) {
                    DiaEtapa a = list_dias.iterator().next().getValue(DiaEtapa.class);

                    DiaEtapaRepository.getInstance().addDiaEtapa(a);


                }


                DiaEtapaFragment leadsFragment = (DiaEtapaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.dias_container);

                if (leadsFragment == null) {
                    leadsFragment = DiaEtapaFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.dias_container, leadsFragment)
                            .commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ERROR FIREBASE", error.getMessage());
            }


        });


    }

    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
    }

    public void onActionNuevo(View view) {


        Intent intent = new Intent(this, DetalleDiaEtapaActivity.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        intent.putExtra("mesg_dia_id", dia_id);
        intent.putExtra("mesg_etapa_id", total_etapas+1);
        startActivity(intent);
    }

    public void onActionGuardar(View view) {

        //Codigo para crear una planilla nuevo
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Date date = calendario_final.getTime();
        //Long distancia = Long.parseLong(tv_distancia.getText().toString());
        //String entrenamiento = tv_treino.getText().toString();
        Long tipo = spinner_tipo.getSelectedItemId();
        //String tiempo = tv_tiempo.getText().toString();
        //String pace = tv_pace.getText().toString();

        DiaPlanilla a = new DiaPlanilla(dia_id,planilla_id,date,0l,"",tipo,"","",0l);


        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas").child("dia_"+dia_id);

        mRef.child("dia").setValue(date);
        mRef.child("tipo").setValue(tipo);
        //mRef.setValue(a);

        Intent intent = new Intent(this, ConfeccionarPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
    }

    public void onActionBorrar(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(true);
        builder.setTitle("Apagar");
        builder.setMessage("Tem certeza de que deseja excluir este dia da planilha?");
        builder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas");
                        mRef.child("dia_"+dia_id).removeValue();

                        Intent intent = new Intent(DetalleDiaPlanillaActivity.this, ConfeccionarPlanillaActivity5.class);
                        intent.putExtra("mesg_planilla_id", planilla_id);
                        startActivity(intent);
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

    DatePickerDialog.OnDateSetListener date_final = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            calendario_final.set(Calendar.YEAR, year);
            calendario_final.set(Calendar.MONTH, monthOfYear);
            calendario_final.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputFinal();
        }

    };

    private void actualizarInputFinal() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        tv_fecha.setText(sdf.format(calendario_final.getTime()));
    }
}
