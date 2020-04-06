package com.traineragenda.Planillas.ConfeccionarPlanilla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.traineragenda.DiasEtapas.DiaEtapa;
import com.traineragenda.DiasPlanilla.DiaPlanilla;
import com.traineragenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PedirDosFechasActivity extends AppCompatActivity {

    Calendar calendario_inicial = Calendar.getInstance();
    Calendar calendario_final = Calendar.getInstance();

    EditText etPlannedDateInicial;
    EditText etPlannedDateFinal;

    Long planilla_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_dos_fechas);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);

        etPlannedDateInicial = (EditText) findViewById(R.id.et_fecha_inicial);
        etPlannedDateInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PedirDosFechasActivity.this, date_inicio, calendario_inicial
                        .get(Calendar.YEAR), calendario_inicial.get(Calendar.MONTH),
                        calendario_inicial.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etPlannedDateFinal = (EditText) findViewById(R.id.et_fecha_final);
        etPlannedDateFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PedirDosFechasActivity.this, date_final, calendario_final
                        .get(Calendar.YEAR), calendario_final.get(Calendar.MONTH),
                        calendario_final.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        actualizarInputInicial();
        actualizarInputFinal();


    }

    public void onActionCrearDias(View view) {

        //Codigo para crear una planilla nuevo
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Long i=1l;

        Calendar start = calendario_inicial;//Calendar.getInstance();
        //start.setTime(startDate);
        Calendar end = calendario_final;//Calendar.getInstance();
        //end.setTime(endDate);
        end.add(Calendar.DATE, 1);

        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {

            DiaPlanilla a = new DiaPlanilla(i,planilla_id,date,0l,"",0l,"","",0l);

            DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + planilla_id).child("diaPlanillas").child("dia_"+i);
            mRef.setValue(a);

            DiaEtapa b = new DiaEtapa(i,planilla_id,1l,0l,1l,"","",0l,"","");

            DatabaseReference mRef_etapa = database.getReference().child("Planillas").child("Planilla_" + planilla_id).child("diaPlanillas").child("dia_"+i).child("etapas").child("etapa_"+1);
            mRef_etapa.setValue(b);


            i++;
        }

        Intent intent = new Intent(this, ConfeccionarPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
    }

    public void onActionVolver(View view) {
        Intent intent = new Intent(this, ConfeccionarPlanillaActivity5.class);
        intent.putExtra("mesg_planilla_id", planilla_id);
        startActivity(intent);
    }


    //Para bloquear el boton volver del telefono
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    DatePickerDialog.OnDateSetListener date_inicio = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            calendario_inicial.set(Calendar.YEAR, year);
            calendario_inicial.set(Calendar.MONTH, monthOfYear);
            calendario_inicial.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputInicial();
        }

    };

    private void actualizarInputInicial() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etPlannedDateInicial.setText(sdf.format(calendario_inicial.getTime()));
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

        etPlannedDateFinal.setText(sdf.format(calendario_final.getTime()));
    }
}
