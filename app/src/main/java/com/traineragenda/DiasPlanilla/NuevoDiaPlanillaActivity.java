package com.traineragenda.DiasPlanilla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.traineragenda.Planillas.ConfeccionarPlanilla.ConfeccionarPlanillaActivity5;
import com.traineragenda.Planillas.ConfeccionarPlanilla.PedirDosFechasActivity;
import com.traineragenda.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NuevoDiaPlanillaActivity extends AppCompatActivity {

    private Long planilla_id;
    private Long dia_id;

    private TextView tv_fecha;
    private TextView tv_distancia;
    private TextView tv_treino;
    private TextView tv_tiempo;
    private TextView tv_pace;
    private Spinner spinner_tipo;

    Calendar calendario_final = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_dia_planilla);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        planilla_id = intent.getLongExtra("mesg_planilla_id",0);
        dia_id = intent.getLongExtra("mesg_dia_id",0);

        tv_fecha = findViewById(R.id.tv_fecha);
        tv_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NuevoDiaPlanillaActivity.this, date_final, calendario_final
                        .get(Calendar.YEAR), calendario_final.get(Calendar.MONTH),
                        calendario_final.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_distancia = findViewById(R.id.tv_distancia);
        tv_treino = findViewById(R.id.tv_treino);
        tv_tiempo = findViewById(R.id.tv_tiempo);
        tv_pace = findViewById(R.id.tv_pace);
        spinner_tipo = findViewById(R.id.spinner_tipo);


        tv_distancia.setText("10");

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

        DiaPlanilla a = new DiaPlanilla(dia_id,planilla_id,date,distancia,entrenamiento,tipo,tiempo,pace,0l);


        DatabaseReference mRef = database.getReference().child("Planillas").child("Planilla_" + (planilla_id)).child("diaPlanillas").child("dia_"+dia_id);
        mRef.setValue(a);

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
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        tv_fecha.setText(sdf.format(calendario_final.getTime()));
    }

}
